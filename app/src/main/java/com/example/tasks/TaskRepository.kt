package com.example.tasks

import android.util.Log
import androidx.lifecycle.LiveData

class TaskRepository(private val tasksDao: TasksDao) {

    val ongoingTasks: List<OngoingTasks> = tasksDao.getOngoingTasks()
    val completedTasks: List<CompletedTasks> = tasksDao.getCompletedTasks()

    suspend fun insertOngoingTask(ongoingTask: OngoingTasks){
        tasksDao.insertOngoingTask(ongoingTask)
        Log.d("Checking", "Task ${ongoingTask.taskName} inserted")
    }

    suspend fun insertCompletedTask(completedTask: CompletedTasks){
        tasksDao.insertCompletedTask(completedTask)
    }

    suspend fun deleteOngoingTask(ongoingTask: OngoingTasks){
        tasksDao.deleteOngoingTask(ongoingTask)
    }

    suspend fun deleteCompletedTask(completedTask: CompletedTasks){
        tasksDao.deleteCompletedTask(completedTask)
    }

    suspend fun updateOngoingTask(ongoingTask: OngoingTasks){
        tasksDao.updateOngoingTasks(ongoingTask)
    }

    suspend fun updateCompletedTask(completedTask: CompletedTasks){
        tasksDao.updateCompletedTask(completedTask)
    }
}