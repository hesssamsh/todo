package com.example.todoapp.util

import androidx.preference.PreferenceManager

object PrefsManager {
    private var sharedPrefs = PreferenceManager.getDefaultSharedPreferences(G.context)

    fun getBoolean(key: String, defaul: Boolean = false): Boolean {
        return sharedPrefs.getBoolean(key, defaul)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }
}