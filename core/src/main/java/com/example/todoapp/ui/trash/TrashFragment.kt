package com.example.todoapp.ui.trash

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.FragmentTrashBinding
import com.example.todoapp.interfaces.OnTaskItemClickListener
import com.example.todoapp.ui.tasks.TasksAdapter
import com.example.todoapp.ui.tasks.TasksTracker
import com.example.todoapp.util.setGone
import com.example.todoapp.util.setVisible

class TrashFragment : Fragment(), OnTaskItemClickListener {

    private val viewModel: TrashViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private lateinit var adapterTrashTasks: TasksAdapter
    private var tracker: SelectionTracker<Task>? = null
    private lateinit var binding: FragmentTrashBinding
    private var actionMode: ActionMode? = null
    private var emptyTrashLayout: ViewStub? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTrashBinding.inflate(inflater, container, false)
        initializeRecyclerview()
        emptyTrashLayout = binding.emptyTrashLayout.viewStub
        viewModel.trashTasks.observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isNotEmpty())
                emptyTrashLayout?.setGone()
            else
                emptyTrashLayout?.setVisible()

            adapterTrashTasks.submitList(tasks)
        })

        viewModel.selectedTasks.observe(viewLifecycleOwner, Observer
        { tasksId ->
            if (tasksId.isEmpty()) {
                actionMode?.finish()
                return@Observer
            }

            if (actionMode == null)
                actionMode = (requireActivity() as AppCompatActivity).startSupportActionMode(TrashActionMode())
            actionMode?.title = tasksId.size.toString()
        })

        binding.btnClearMessageTrash.setOnClickListener {
            viewModel.hideTrahMessage()
        }

        viewModel.showTrashMessage.observe(viewLifecycleOwner, Observer
        { showTrashMessage ->
            if (showTrashMessage)
                binding.containerMessageTrash.setVisible()
            else
                binding.containerMessageTrash.setGone()
        })

        return binding.root
    }

    private fun initializeRecyclerview() {
        adapterTrashTasks = TasksAdapter(this)
        binding.rvTrash.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterTrashTasks
        }

        initializeTracker()
    }

    override fun onTaskItemClick(taskId: Long) {
        val action = TrashFragmentDirections.actionTrashTaskDetail(taskId)
        navController.navigate(action)
    }

    private fun initializeTracker() {
        tracker = TasksTracker.create(binding.rvTrash)
        adapterTrashTasks.setTracker(tracker)
        viewModel.selectedTasks.value?.let { tracker?.setItemsSelected(it, true) }
        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                viewModel.setSelectedTasks(tracker?.selection?.toList())
            }
        })
    }


    inner class TrashActionMode : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.trash_action_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.undoRemove -> viewModel.undoRemoveSelectedTasks()
                R.id.delete -> viewModel.deleteSelectedTasks()
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