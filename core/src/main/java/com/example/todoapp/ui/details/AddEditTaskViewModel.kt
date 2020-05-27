package com.example.todoapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.AppRepository
import com.example.todoapp.data.Task
import kotlinx.coroutines.launch
import java.util.*

class AddEditTaskViewModel(val taskId: Long) : ViewModel() {
    private val repository by lazy { AppRepository() }

    private val _addEditTaskDone = MutableLiveData<Boolean>()
    val addEditTaskDone: LiveData<Boolean>
        get() = _addEditTaskDone

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    init {
        viewModelScope.launch {
            val task = repository.getTask(taskId)
            _task.value = task
        }
    }

    private fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
            _addEditTaskDone.value = true
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            _addEditTaskDone.value = true
        }
    }

    fun addEditTask(newTask: Task) {
        if (_task.value == null) {
            addTask(newTask)
            return
        }
        val task = _task.value!!
        newTask.taskId = task.taskId
        newTask.updatedAt = Date()
        updateTask(newTask)
    }
}