package com.example.todoapp.ui.tasks

import androidx.recyclerview.selection.ItemKeyProvider
import com.example.todoapp.data.Task

class TasksKeyProvide(private val adapter: TasksAdapter) : ItemKeyProvider<Task>(SCOPE_MAPPED) {
    override fun getKey(position: Int): Task? {
        return adapter.getKey(position)
    }

    override fun getPosition(key: Task): Int {
        return adapter.getPosition(key)
    }
}