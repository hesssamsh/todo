package com.example.todoapp.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.todoapp.R
import com.example.todoapp.data.Task
import java.util.*

@BindingAdapter("date")
fun TextView.date(date: Date) {
    text = date.format()
}

@BindingAdapter("taskColor")
fun View.taskColor(task: Task) {

    if (task.deletedAt == null)
        if (task.isDone)
            setBackgroundResource(R.color.primaryDarkColor)
        else
            setBackgroundResource(R.color.secondaryColor)
    else
        setBackgroundResource(R.color.trashColor)
}