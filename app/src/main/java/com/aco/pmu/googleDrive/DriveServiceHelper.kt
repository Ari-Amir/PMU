package com.aco.pmu.googleDrive

import android.content.Context
import android.os.Environment
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
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class DriveServiceHelper(private val mDriveService: Drive) {
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    var fileId = ""
    var folderId = ""

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

    fun createFolder() {
        val folderMetadata = File()
        folderMetadata.name = "PMU"
        folderMetadata.mimeType = "application/vnd.google-apps.folder"
        val folder: File =
            mDriveService.files().create(folderMetadata)
                .setFields("id")
                .execute()
        folderId = folder.id
    }

    fun uploadFileToGoogle(
        localFile: java.io.File,
        mimeType: String?
    ): Task<File>? {
        return Tasks.call(
            mExecutor,
            Callable {
                val metadata =
                    File()
                        .setParents(listOf(folderId))
                        .setMimeType(mimeType)
                        .setName(localFile.name)
                val fileContent = FileContent(mimeType, localFile)
                mDriveService.files().create(metadata, fileContent).execute()
            })
    }

    fun downloadGoogleFile(fileSaveLocation: java.io.File?, fileId: String?): Task<Unit>? {
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
                val fileList = mDriveService.files().list()
                    .setQ("name = '$name'")
                    .execute()
                fileId = fileList.files[0].id
            }
        )
    }

    fun checkIsGoogleFolderExists(folderName: String): Task<Unit?> {
        return Tasks.call(
            mExecutor,
            Callable {
                val folderList = mDriveService.files().list()
                    .setQ("name = '$folderName'")
                    .execute()
                if (folderList.files.isEmpty()) {               //check if PMU folder is exists
                    createFolder()                               // if Not exist6 create folder
                } else {
                    folderId = folderList.files[0].id           //if exist, get folders Id
                }
            }
        )
    }

    fun deleteGoogleFile(name: String): Task<Unit?> {
        return Tasks.call(
            mExecutor,
            Callable {
                val fileList = mDriveService.files().list()
                    .setQ("name = '$name'")
                    .execute()
                if (fileList.files.size != 0) {
                    val fileIdToDelete = fileList.files[0].id
                    mDriveService.files().delete(fileIdToDelete).execute()
                }
            })

    }

    fun getDatabaseFiles(): MutableList<java.io.File> {
        val databaseFileDirectory = Environment.getDataDirectory()
        val databaseFileName1 = "//data//com.aco.pmu//databases//PMUs_database"
        val databaseFileName2 = "data//com.aco.pmu//databases//PMUs_database-shm"
        val databaseFileName3 = "data//com.aco.pmu//databases//PMUs_database-wal"
        val databaseFile1 = java.io.File(databaseFileDirectory, databaseFileName1)
        val databaseFile2 = java.io.File(databaseFileDirectory, databaseFileName2)
        val databaseFile3 = java.io.File(databaseFileDirectory, databaseFileName3)
        val databaseFileList = mutableListOf<java.io.File>()
        databaseFileList.addAll(listOf(databaseFile1, databaseFile2, databaseFile3))
        return databaseFileList
    }

    fun getDatabaseFileNames(): MutableList<String> {
        val databaseFileName1 = "//data//com.aco.pmu//databases//PMUs_database"
        val databaseFileName2 = "data//com.aco.pmu//databases//PMUs_database-shm"
        val databaseFileName3 = "data//com.aco.pmu//databases//PMUs_database-wal"
        val databaseFileNamesList = mutableListOf<String>()
        databaseFileNamesList.addAll(
            listOf(
                databaseFileName1,
                databaseFileName2,
                databaseFileName3
            )
        )
        return databaseFileNamesList
    }

    fun getGoogleFileNames(): MutableList<String> {
        val googleFileName1 = "PMUs_database"
        val googleFileName2 = "PMUs_database-shm"
        val googleFileName3 = "PMUs_database-wal"
        val googleFileNamesList = mutableListOf<String>()
        googleFileNamesList.addAll(listOf(googleFileName1, googleFileName2, googleFileName3))
        return googleFileNamesList
    }

}

