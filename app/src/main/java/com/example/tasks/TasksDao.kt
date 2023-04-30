package com.example.tasks

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao

interface TasksDao {
    @Insert
    fun insertOngoingTask(task: OngoingTasks)
    @Insert
    fun insertCompletedTask(task: CompletedTasks)

    @Delete
    fun deleteOngoingTask(task: OngoingTasks)
    @Delete
    fun deleteCompletedTask(task: CompletedTasks)

    @Query("SELECT * from ongoingTasksTable order by id DESC")
    fun getOngoingTasks(): List<OngoingTasks>
    @Query("SELECT * from completedTasksTable order by id DESC")
    fun getCompletedTasks(): List<CompletedTasks>

    @Update
    fun updateOngoingTasks(task: OngoingTasks)
    @Update
    fun updateCompletedTask(task: CompletedTasks)
}