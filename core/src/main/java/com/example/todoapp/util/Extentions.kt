package com.example.todoapp.util

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*

fun Date.format(): String {
    val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.ENGLISH)
    return dateTimeFormatter.format(this)
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun Fragment.getColor(@ColorRes colorRes: Int): Int = ContextCompat.getColor(requireContext(), colorRes)

fun Context.getVersionName(): String? {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo?.versionName
}

fun DrawerLayout.lockDrawer() = setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

fun DrawerLayout.unlockDrawer() = setDrawerLockMode(LOCK_MODE_UNLOCKED)

fun EditText.getValue() = text.trim().toString()