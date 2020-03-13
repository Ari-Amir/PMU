package com.aco.pmu.googleDrive

import androidx.core.util.Pair
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.InputStreamContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import java.io.*
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */

class DriveServiceHelper(driveService: Drive) {
    private val mExecutor: Executor =
        Executors.newSingleThreadExecutor()
    private val mDriveService: Drive

//    fun createFolder() : String {
//        val fileMetadata =
//            File()
//        fileMetadata.name = "PMU"
//        fileMetadata.mimeType = "application/vnd.google-apps.folder"
//
//        val file: File =
//            mDriveService.files().create(fileMetadata)
//                .setFields("id")
//                .execute()
//        return file.id
//    }

    fun uploadFile(
        name: String?,
        fileTest: java.io.File?
    ): Task<String> {
        return Tasks.call(mExecutor, Callable {
            val metadata = File()
                    .setParents(listOf("root"))
                    .setMimeType("text/plain")
                    .setName(name)
            val targetStream: InputStream = FileInputStream(fileTest)
            val inputStreamContent =
                InputStreamContent("text/plain", targetStream)
            val googleFile =
                mDriveService.files().create(metadata, inputStreamContent).execute()
                    ?: throw IOException("Null result when requesting file creation.")
            googleFile.id
        })
    }

    /**
     * Opens the file identified by `fileId` and returns a [Pair] of its name and
     * contents.
     */
    fun readFile(fileId: String?): Task<Pair<String, String>> {
        return Tasks.call(mExecutor,
            Callable {

                // Retrieve the metadata as a File object.
                val fileMetadata = mDriveService.files()[fileId].execute()
                val fileName = fileMetadata.name
                mDriveService.files()[fileId].executeMediaAsInputStream().use { `is` ->
                    BufferedReader(InputStreamReader(`is`)).use { reader ->
                        val stringBuilder = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line)
                        }
                        val contents = stringBuilder.toString()
                        return@Callable Pair.create(
                            fileName,
                            contents
                        )
                    }
                }
            }
        )
    }

    /**
     * Returns a [FileList] containing all the visible files in the user's My Drive.
     *
     *
     * The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the [Google
 * Developer's Console](https://play.google.com/apps/publish) and be submitted to Google for verification.
     */
    fun queryFiles(): Task<FileList> {
        return Tasks.call(mExecutor,
            Callable {
                mDriveService.files().list().setSpaces("drive").execute()
            }
        )
    }

    init {
        mDriveService = driveService
    }
}