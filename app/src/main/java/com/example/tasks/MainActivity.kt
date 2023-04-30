package com.example.tasks

import android.app.Dialog
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var difficulty = 1
var DATABASE_NAME = "task_database"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ongoingTasksFragment = OngoingTasksFragment()
        val completedTasksFragment = CompletedTasksFragment()

        // set the noTasksFragment to the container
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, ongoingTasksFragment)
            commit()
        }

        val ongoingTab = findViewById<TextView>(R.id.ongoing_tab)
        val completedTab = findViewById<TextView>(R.id.completed_tab)
        val newTaskButton = findViewById<ImageView>(R.id.new_task)

        ongoingTab.setTypeface(null, Typeface.BOLD)

        ongoingTab.setOnClickListener { replaceFragment(ongoingTasksFragment, completedTab, ongoingTab) }
        completedTab.setOnClickListener { replaceFragment(completedTasksFragment, ongoingTab, completedTab) }
        newTaskButton.setOnClickListener { openCreateTaskDialog() }
    }

    private fun replaceFragment(fragment: Fragment, prevTab: TextView, currTab: TextView){
        currTab.setTypeface(null, Typeface.BOLD)
        prevTab.setTypeface(null, Typeface.NORMAL)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun openCreateTaskDialog(){
        val createTaskDialog = Dialog(this)
        createTaskDialog.setContentView(R.layout.create_task_layout)
        createTaskDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val taskNameView = createTaskDialog.findViewById<EditText>(R.id.task_name)
        taskNameView.setOnClickListener {
            val textViewWarning = createTaskDialog.findViewById<TextView>(R.id.textview_warning)
            textViewWarning.text = ""
        }

        val taskDeadline = createTaskDialog.findViewById<TextView>(R.id.task_deadline)
        taskDeadline.text = getCurrentDate()

        val setDeadlineBtn = createTaskDialog.findViewById<CardView>(R.id.set_deadline_btn)
        setDeadlineBtn.setOnClickListener{ setDeadline(taskDeadline) }

        val fire1 = createTaskDialog.findViewById<ImageView>(R.id.fire_1)
        val fire2 = createTaskDialog.findViewById<ImageView>(R.id.fire_2)
        val fire3 = createTaskDialog.findViewById<ImageView>(R.id.fire_3)
        val fire4 = createTaskDialog.findViewById<ImageView>(R.id.fire_4)
        val fire5 = createTaskDialog.findViewById<ImageView>(R.id.fire_5)

        val fireList = listOf(fire1, fire2, fire3, fire4, fire5)

        fire1.setOnClickListener { setDifficulty(fireList, 1) }
        fire2.setOnClickListener { setDifficulty(fireList, 2) }
        fire3.setOnClickListener { setDifficulty(fireList, 3) }
        fire4.setOnClickListener { setDifficulty(fireList, 4) }
        fire5.setOnClickListener { setDifficulty(fireList, 5) }

        val cancel = createTaskDialog.findViewById<Button>(R.id.cancel_btn)
        cancel.setOnClickListener { createTaskDialog.dismiss() }
        val save = createTaskDialog.findViewById<Button>(R.id.save_btn)
        save.setOnClickListener { saveTask(createTaskDialog) }

        createTaskDialog.setCancelable(false)

        createTaskDialog.show()
    }

    private fun setDeadline(taskDeadline: TextView){
        val datePickerDialog = Dialog(this)
        datePickerDialog.setContentView(R.layout.date_picker_layout)
        datePickerDialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val datePicker: DatePicker = datePickerDialog.findViewById(R.id.date_picker)

        // set ok button to change text in dob view
        val okButton: Button = datePickerDialog.findViewById(R.id.date_picker_ok)
        okButton.setOnClickListener {
            val deadline = "${datePicker.dayOfMonth}/${datePicker.month}/${datePicker.year}"
            taskDeadline.text = deadline

            datePickerDialog.dismiss()
        }

        // set cancel button to close dialog
        val cancelButton: Button = datePickerDialog.findViewById(R.id.date_picker_cancel)
        cancelButton.setOnClickListener { datePickerDialog.dismiss() }

        datePickerDialog.show()
    }

    private fun getCurrentDate(): String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return current.format(formatter)
    }

    private fun setDifficulty(fireList: List<ImageView>, level: Int){
        // reset difficulty
        for (fire in fireList){
            fire.setColorFilter(
                ContextCompat.getColor(this, R.color.grey),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        // set difficulty
        for (i in 0 until level){
            fireList[i].setColorFilter(
                ContextCompat.getColor(this, R.color.red),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        difficulty = level
    }

    private fun saveTask(dialog: Dialog){
        val taskNameView = dialog.findViewById<EditText>(R.id.task_name)
        val taskName = taskNameView.text.toString()

        if (taskName != ""){
            val taskDeadlineView = dialog.findViewById<TextView>(R.id.task_deadline)
            val taskDeadline = taskDeadlineView.text.toString()

            val db = Room.databaseBuilder(
                applicationContext, TasksDatabase::class.java, DATABASE_NAME
            ).allowMainThreadQueries().build()

            val dao = db.getTasksDao()
            val ongoingTask = OngoingTasks(taskName, taskDeadline, difficulty)
            dao.insertOngoingTask(ongoingTask)

            dialog.dismiss()

            val ongoingTab = findViewById<TextView>(R.id.ongoing_tab)
            val completedTab = findViewById<TextView>(R.id.completed_tab)

            replaceFragment(OngoingTasksFragment(), completedTab, ongoingTab)
        }
        else {
            val textViewWarning = dialog.findViewById<TextView>(R.id.textview_warning)
            textViewWarning.text = getString(R.string.task_name_warning)
        }
    }
}