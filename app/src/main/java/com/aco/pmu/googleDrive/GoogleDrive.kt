package com.aco.pmu.googleDrive

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.aco.pmu.R
import com.aco.pmu.googleDrive.DriveServiceHelper.Companion.getGoogleDriveService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_google_drive.*
import java.io.File


class GoogleDrive : RunTimePermission() {
    private var googleSignInClient: GoogleSignInClient? = null
    private var driveServiceHelper: DriveServiceHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_drive)

        var fileNameToDelete = 0
        var fileNameToDownload = 0
        var fileNameToUpload = 0
        var fileToGetGoogleId = 0
        var successCounter = 0


        btnBackup.setOnClickListener {
            requestPermission(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) { isGranted ->
                if (isGranted) {
                    loader.startAnimation()
                    loader.setIsVisible(true)
                    btnBackup.isEnabled = false
                    btnRestore.isEnabled = false

                    driveServiceHelper?.checkIsGoogleFolderExists("PMU")

                    for (i in driveServiceHelper!!.getGoogleFileNames()) {
                        driveServiceHelper!!.deleteGoogleFile(driveServiceHelper!!.getGoogleFileNames()[fileNameToDelete])
                        fileNameToDelete += 1

                        for (i in driveServiceHelper!!.getDatabaseFiles()) {
                            driveServiceHelper!!.uploadFileToGoogle(
                                    driveServiceHelper!!.getDatabaseFiles()[fileNameToUpload],
                                    "text/plain"
                                )
                                ?.addOnSuccessListener { _ ->
                                    successCounter += 1
                                    if (successCounter == 3) {
                                        loader.stopAnimation()
                                        btnBackup.isEnabled = true
                                        btnRestore.isEnabled = true
                                        Toast.makeText(
                                            this,
                                            "Резервное копирование базы данных на Google Disk выполнено успешно",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        successCounter = 0
                                        this.finish()
                                    }
                                }
                                ?.addOnFailureListener { _ ->
                                    loader.stopAnimation()
                                    btnBackup.isEnabled = true
                                    btnRestore.isEnabled = true
                                    Toast.makeText(
                                        this,
                                        "Ошибка... попробуйте еще раз",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            fileNameToUpload += 1
                            break
                        }
                    }
                }
            }
        }


        btnRestore.setOnClickListener {
            requestPermission(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) { isGranted ->
                if (isGranted) {
                    loader.startAnimation()
                    loader.setIsVisible(true)
                    btnBackup.isEnabled = false
                    btnRestore.isEnabled = false

                    driveServiceHelper?.checkIsGoogleFolderExists("PMU")

                    driveServiceHelper!!.getGoogleFileId(driveServiceHelper!!.getGoogleFileNames()[fileToGetGoogleId])
                        .addOnSuccessListener { _ ->
                            fileToGetGoogleId += 1
                            driveServiceHelper!!.downloadGoogleFile(
                                    File(
                                        Environment.getDataDirectory(),
                                        driveServiceHelper!!.getDatabaseFileNames()[fileNameToDownload]
                                    ), driveServiceHelper!!.fileId
                                )
                                ?.addOnSuccessListener {
                                    fileNameToDownload += 1
                                    successCounter += 1


                                    driveServiceHelper!!.getGoogleFileId(driveServiceHelper!!.getGoogleFileNames()[fileToGetGoogleId])
                                        .addOnSuccessListener { _ ->
                                            fileToGetGoogleId += 1
                                            driveServiceHelper!!.downloadGoogleFile(
                                                    File(
                                                        Environment.getDataDirectory(),
                                                        driveServiceHelper!!.getDatabaseFileNames()[fileNameToDownload]
                                                    ), driveServiceHelper!!.fileId
                                                )
                                                ?.addOnSuccessListener {
                                                    fileNameToDownload += 1
                                                    successCounter += 1


                                                    driveServiceHelper!!.getGoogleFileId(
                                                            driveServiceHelper!!.getGoogleFileNames()[fileToGetGoogleId]
                                                        )
                                                        .addOnSuccessListener { _ ->
                                                            fileToGetGoogleId += 1
                                                            driveServiceHelper!!.downloadGoogleFile(
                                                                    File(
                                                                        Environment.getDataDirectory(),
                                                                        driveServiceHelper!!.getDatabaseFileNames()[fileNameToDownload]
                                                                    ), driveServiceHelper!!.fileId
                                                                )
                                                                ?.addOnSuccessListener {
                                                                    fileNameToDownload += 1
                                                                    successCounter += 1

                                                                    if (successCounter == 3) {
                                                                        loader.stopAnimation()
                                                                        btnBackup.isEnabled = true
                                                                        btnRestore.isEnabled = true
                                                                        Toast.makeText(
                                                                            this,
                                                                            "Восстановление базы данных с Google Disk выполнено успешно",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()
                                                                        successCounter = 0
                                                                        this.finish()
                                                                    }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                        .addOnFailureListener { _ ->
                            loader.stopAnimation()
                            btnBackup.isEnabled = true
                            btnRestore.isEnabled = true
                            Toast.makeText(this, "Ошибка... попробуйте еще раз", Toast.LENGTH_LONG)
                                .show()
                        }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val account =
            GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account == null) {
            signIn()
        } else {
            gmail!!.text = account.email
            gname!!.text = account.displayName
            Glide.with(applicationContext).load(account.photoUrl)
                .thumbnail(0.8f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(gphoto)

            driveServiceHelper = DriveServiceHelper(
                getGoogleDriveService(
                    applicationContext,
                    account,
                    "appName"
                )
            )
        }
    }

    private fun signIn() {
        googleSignInClient = buildGoogleSignInClient()
        startActivityForResult(
            googleSignInClient!!.signInIntent,
            REQUEST_CODE_SIGN_IN
        )
    }

    private fun buildGoogleSignInClient(): GoogleSignInClient {
        val signInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .requestEmail()
                .build()
        return GoogleSignIn.getClient(applicationContext, signInOptions)
    }

    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        resultData: Intent?
    ) {
        when (requestCode) {
            REQUEST_CODE_SIGN_IN -> if (resultCode == Activity.RESULT_OK && resultData != null) {
                handleSignInResult(resultData)
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData)
    }

    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener { googleSignInAccount ->
                Log.d(
                    TAG,
                    "Signed in as " + googleSignInAccount.email
                )
                gmail!!.text = googleSignInAccount.email
                driveServiceHelper = DriveServiceHelper(
                    getGoogleDriveService(
                        applicationContext,
                        googleSignInAccount,
                        "PMU"
                    )
                )
                Log.d(
                    TAG,
                    "handleSignInResult: $driveServiceHelper"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    TAG,
                    "Unable to sign in.",
                    e
                )
            }
    }

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 100
        private const val TAG = "GoogleDrive"
    }
}
