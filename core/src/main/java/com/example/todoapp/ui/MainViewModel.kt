package com.example.todoapp.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todoapp.util.PrefsManager
import com.example.todoapp.R
import com.example.todoapp.data.AppRepository
import com.example.todoapp.data.Task
import com.example.todoapp.ui.trash.TrashWorker
import com.example.todoapp.util.WORK_NAME
import com.example.todoapp.util.getVersionName
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository()
    private val context = application.applicationContext

    private val _nightMode = MutableLiveData<Boolean>()
    val nightMode: LiveData<Boolean>
        get() = _nightMode

    private val _versionName = MutableLiveData<String>()
    val versionName: LiveData<String>
        get() = _versionName

    val trashTasks: LiveData<List<Task>>

    init {
        trashTasks = repository.getTrashTasks().asLiveData()
        _nightMode.value = PrefsManager.getBoolean(context.getString(R.string.night_mode_prefs_key))
        _versionName.value = context.getVersionName()
    }

    fun setNightMode(nightMode: Boolean) {
        viewModelScope.launch(IO) {
            delay(300)
            _nightMode.postValue(nightMode)
        }
    }

    fun initWork() {
        val constraint = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest =
            PeriodicWorkRequest.Builder(TrashWorker::class.java, 3, TimeUnit.DAYS)
                .setConstraints(constraint)
                .setInitialDelay(1, TimeUnit.HOURS)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }
}