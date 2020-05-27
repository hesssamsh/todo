package com.example.todoapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.util.DATABASE_NAME
import com.example.todoapp.util.DATABASE_VERSION
import com.example.todoapp.util.G

@TypeConverters(Converters::class)
@Database(entities = [Task::class, TaskFts::class], version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val taskDao: TaskDAO

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            G.context, AppDatabase::class.java, DATABASE_NAME
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance!!
            }
        }
    }
}

