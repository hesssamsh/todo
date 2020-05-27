package com.example.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task?)

    @Query("UPDATE task SET deleted_at =  strftime('%s','now') * 1000 WHERE task_id  = :taskId")
    suspend fun removeTask(taskId: Long)

    @Query("UPDATE task SET deleted_at = NULL WHERE task_id  = :taskId")
    suspend fun undoRemoveTask(taskId: Long)

    @Query("UPDATE task SET deleted_at = NULL WHERE task_id  IN (:tasksId)")
    suspend fun undoRemoveSelectedTasks(tasksId: List<Long>?)

    @Delete
    suspend fun deleteSelectedTasks(tasks: List<Task>?)

    @Query("UPDATE task SET deleted_at =  strftime('%s','now') * 1000 WHERE task_id  IN (:selectedTasksId)")
    suspend fun removeSelectedTasks(selectedTasksId: List<Long>?)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task WHERE task_id = :taskId LIMIT 1")
    suspend fun getTask(taskId: Long): Task

    @Query("SELECT * FROM task WHERE is_done = :isDone AND deleted_at IS NULL ORDER BY task_id DESC")
    fun getTasks(isDone: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE is_done = :isDone AND deleted_at IS NULL ORDER BY task_id DESC")
    fun sortTasksDesc(isDone: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE is_done = :isDone AND deleted_at IS NULL ORDER BY task_id ASC")
    fun sortTasksAsc(isDone: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM taskFts JOIN task ON (task.task_id = taskFts.rowId) WHERE taskFts MATCH :query AND task.is_done = :isDone AND deleted_at IS NULL ORDER BY task.task_id DESC")
    fun searchTasksDesc(query: String?, isDone: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM taskFts JOIN task ON (task.task_id = taskFts.rowId) WHERE taskFts MATCH :query AND task.is_done = :isDone AND deleted_at IS NULL ORDER BY task.task_id ASC")
    fun searchTasksAsc(query: String?, isDone: Boolean): Flow<List<Task>>

    @Query("UPDATE task SET is_done = :isDone WHERE task_id IN (:tasksId)")
    suspend fun setDoneSelectedTasks(tasksId: List<Long>?, isDone: Boolean)

    @Query("SELECT * FROM task WHERE deleted_at IS NOT NULL ORDER BY deleted_at DESC")
    fun getTrashTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE deleted_at < strftime('%s','now', '-3 days') * 1000")
    suspend fun getTrashTasksUntil(): List<Task>
}