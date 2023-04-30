package com.example.tasks

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class CompletedRecyclerAdapter(private val tasks: ArrayList<CompletedTasks>): RecyclerView.Adapter<CompletedRecyclerAdapter.MyViewHolder>() {

    private var db: TasksDatabase? = null

    class MyViewHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView){
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDeadline: TextView = itemView.findViewById(R.id.task_deadline)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_btn)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.task_checkbox)

        val fire1: ImageView = itemView.findViewById(R.id.fire_1)
        val fire2: ImageView = itemView.findViewById(R.id.fire_2)
        val fire3: ImageView = itemView.findViewById(R.id.fire_3)
        val fire4: ImageView = itemView.findViewById(R.id.fire_4)
        val fire5: ImageView = itemView.findViewById(R.id.fire_5)

        val myContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tasks[position]

        holder.taskName.text = currentItem.taskName
        holder.taskDeadline.text = currentItem.deadline

        val difficulty = currentItem.difficulty
        val fireList = listOf(holder.fire1, holder.fire2, holder.fire3, holder.fire4, holder.fire5)
        setDifficulty(fireList, difficulty, holder.myContext)

        holder.deleteButton.setOnClickListener { openDeleteDialog(holder.myContext, position) }
        holder.taskCheckBox.isChecked = true
        holder.taskCheckBox.setOnCheckedChangeListener { compoundButton, _ ->
            run {
                if (!compoundButton.isChecked){
                    val dao = db?.getTasksDao()
                    val task = tasks[position]
                    val ongoingTask = OngoingTasks(task.taskName, task.deadline, task.difficulty)

                    dao?.insertOngoingTask(ongoingTask)
                    dao?.deleteCompletedTask(task)

                    tasks.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, tasks.size)
                }
            }
        }

        db = Room.databaseBuilder(
            holder.myContext.applicationContext, TasksDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries().build()
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    private fun openDeleteDialog(context: Context, position: Int){
        val deleteDialog = Dialog(context)
        deleteDialog.setContentView(R.layout.delete_dialog_layout)
        deleteDialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val noButton = deleteDialog.findViewById<Button>(R.id.no_btn)
        noButton.setOnClickListener { deleteDialog.dismiss() }
        val yesButton = deleteDialog.findViewById<Button>(R.id.yes_btn)
        yesButton.setOnClickListener { deleteTask(position, deleteDialog) }

        deleteDialog.show()
    }

    private fun deleteTask(position: Int, dialog: Dialog){
        val dao = db!!.getTasksDao()
        dao.deleteCompletedTask(tasks[position])

        tasks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, tasks.size)

        dialog.dismiss()
    }

    private fun setDifficulty(fireList: List<ImageView>, level: Int, context: Context){
        // reset difficulty
        for (fire in fireList){
            fire.setColorFilter(
                ContextCompat.getColor(context, R.color.grey),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        // set difficulty
        for (i in 0 until level){
            fireList[i].setColorFilter(
                ContextCompat.getColor(context, R.color.red),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }
}