package com.example.noty.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noty.data.TaskDataBase
import com.example.noty.data.model.Task
import com.example.noty.navigator.Navigator
import com.example.noty.domain.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.random.Random

class AddViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository: TaskRepository

    init {
        val taskDao = TaskDataBase.getDataBase(application.applicationContext).taskDao()
        repository = TaskRepository(taskDao = taskDao)
    }

    private fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    private suspend fun getById(id: Int): Task {
        return repository.getById(id)
    }

    private fun initList(): ArrayList<String> {
        val colors = ArrayList<String>()
        colors.add("#5E97F6")
        colors.add("#9CCC65")
        colors.add("#FF8A65")
        colors.add("#9E9E9E")
        colors.add("#9FA8DA")
        colors.add("#90A4AE")
        colors.add("#AED581")
        colors.add("#F6BF26")
        colors.add("#FFA726")
        colors.add("#4DD0E1")
        colors.add("#BA68C8")
        colors.add("#A1887F")
        return colors
    }

    private fun getRandomColor(): String {
        val random = Random
        val i1 = random.nextInt(11 - 0) + 0
        return initList()[i1]
    }

    fun chooseTask(task: Task?, title: String, navigator: Navigator, date: String, showDate: Boolean) {
        when {
            task != null && showDate -> {
                val taskEdited = Task(task.id, title, date, task.color)
                updateTask(taskEdited)
                navigator.goBack()
            }
            task != null && !showDate -> {
                val taskEdited = Task(task.id,title,"",task.color)
                updateTask(taskEdited)
                navigator.goBack()
            }
            title.isBlank() -> {
                navigator.goBack()
            }
            task == null && !showDate ->{
                val newTask = Task(0, title, "", getRandomColor())
                addTask(newTask)
                navigator.goBack()
            }
            else -> {
                val newTask = Task(0, title, date, getRandomColor())
                addTask(newTask)
                navigator.goBack()
            }
        }
    }
}