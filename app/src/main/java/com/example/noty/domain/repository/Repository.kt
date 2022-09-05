package com.example.noty.domain

import com.example.noty.data.TaskDao
import com.example.noty.data.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    fun readTasks() = taskDao.readAllTasks()

    suspend fun addTask(task: Task): Long = taskDao.addTask(task = task)

    suspend fun deleteTask(task: Task): Int = taskDao.deleteTask(task = task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun getAll(): List<Task> = taskDao.getAll()

    suspend fun getById(id: Int): Task = taskDao.getById(id)

}