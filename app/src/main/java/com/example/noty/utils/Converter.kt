package com.example.noty.utils

import androidx.room.TypeConverter
import java.util.*

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value?.let { value ->
        Calendar.getInstance().also { calendar ->
            calendar.timeInMillis = value
        }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis
}