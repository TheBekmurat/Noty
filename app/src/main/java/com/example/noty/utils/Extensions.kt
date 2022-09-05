package com.example.noty.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.formatDate(pattern: String , calendar: Calendar): String {
    val viewFormatter = SimpleDateFormat(pattern)
    return viewFormatter.format(calendar.time)
}

fun Activity.formatDate(pattern: String, calendar: Calendar): String {
    val viewFormatter = SimpleDateFormat(pattern)
    return viewFormatter.format(calendar.time)
}