package com.example.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel (application: Application) : AndroidViewModel(application) {

    val ongoingTasks: List<OngoingTasks>
    val completedTasks: List<CompletedTasks>
    val repository: TaskRepository

    init {
        val dao = TasksDatabase.getDatabase(application).getTasksDao()
        repository = TaskRepository(dao)
        ongoingTasks = repository.ongoingTasks
        completedTasks = repository.completedTasks
    }

    fun deleteOngoingTask(task: OngoingTasks) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteOngoingTask(task)
    }

    fun deleteCompletedTask(task: CompletedTasks) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteCompletedTask(task)
    }

    fun updateOngoingTask(task: OngoingTasks) = viewModelScope.launch(Dispatchers.IO){
        repository.updateOngoingTask(task)
    }

    fun updateCompletedTask(task: CompletedTasks) = viewModelScope.launch(Dispatchers.IO){
        repository.updateCompletedTask(task)
    }

    fun addOngoingTask(task: OngoingTasks) = viewModelScope.launch(Dispatchers.IO){
        repository.insertOngoingTask(task)
    }

    fun addCompletedTask(task: CompletedTasks) = viewModelScope.launch(Dispatchers.IO){
        repository.insertCompletedTask(task)
    }
}