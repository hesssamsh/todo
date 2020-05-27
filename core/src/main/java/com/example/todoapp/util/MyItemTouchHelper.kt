package com.example.todoapp.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.interfaces.OnTaskItemSwipeListener

class MyItemTouchHelper(private val onTaskItemSwipeListener: OnTaskItemSwipeListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onTaskItemSwipeListener.onTaskItemSwipe(viewHolder.adapterPosition)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
}