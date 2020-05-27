package com.example.todoapp.ui.tasks

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.interfaces.OnTaskItemClickListener
import com.example.todoapp.interfaces.OnTaskItemSwipeListener
import com.example.todoapp.util.MyItemTouchHelper
import com.example.todoapp.util.getColor
import com.example.todoapp.util.setGone
import com.example.todoapp.util.setVisible
import com.google.android.material.snackbar.Snackbar

class TasksFragment : Fragment(), OnTaskItemClickListener, OnTaskItemSwipeListener {
    private val viewModel: TasksViewModel by viewModels()
    private lateinit var binding: FragmentTasksBinding
    private val navController by lazy { findNavController() }
    private lateinit var tasksAdapter: TasksAdapter
    private var actionMode: ActionMode? = null
    private var tracker: SelectionTracker<Task>? = null
    private var copyMenu: MenuItem? = null
    private var shareMenu: MenuItem? = null
    private var viewStub: ViewStub? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        initializeRecyclerview()

        binding.fabOpenAddEditTask.setOnClickListener {
            navController.navigate(R.id.action_tasks_detail)
        }

        viewStub = binding.emptyTasksLayout.viewStub
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isNotEmpty())
                viewStub?.setGone()
            else
                viewStub?.setVisible()
            
            tasksAdapter.submitList(tasks)
            viewModel.changeSortType(false)
        })

        viewModel.searchResult.observe(viewLifecycleOwner, Observer { tasks ->
            if (viewModel.inSearchMode) {
                tasksAdapter.submitList(tasks)
                viewModel.inSearchMode = false
            }
        })

        viewModel.sortResult.observe(viewLifecycleOwner, Observer { tasks ->
            if (viewModel.inSortMode) {
                tasksAdapter.submitList(tasks)
                viewModel.inSortMode = false
            }
        })

        viewModel.selectedTasks.observe(viewLifecycleOwner, Observer { tasksId ->
            if (tasksId.isEmpty()) {
                actionMode?.finish()
                return@Observer
            }

            if (actionMode == null)
                actionMode = (requireActivity() as AppCompatActivity).startSupportActionMode(MyActionMode())
            refreshActionMode(tasksId.size)
        })

        viewModel.removeTask.observe(viewLifecycleOwner, Observer { taskId ->
            taskId?.let {
                showUndoSnackbar(taskId)
                viewModel.doneTaskDeleted()
            }
        })
        return binding.root
    }

    private fun showUndoSnackbar(taskId: Long) {
        Snackbar.make(binding.root, getString(R.string.undo_action), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                viewModel.undoRemoveTask(taskId)
            }
            .setActionTextColor(getColor(R.color.secondaryDarkColor))
            .show()
    }

    private fun refreshActionMode(itemsCount: Int) {
        actionMode?.title = itemsCount.toString()
        copyMenu?.isVisible = itemsCount < 2
        shareMenu?.isVisible = itemsCount < 2
    }

    private fun initializeRecyclerview() {
        tasksAdapter = TasksAdapter(this)
        val itemTouchHelper = MyItemTouchHelper(this)
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(this)
        }

        initializeTracker()
    }

    private fun initializeTracker() {
        tracker = TasksTracker.create(binding.recyclerview)
        tasksAdapter.setTracker(tracker)
        viewModel.selectedTasks.value?.let { tracker?.setItemsSelected(it, true) }
        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                viewModel.setSelectedTasks(tracker?.selection?.toList())
            }
        })
    }

    override fun onTaskItemClick(taskId: Long) {
        if (actionMode != null) return
        navController.navigate(TasksFragmentDirections.actionTasksDetail(taskId))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tasks, menu)
        initializeSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_tasks -> {
                viewModel.changeSortType(true)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeSearchView(menu: Menu) {
        val searchView = menu.findItem(R.id.search_tasks).actionView as SearchView
        val listener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.tasks.removeObservers(viewLifecycleOwner)
                viewModel.tasks.observe(viewLifecycleOwner, Observer { task ->
                    tasksAdapter.submitList(task)
                })
                return true
            }
        }

        menu.findItem(R.id.search_tasks).setOnActionExpandListener(listener)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onTaskItemSwipe(position: Int) {
        viewModel.removeTask(tasksAdapter.getItemAt(position)?.taskId ?: -1)
    }

    inner class MyActionMode : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_action_mode, menu)
            copyMenu = mode?.menu?.findItem(R.id.duplicate_task)
            shareMenu = mode?.menu?.findItem(R.id.share_task)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.remove_selected_tasks -> viewModel.removeSelectedTasks()
                R.id.duplicate_task -> viewModel.duplicateTask()
                R.id.share_task -> viewModel.selectedTasks.value?.get(0)?.share(requireActivity())
                R.id.set_done_task -> viewModel.setDoneSelectedTasks(true)
            }
            mode?.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            tracker?.clearSelection()
        }
    }
}