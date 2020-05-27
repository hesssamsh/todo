package com.example.todoapp.data

import android.app.Activity
import android.os.Parcelable
import androidx.core.app.ShareCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "task")
data class Task(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var taskId: Long = 0,

    val title: String,
    val description: String,

    @ColumnInfo(name = "created_at")
    var createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    var updatedAt: Date = Date(),

    @ColumnInfo(name = "deleted_at")
    var deletedAt: Date? = null,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean = false
) : Parcelable {

    fun share(activity: Activity) {
        val shareIntent = ShareCompat.IntentBuilder.from(activity)
            .setSubject(title)
            .setText(description)
            .setType("text/plain")
            .intent
        activity.startActivity(shareIntent)
    }
}