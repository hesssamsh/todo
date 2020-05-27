package com.example.todoapp.ui.tasks

import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task

class TasksTracker {

    companion object {
        fun create(recyclerView: RecyclerView): SelectionTracker<Task> {
            return SelectionTracker.Builder(
                    "mySelection",
                    recyclerView,
                    TasksKeyProvide(recyclerView.adapter as TasksAdapter),
                    TasksDetailsLookup(recyclerView),
                    StorageStrategy.createParcelableStorage(Task::class.java)
                )
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build()
        }
    }
}