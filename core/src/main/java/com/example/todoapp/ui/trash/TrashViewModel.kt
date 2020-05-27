package com.example.todoapp.ui.trash

import androidx.lifecycle.*
import com.example.todoapp.util.PrefsManager
import com.example.todoapp.data.AppRepository
import com.example.todoapp.data.Task
import com.example.todoapp.util.SHOW_TRASH_MESSAGE
import kotlinx.coroutines.launch

class TrashViewModel : ViewModel() {
    private val repository by lazy { AppRepository() }
    val trashTasks: LiveData<List<Task>>

    private val _showTrashMessage = MutableLiveData<Boolean>()
    val showTrashMessage: LiveData<Boolean>
        get() = _showTrashMessage

    private var _selectedTasks = MutableLiveData<List<Task>>()
    val selectedTasks: LiveData<List<Task>>
        get() = _selectedTasks

    init {
        trashTasks = repository.getTrashTasks().asLiveData()
        _showTrashMessage.value = PrefsManager.getBoolean(SHOW_TRASH_MESSAGE, true)
    }

    fun setSelectedTasks(tasks: List<Task>?) {
        _selectedTasks.value = tasks
    }

    fun deleteSelectedTasks() {
        viewModelScope.launch {
            val tasks = _selectedTasks.value
            repository.deleteSelectedTasks(tasks)
        }
    }

    fun undoRemoveSelectedTasks() {
        viewModelScope.launch {
            val tasks = _selectedTasks.value?.map { it.taskId }
            repository.undoRemoveSelectedTasks(tasks)
        }
    }

    fun hideTrahMessage() {
        _showTrashMessage.value = false
        PrefsManager.putBoolean(SHOW_TRASH_MESSAGE, false)
    }
}