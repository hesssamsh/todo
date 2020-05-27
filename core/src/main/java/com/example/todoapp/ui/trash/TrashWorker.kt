package com.example.todoapp.ui.trash

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.data.AppRepository

class TrashWorker(context: Context, parameters: WorkerParameters)
    : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        val repository = AppRepository()
        val trashTasks = repository.getTrashTasksUntil()
        repository.deleteSelectedTasks(trashTasks)
        return Result.success()
    }
}