package com.example.todoapp.ui.tasks

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task

class TasksDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Task>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Task>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        view?.let {
            return (recyclerView.getChildViewHolder(view) as TasksAdapter.TasksViewHolder).getItemDetails()
        }
        return null
    }
}