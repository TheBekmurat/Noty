package com.example.noty.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.*

typealias DateListener = (Any?, Int, Int, Int) -> Unit

class DatePickerFragment(context: Context, private val dateListener: DateListener) :
    DatePickerDialog.OnDateSetListener {

    val datePickerDialog =
        object : DatePickerDialog(context, this, Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){}

    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
        dateListener.invoke(picker, year, month, day)
    }
}