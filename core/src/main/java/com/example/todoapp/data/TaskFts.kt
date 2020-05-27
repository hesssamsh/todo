package com.example.todoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "taskFts")
@Fts4(contentEntity = Task::class)
data class TaskFts(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    val rowId: Int,

    val title: String,
    val description: String
)