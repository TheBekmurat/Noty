package com.example.noty.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noty.data.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task): Long

    @Delete
    suspend fun deleteTask(task: Task): Int

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun readAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ")
    suspend fun getAll(): List<Task>

    @Query("SELECT * FROM task_table WHERE id = :id")
    suspend fun getById(id: Int): Task
}