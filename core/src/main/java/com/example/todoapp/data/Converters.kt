package com.example.todoapp.data

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun timeStampToDate(timeStamp: Long?): Date? {
        return if (timeStamp != null) Date(timeStamp) else null
    }
}