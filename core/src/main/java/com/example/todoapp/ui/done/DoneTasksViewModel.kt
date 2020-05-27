package com.example.todoapp.ui.done

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.todoapp.data.AppRepository
import com.example.todoapp.data.Task

class DoneTasksViewModel : ViewModel() {

    private val repository by lazy { AppRepository() }
    val tasks: LiveData<List<Task>>
    private val isDone = true

    init {
        tasks = repository.getTasks(isDone).asLiveData()
    }
}