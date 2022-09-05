package com.example.noty.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noty.data.TaskDataBase
import com.example.noty.data.model.Task
import com.example.noty.domain.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository: TaskRepository
    var task: LiveData<List<Task>>? = null

    init {
        val taskDao = TaskDataBase.getDataBase(application).taskDao()
        repository = TaskRepository(taskDao)
        task = repository.readTasks()
    }

    suspend fun getALlTask(): List<Task> {
        return repository.getAll()
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    suspend fun getById(id: Int): Task {
        return repository.getById(id)
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }
}