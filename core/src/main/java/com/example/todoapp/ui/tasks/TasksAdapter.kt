package com.example.todoapp.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.interfaces.OnTaskItemClickListener
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ItemTaskBinding

class TasksAdapter(private var onTaskItemClickListener: OnTaskItemClickListener) :
    ListAdapter<Task, TasksAdapter.TasksViewHolder>(TasksDiffCallback()) {
    private var tracker: SelectionTracker<Task>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(getItem(position), tracker?.isSelected(getItem(position)) ?: false, onTaskItemClickListener)
    }

    class TasksViewHolder private constructor(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): TasksViewHolder {
                val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return TasksViewHolder(binding)
            }
        }

        fun bind(task: Task?, isActivated: Boolean, onTaskItemClickListener: OnTaskItemClickListener) {
            binding.task = task
            binding.listener = onTaskItemClickListener
            itemView.isActivated = isActivated
            binding.executePendingBindings()
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Task>? = object : ItemDetailsLookup.ItemDetails<Task>() {
            override fun getSelectionKey(): Task? = binding.task
            override fun getPosition(): Int = adapterPosition
        }
    }

    fun getItemAt(position: Int): Task? {
        return getItem(position)
    }

    // TasksKeyProvide methods
    fun getKey(position: Int): Task? {
        return getItem(position)
    }

    fun getPosition(task: Task): Int {
        return currentList.indexOf(task)
    }

    fun setTracker(tracker: SelectionTracker<Task>?) {
        this.tracker = tracker
    }
}