package com.example.noty.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.noty.data.model.Task

class MyDiffUtil(
    private val oldList: List<Task>,
    private val newList: List<Task>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}

