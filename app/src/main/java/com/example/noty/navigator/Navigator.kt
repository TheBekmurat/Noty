package com.example.noty.navigator

import androidx.fragment.app.Fragment
import com.example.noty.data.model.Task

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {
    fun launch(task: Any? = null)

    fun goBack()

    fun update(task: Task)

    fun launchSetting()
}