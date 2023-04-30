package com.example.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="completedTasksTable")

class CompletedTasks (
    @ColumnInfo(name="task_name") val taskName: String,
    @ColumnInfo(name="deadline") val deadline: String,
    @ColumnInfo(name="difficulty") val difficulty: Int
){
    @PrimaryKey(autoGenerate=true) var id=0
}