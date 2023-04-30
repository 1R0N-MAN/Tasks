package com.example.tasks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OngoingTasks::class, CompletedTasks::class], version=1, exportSchema = false)
abstract class TasksDatabase: RoomDatabase(){

    abstract fun getTasksDao(): TasksDao

    companion object{
        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun getDatabase(context: Context): TasksDatabase {
            // return instance if it is not null else create a new database
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TasksDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}