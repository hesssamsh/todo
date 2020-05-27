package com.example.todoapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddEditViewModelFactory(val taskId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddEditTaskViewModel::class.java))
            AddEditTaskViewModel(taskId) as T
        else
            throw IllegalArgumentException("viewModel not found")
    }
}