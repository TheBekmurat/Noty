package com.example.noty.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.noty.R
import com.example.noty.ui.base.BaseFragment
import com.example.noty.databinding.FragmentListBinding
import com.example.noty.data.model.Task
import com.example.noty.navigator.navigator
import com.example.noty.ui.adapter.Click
import com.example.noty.ui.adapter.ListAdapter
import com.example.noty.ui.viewModel.ListViewModel
import com.example.noty.utils.ItemToucheCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : BaseFragment<FragmentListBinding>() {

    private lateinit var adapter: ListAdapter
    private val viewModel by viewModels<ListViewModel> {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initItemTouch()
        initListeners()
        initToolBar()
    }

    private fun initAdapter() {
        adapter = ListAdapter(click = object : Click {
            override fun click(task: Task) {
                navigator().launch(task)
            }
        })
        binding.rv.adapter = adapter
        viewModel.task?.observe(viewLifecycleOwner) {
            adapter.tasks = it
            checkOnFresh(it)
        }
    }

    private fun initListeners() {
        binding.fab.setOnClickListener {
            navigator().launch()
        }
    }

    private fun initToolBar() {
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.inflateMenu(R.menu.menu_main)
        requireActivity().setActionBar(binding.toolbar)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    navigator().launchSetting()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun initItemTouch() {
        object : ItemTouchHelper(ItemToucheCallBack { viewHolder ->
            lifecycleScope.launch(Dispatchers.Main) {
                val position = viewHolder.adapterPosition
                val taskToDelete = viewModel.getALlTask()[position]
                viewModel.deleteTask(taskToDelete)
                adapter.notifyItemRemoved(position)
            }
        }) {}.attachToRecyclerView(binding.rv)
    }

    private fun isDateInPast(oldFormat: String): Boolean {
        val currentTime = SimpleDateFormat().format(Date())
        val currentDate = SimpleDateFormat().parse(currentTime)
        val oldDate = SimpleDateFormat("MMM dd,yyyy HH:mm", Locale.getDefault()).parse(oldFormat)
        return oldDate.before(currentDate)
    }

    private fun checkOnFresh(tasks: List<Task>) {
        for (task in tasks) {
            if (task.date != "") {
                if (isDateInPast(task.date)) {
                    viewModel.updateTask(task.copy(date = ""))
                }
            }
        }
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentListBinding =
        FragmentListBinding.inflate(inflater, container, false)
}
