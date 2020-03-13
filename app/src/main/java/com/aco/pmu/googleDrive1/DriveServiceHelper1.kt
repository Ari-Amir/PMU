package com.aco.pmu.googleDrive1

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import junit.runner.Version.id
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.BreakIterator
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class DriveServiceHelper1(private val mDriveService: Drive) {
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    var fileId = ""

    companion object {
        fun getGoogleDriveService(
            context: Context?,
            account: GoogleSignInAccount,
            appName: String?
        ): Drive {
            val credential = GoogleAccountCredential.usingOAuth2(
                context, setOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = account.account
            return Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    GsonFactory(),
                    credential
                )
                .setApplicationName("PMU")
                .build()
        }
    }

    fun uploadFileToGoogle(
        localFile: java.io.File,
        mimeType: String?
    ): Task<Unit?>? {
        return Tasks.call(
            mExecutor,
            Callable { // Retrieve the metadata as a File object.
                val metadata =
                    File()
                        .setParents(listOf("root"))
                        .setMimeType(mimeType)
                        .setName(localFile.name)
                val fileContent = FileContent(mimeType, localFile)
                val fileMeta =
                    mDriveService.files().create(metadata, fileContent).execute()
            })
    }

    fun downloadGoogleFile(
        fileSaveLocation: java.io.File?,
        fileId: String?
    ): Task<Unit>? {
        return Tasks.call(
            mExecutor,
            Callable {
                val outputStream: OutputStream =
                    FileOutputStream(fileSaveLocation)
                mDriveService.files()[fileId].executeMediaAndDownloadTo(outputStream)
            })
    }


    fun getGoogleFileId(name: String): Task<Unit?> {
        return Tasks.call(
            mExecutor,
            Callable {
               val fileName = mDriveService.files().list()
                   .setQ("name = '$name'")
                   .execute()
                fileId = fileName.files[0].id

            })
    }

    fun deleteGoogleFile(name: String): Task<Unit?> {
        return Tasks.call(
            mExecutor,
            Callable {
                val fileName = mDriveService.files().list()
                    .setQ("name = '$name'")
                    .execute()
                if (fileName.files.size != 0) {
                    val fileIdToDelete = fileName.files[0].id
                    val delete = mDriveService.files().delete(fileIdToDelete).execute()
                }
            })

    }

    }



//    fun createFolder(
//        folderName: String?,
//        folderId: String?
//    ): Task<GoogleDriveFileHolder1> {
//        return Tasks.call(
//            mExecutor,
//            Callable {
//                val GoogleDriveFileHolder1 = GoogleDriveFileHolder1()
//                val root: List<String>
//                root = folderId?.let { listOf(it) } ?: listOf("root")
//                val metadata =
//                    File()
//                        .setParents(root)
//                        .setMimeType("application/vnd.google-apps.folder")
//                        .setName(folderName)
//                val googleFile =
//                    mDriveService.files().create(metadata).execute()
//                        ?: throw IOException("Null result when requesting file creation.")
//                GoogleDriveFileHolder1.id = googleFile.id
//                GoogleDriveFileHolder1
//            })
//    }
