package com.example.todoapp.ui.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import com.example.todoapp.util.getValue
import com.example.todoapp.util.setGone
import com.example.todoapp.util.setVisible

class AddEditTaskFragment : Fragment() {
    private val args: AddEditTaskFragmentArgs by navArgs()
    private val viewModel: AddEditTaskViewModel by viewModels(factoryProducer = {
        AddEditViewModelFactory(args.taskId)
    })
    private lateinit var binding: FragmentAddEditTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        viewModel.task.observe(viewLifecycleOwner, Observer { task ->
            if (task != null) {
                binding.task = task
                binding.chbIsDone.setVisible()
                requireActivity().title = getString(R.string.edit_task)
            } else {
                binding.chbIsDone.setGone()
                requireActivity().title = getString(R.string.add_edit_task)
            }
        })

        // for saving edittext values due to rotation change
        binding.executePendingBindings()
        viewModel.addEditTaskDone.observe(viewLifecycleOwner, Observer { isDone ->
            if(isDone)
                requireActivity().onBackPressed()
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        when (args.taskId) {
            0L -> inflater.inflate(R.menu.menu_add_task, menu)
            else -> inflater.inflate(R.menu.menu_edit_task, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_task -> {
                viewModel.task.value?.share(requireActivity())
                true
            }

            R.id.add_edit_task -> {
                val task = Task(title = binding.edtTitleAddTask.getValue(),
                    description = binding.edtDescAddTask.getValue(),
                    isDone = binding.chbIsDone.isChecked)
                viewModel.addEditTask(task)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}