package com.example.todoapp.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.interfaces.OnTaskItemClickListener
import com.example.todoapp.databinding.FragmentDoneTasksBinding
import com.example.todoapp.ui.tasks.TasksAdapter
import com.example.todoapp.util.setGone
import com.example.todoapp.util.setVisible

class DoneTasksFragment : Fragment(), OnTaskItemClickListener {

    private val viewModel: DoneTasksViewModel by viewModels()
    private lateinit var binding: FragmentDoneTasksBinding
    private lateinit var doneTasksAdapter: TasksAdapter
    private val navController by lazy { findNavController() }
    private var viewStub: ViewStub? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoneTasksBinding.inflate(inflater, container, false)
        initializeRecyclerview()
        viewStub = binding.emptyDoneTasksLayout.viewStub
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isNotEmpty())
                viewStub?.setGone()
            else
                viewStub?.setVisible()

            doneTasksAdapter.submitList(tasks)
        })
        return binding.root
    }

    private fun initializeRecyclerview() {
        doneTasksAdapter = TasksAdapter(this)
        binding.rvDoneTasks.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = doneTasksAdapter
        }
    }

    override fun onTaskItemClick(taskId: Long) {
        val action = DoneTasksFragmentDirections.actionDoneTasksDetail(taskId)
        navController.navigate(action)
    }
}