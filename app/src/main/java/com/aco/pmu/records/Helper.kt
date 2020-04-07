package com.aco.pmu.records


import android.app.Application
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

    private val photosFolder = "/data/data/com.aco.pmu/databases/Photos"


    fun getImagesPaths (listOfUncompressedImages: MutableList<Image>): String {
        //get paths of images which are in recycler view from (listOfUncompressedImages)
        // put them into list (pathsList)
        // convert this list to String (pathsString)
        val pathsList = mutableListOf<String>()
        var pathsString = ""

        for (i in listOfUncompressedImages) {
            val folder = photosFolder
            val imagePath = "$folder/${i.name}"

            pathsList.add(imagePath)
            pathsString = pathsList.joinToString()
        }

        //get list of files names (foldersFileNameList) from //data//com.aco.pmu//databases//Photo
         val foldersFileNameList = mutableListOf<String>()
         val foldersFileList = File(photosFolder).listFiles()

         if (foldersFileList != null) {
             for (i in foldersFileList) {
                 foldersFileNameList.add(i.name)
             }
         }
        
        //if files to write are NOT exist in //data//com.aco.pmu//databases//Photo, add those files to list (filesToWriteList), else -> do nothing
        val filesToWriteList = mutableListOf<Image>()
            for (i in listOfUncompressedImages) {
                val file = File(photosFolder, i.name)
                if (!file.exists()) {
                    filesToWriteList.add(i)
                }
            }

        //write files to //data//com.aco.pmu//databases//Photos
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
        return pathsString.replace(" ", "")
    }

    fun getImagesFromPath(path: String) : List<Image> {
        val list = mutableListOf<Image>()
        val stringToList = path.split(',')

        val databaseFileList = mutableListOf<String>()
        val filesPath = File("//data/data//com.aco.pmu//databases//Photos")
        val listFiles = filesPath.listFiles()
        if (listFiles != null) {
            for (i in listFiles) {
                databaseFileList.add(i.toString())
            }
        }

        val stringTolistChecked = stringToList.intersect(databaseFileList)

        if (stringTolistChecked.isNotEmpty()) {
            for (i in stringTolistChecked) {
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

class Helper2(application: Application) {
    private val repository = RecordsRepository(application)

    fun updateFiles() {
        val photosPathsFromDatabase = repository.getPhotosPath()
        val photosPathsFromDatabaseInString = photosPathsFromDatabase.joinToString()
        val photosPathsFromDatabaseInList = mutableListOf<String>()
        for (i in photosPathsFromDatabaseInString.split(",")) {
            photosPathsFromDatabaseInList.add(i.trim())
        }
        val allPhotosPaths = photosPathsFromDatabaseInList.distinct()


        //get list of files full path (foldersFileNameList) from //data//com.aco.pmu//databases//Photo
        val foldersFileFullPathList = mutableListOf<String>()
        val foldersFileList = File("//data/data//com.aco.pmu//databases//Photos").listFiles()

        if (foldersFileList != null) {
            for (i in foldersFileList) {
                foldersFileFullPathList.add(i.path)
            }
        }

        val filesToDelete = foldersFileFullPathList.filter { !allPhotosPaths.contains(it) }

        for (i in filesToDelete) {
            File(i).delete()
        }
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


