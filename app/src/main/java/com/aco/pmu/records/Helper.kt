package com.aco.pmu.records

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import com.nguyenhoanglam.imagepicker.model.Image
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.roundToInt


class Helper {

    fun getImagesPaths (listOfUncompressedImages: MutableList<Image>): String {
        val imagesPathsList = mutableListOf<String>()
        var imagesPaths = ""

        for (i in listOfUncompressedImages) {
            val imagePath = "/data/data/com.aco.pmu/databases/Photos"
            val imageFullPath = "$imagePath/${i.name}"

            imagesPathsList.add(imageFullPath)
            imagesPaths = imagesPathsList.joinToString()
        }

        //get files in database
         val databaseFileList = mutableListOf<String>()
         val filesPath = File("//data/data//com.aco.pmu//databases//Photos")
         val list = filesPath.listFiles()

         if (list != null) {
             for (i in list) {
                 databaseFileList.add(i.name)
             }
         }
        
        //check if files to write are exist in database
        val filesToWriteList = mutableListOf<Image>()
        if (databaseFileList.size == 0) {
            for (i in listOfUncompressedImages) {
                filesToWriteList.add(i)
            }

        } else {
            for (i in listOfUncompressedImages) {
                val directory = "//data/data//com.aco.pmu//databases//Photos"
                val file = File(directory, i.name)
                if (!file.exists()) {
                    filesToWriteList.add(i)
                }
            }
        }
        
        //write files to database
        for (i in filesToWriteList) {
            val bitmap: Bitmap = BitmapFactory.decodeFile(i.path) //Convert the image into bitmap.
            val imageFile: File = File(i.path)
            val fileSize = (imageFile.length().toDouble() / 1048576 * 100.0).roundToInt() / 100.0
            val compressedBitmap: Bitmap

            compressedBitmap = when {
                fileSize >= 14.0 -> Bitmap.createScaledBitmap(bitmap, bitmap.width/5, bitmap.height/5, true)
                fileSize >= 12.0 -> Bitmap.createScaledBitmap(bitmap, (bitmap.width/4.5).toInt(), (bitmap.height/4.5).toInt(), true)
                fileSize >= 10.0 -> Bitmap.createScaledBitmap(bitmap, bitmap.width/4, bitmap.height/4, true)
                fileSize >= 8.0 -> Bitmap.createScaledBitmap(bitmap, (bitmap.width/3.5).toInt(), (bitmap.height/3.5).toInt(), true)
                fileSize >= 5.0 -> Bitmap.createScaledBitmap(bitmap, (bitmap.width/2.5).toInt(), (bitmap.height/2.5).toInt(), true)
                fileSize >= 3.0 -> Bitmap.createScaledBitmap(bitmap, (bitmap.width/2), (bitmap.height/2), true)
                fileSize >= 1.0 -> Bitmap.createScaledBitmap(bitmap, (bitmap.width/1.5).toInt(), (bitmap.height/1.5).toInt(), true)
                else -> bitmap
            }
            
            val exif = ExifInterface(i.path)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            when (orientation) {
                6 -> matrix.postRotate(90f)
                3 -> matrix.postRotate(180f)
                8 -> matrix.postRotate(270f)
            }

            val rotatedBitmap= Bitmap.createBitmap(compressedBitmap, 0, 0, compressedBitmap.width, compressedBitmap.height, matrix, true)

            val root = Environment.getDataDirectory()
            val folder = File(root, "//data//com.aco.pmu//databases//Photos")
            folder.mkdirs()

            val fileName = i.name
            val file = File(folder, fileName)

            if (file.exists()) file.delete()
            try {
                val stream = FileOutputStream(file)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                stream.flush()
                stream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return imagesPaths.replace(" ", "")
    }

    fun getImagesFromPath(path: String) : List<Image> {
        val list = mutableListOf<Image>()
        val stringToList = path.split(',')

        if (path.isNotEmpty()) {
            for (i in stringToList) {
                val imageName = File(i).name
                val imagePath = File(i).parent + "/" + imageName
                val imageId = String.format("%04d", Random().nextInt(10000)).toLong()
                val image = Image(imageId, imageName, imagePath)
                list.add(image)
            }
        }
        return list.toList()
    }
}
























//    fun convertByteArrayToImagesList(imageLength: String, imagesInByteArray: ByteArray) : MutableList<Image> {
//
//        val images = mutableListOf<Image>()
//        val listOfImagesLength = imageLength.split(",").map { it.trim() }
//        var fromIndex = 0
//        var toIndex = 0
//
//        for (l in listOfImagesLength) {
//            toIndex += l.toInt()
//            val pieceOfByteArray = imagesInByteArray.copyOfRange(fromIndex, toIndex)
//            val byteArrayToBitmap = BitmapFactory.decodeByteArray(pieceOfByteArray, 0, pieceOfByteArray.size)
//
//            val root: String = Environment.getExternalStorageDirectory().toString()
//            val folder = File("$root/tempImages")
//            folder.mkdirs()
//
//            val uuid = String.format("%04d", Random().nextInt(10000)).toLong()
//
//            val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss_SSS", Locale.getDefault()).format(Date())
//            val fileName = "PMU_$timeStamp.jpg"
//            val file = File(folder, fileName)
//
//            if (file.exists()) file.delete ()
//            try {
//                val stream = FileOutputStream(file)
//                byteArrayToBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//                stream.flush()
//                stream.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//            val image = Image(uuid, file.name, file.path)
//
//            images.add(image)
//
//            fromIndex = toIndex
//            toIndex = fromIndex
//        }
//        return images
//    }


