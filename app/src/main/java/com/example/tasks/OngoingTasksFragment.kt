package com.example.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class OngoingTasksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =inflater.inflate(R.layout.fragment_ongoing_tasks, container, false)
        val loadedTasks = loadTasks()

        if (loadedTasks.isEmpty()){
            this.requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, NoTasksFragment())
                addToBackStack(null)
                commit()
            }
        }

        val adapter = OngoingRecyclerAdapter(loadedTasks)
        val ongoingRecycler = view.findViewById<RecyclerView>(R.id.ongoing_recycler)
        ongoingRecycler.adapter = adapter

        return view
    }

    private fun loadTasks(): ArrayList<OngoingTasks> {

        val tasks = ArrayList<OngoingTasks>()
        val db = Room.databaseBuilder(
            this.requireContext().applicationContext, TasksDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries().build()

        val dao = db.getTasksDao()
        val ongoingTasks = dao.getOngoingTasks()
        tasks.addAll(ongoingTasks)

        return tasks
    }
}