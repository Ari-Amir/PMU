package com.aco.pmu.appDatabase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.room.TypeConverter
import com.nguyenhoanglam.imagepicker.model.Image
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class Converters {

    val dateFormat = SimpleDateFormat("d MMMM YYYY", Locale.getDefault())

    @TypeConverter
    fun timestampToDate(value: Long?): String {
        val date = value?.let { Date(it) }
        val format = dateFormat
        return format.format(date)
    }
}
