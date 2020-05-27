package com.example.todoapp.util

import android.app.Application
import android.content.Context

class G : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}