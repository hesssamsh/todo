package com.example.todoapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.util.lockDrawer
import com.example.todoapp.util.unlockDrawer

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private val viewModel: MainViewModel by viewModels()
    private val navController by lazy { findNavController(R.id.navHostFragment) }
    private lateinit var binding: ActivityMainBinding
    private val rootFragments = setOf(R.id.allTasks, R.id.doneTasks, R.id.trash, R.id.settings)

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.nightMode.observe(this, Observer { nightMode ->
            setAppNightMode(nightMode)
        })
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        val appBarConfiguration = AppBarConfiguration(
            rootFragments, binding.drawerLayout)

        binding.navDrawer.setupWithNavController(navController)
        binding.toolbarMain.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(this)

        viewModel.trashTasks.observe(this, Observer {
            viewModel.initWork()
        })
    }

    private fun setAppNightMode(nightMode: Boolean) {
        if (nightMode)
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.allTasks -> {
                binding.drawerLayout.unlockDrawer()
                title = getString(R.string.all_tasks)
            }

            R.id.doneTasks -> {
                binding.drawerLayout.unlockDrawer()
                title = getString(R.string.done_tasks)
            }

            R.id.settings -> {
                binding.drawerLayout.unlockDrawer()
                title = getString(R.string.settings)
            }

            R.id.trash -> {
                binding.drawerLayout.unlockDrawer()
                title = getString(R.string.trash)
            }

            else -> binding.drawerLayout.lockDrawer()
        }
    }
}