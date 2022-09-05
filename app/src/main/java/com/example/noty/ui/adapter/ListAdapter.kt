package com.example.noty.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noty.databinding.ItemLayoutBinding
import com.example.noty.data.model.Task

class ListAdapter(
    private val click: Click,
) : RecyclerView.Adapter<ListAdapter.MainViewHolder>() {

    class MainViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    var tasks: List<Task> = emptyList()
        set(value) {
//            val diffUtil = MyDiffUtil(field, value)
//            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
//            field = value
//            diffUtilResult.dispatchUpdatesTo(this)
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = tasks[position]
        val randomAvatarColor = GradientDrawable().apply {
            setColor(Color.parseColor(task.color))
        }
        holder.binding.apply {
            tvTitle.text = task.title
            tvAvatar.background = randomAvatarColor
            tvDate.text = task.date
            if (task.title.isNotEmpty()) {
                val avatar = task.title.substring(0, 1)
                tvAvatar.text = avatar
            }
            root.setOnClickListener { click.click(task) }
        }
    }

    override fun getItemCount(): Int = tasks.size
}

interface Click {
    fun click(task: Task)
}