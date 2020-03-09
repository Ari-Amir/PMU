package com.aco.pmu.appDatabase

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


class Converters {

    val dateFormat = SimpleDateFormat("d MMMM YYYY", Locale.getDefault())

    @TypeConverter
    fun dateFromTimestamp(value: Long?): String {
        val date = value?.let { Date(it) }
        val format = dateFormat
        return format.format(date)
    }
}
