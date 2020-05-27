package com.example.todoapp.ui.tasks

import androidx.lifecycle.*
import com.example.todoapp.data.AppRepository
import com.example.todoapp.data.Task
import com.example.todoapp.util.SORT_BY_ID_ASC
import com.example.todoapp.util.SORT_BY_ID_DESC
import kotlinx.coroutines.launch
import java.util.*

class TasksViewModel : ViewModel() {
    private val repository by lazy { AppRepository() }
    private var _selectedTasks = MutableLiveData<List<Task>>()
    val selectedTasks: LiveData<List<Task>>
        get() = _selectedTasks

    private var isDone = false

    private val _removeTask = MutableLiveData<Long>()
    val removeTask: LiveData<Long>
        get() = _removeTask

    private val query = MutableLiveData<String>()
    var inSearchMode = false
    var inSortMode = false

    var tasks: LiveData<List<Task>>

    private val sortType = MutableLiveData(SORT_BY_ID_DESC)

    val sortResult = sortType.switchMap {
        if (sortType.value == SORT_BY_ID_DESC)
            repository.sortTasksDesc(isDone).asLiveData()
        else
            repository.sortTasksAsc(isDone).asLiveData()
    }

    val searchResult = query.switchMap {
        if (sortType.value == SORT_BY_ID_DESC)
            repository.searchTasksDesc(query.value, isDone).asLiveData()
        else
            repository.searchTasksAsc(query.value, isDone).asLiveData()
    }

    init {
        tasks = repository.getTasks(isDone).asLiveData()
    }

    fun changeSortType(inSortMode:Boolean) {
        if(!inSortMode) {
            sortType.value = SORT_BY_ID_DESC
            return
        }
        this.inSortMode = inSortMode
        sortType.value = if (sortType.value == SORT_BY_ID_DESC) SORT_BY_ID_ASC else SORT_BY_ID_DESC
    }

    fun duplicateTask() {
        viewModelScope.launch {
            val task = getDuplicateTask()
            repository.addTask(task)
        }
    }

    private fun getDuplicateTask(): Task? {
        val task = _selectedTasks.value?.get(0)
        val date = Date()
        task?.taskId = 0
        task?.createdAt = date
        task?.updatedAt = date
        return task
    }

    fun removeTask(taskId: Long) {
        viewModelScope.launch {
            repository.removeTask(taskId)
            _removeTask.postValue(taskId)
        }
    }

    fun doneTaskDeleted() {
        _removeTask.value = null
    }

    fun setQuery(q: String) {
        inSearchMode = true
        query.value = q
    }

    fun setSelectedTasks(tasks: List<Task>?) {
        _selectedTasks.value = tasks
    }

    fun removeSelectedTasks() {
        viewModelScope.launch {
            val selectedTasksId = _selectedTasks.value?.map { it.taskId }
            repository.removeSelectedTasks(selectedTasksId)
        }
    }

    fun setDoneSelectedTasks(isDone: Boolean) {
        viewModelScope.launch {
            val selectedTasksId = _selectedTasks.value?.map { it.taskId }
            repository.setDoneSelectedTasks(selectedTasksId, isDone)
        }
    }

    fun undoRemoveTask(taskId: Long) {
        viewModelScope.launch {
            repository.undoRemoveTask(taskId)
        }
    }
}