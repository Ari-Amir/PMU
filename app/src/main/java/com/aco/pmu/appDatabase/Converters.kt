package com.aco.pmu.appDatabase

import android.annotation.SuppressLint
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

    @TypeConverter
    fun stringToImage(imagesInString: ArrayList<String>?): MutableList<Image> {
        val imagesAsFiles = mutableListOf<Image>()
        if (imagesInString != null) {
            for (i in imagesInString) {
                val imageInByte = i.toByteArray()
                val decodedByte = Base64.getDecoder().decode(imageInByte)
                val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)

                val root: String = Environment.getExternalStorageDirectory().toString()
                val folder = File("$root/tempImages")
                folder.mkdirs()

                val uuid = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE

                val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss_SSS", Locale.getDefault()).format(Date())
                val fileName = "PMU_$timeStamp.jpg"
                val file = File(folder, fileName)


                // Compress the bitmap and save in jpg format
                if (file.exists()) file.delete ()
                try {
                    val stream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.flush()
                    stream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val image = Image(uuid, file.name, file.path)

                imagesAsFiles.add(image)
            }
        }
        return imagesAsFiles
    }
}
