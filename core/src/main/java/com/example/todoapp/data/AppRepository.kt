package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

class AppRepository {
    private val appDatabase by lazy { AppDatabase.getInstance() }
    private val taskDAO by lazy { appDatabase.taskDao }

    suspend fun addTask(task: Task?) {
        taskDAO.addTask(task)
    }

    suspend fun removeTask(taskId: Long) {
        taskDAO.removeTask(taskId)
    }

    suspend fun removeSelectedTasks(selectedTasksId: List<Long>?) {
        taskDAO.removeSelectedTasks(selectedTasksId)
    }

    suspend fun updateTask(task: Task) {
        taskDAO.updateTask(task)
    }

    suspend fun getTask(taskId: Long): Task {
        return taskDAO.getTask(taskId)
    }

    fun getTasks(isDone: Boolean): Flow<List<Task>> {
        return taskDAO.getTasks(isDone)
    }

    suspend fun setDoneSelectedTasks(tasksId: List<Long>?, isDone: Boolean) {
        taskDAO.setDoneSelectedTasks(tasksId, isDone)
    }

    fun searchTasksDesc(query: String?, isDone: Boolean): Flow<List<Task>> {
        return taskDAO.searchTasksDesc(query, isDone)
    }

    fun searchTasksAsc(query: String?, isDone: Boolean): Flow<List<Task>> {
        return taskDAO.searchTasksAsc(query, isDone)
    }

    fun sortTasksAsc(isDone: Boolean): Flow<List<Task>> {
        return taskDAO.sortTasksAsc(isDone)
    }

    fun sortTasksDesc(isDone: Boolean): Flow<List<Task>> {
        return taskDAO.sortTasksDesc(isDone)
    }

    fun getTrashTasks(): Flow<List<Task>> {
        return taskDAO.getTrashTasks()
    }

    suspend fun undoRemoveTask(taskId: Long) {
        taskDAO.undoRemoveTask(taskId)
    }

    suspend fun undoRemoveSelectedTasks(tasksId: List<Long>?) {
        taskDAO.undoRemoveSelectedTasks(tasksId)
    }

    suspend fun deleteSelectedTasks(tasks: List<Task>?) {
        taskDAO.deleteSelectedTasks(tasks)
    }

    suspend fun getTrashTasksUntil(): List<Task> {
        return taskDAO.getTrashTasksUntil()
    }
}