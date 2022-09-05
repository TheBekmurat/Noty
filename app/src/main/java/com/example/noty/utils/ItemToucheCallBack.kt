package com.example.noty.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.noty.ui.adapter.ListAdapter

typealias SwipeListener = (RecyclerView.ViewHolder) -> Unit

class ItemToucheCallBack(private val listener: SwipeListener) :
    ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, START or END) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        val adapter = recyclerView.adapter as ListAdapter
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter.notifyItemMoved(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.invoke(viewHolder)
    }
}