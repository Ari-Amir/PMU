package com.aco.pmu.googleDrive1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.aco.pmu.R
import com.aco.pmu.googleDrive1.DriveServiceHelper1.Companion.getGoogleDriveService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_google_drive.*
import java.io.File

class GoogleDrive1 : RunTimePermission1() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mDriveServiceHelper: DriveServiceHelper1? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_drive)


        btnBackup.setOnClickListener {
            mDriveServiceHelper!!.deleteGoogleFile("123456.txt")
                .addOnSuccessListener(OnSuccessListener<Any?> { _ ->
                    mDriveServiceHelper!!.uploadFileToGoogle(getFile(),"text/plain")
                        ?.addOnSuccessListener(OnSuccessListener<Any?> { googleDriveFileHolder ->
                            Toast.makeText(this, "Backup upload successfully", Toast.LENGTH_LONG).show()
                        })
                        ?.addOnFailureListener(OnFailureListener { _ ->
                            Toast.makeText(this, "Backup upload error", Toast.LENGTH_LONG).show()
                        })
                })
                .addOnFailureListener(OnFailureListener { _ ->
                    Toast.makeText(this, "Backup upload error", Toast.LENGTH_LONG).show()
                })


        }

        btnRestore.setOnClickListener {
            mDriveServiceHelper!!.getGoogleFileId("123456.txt")
                .addOnSuccessListener(OnSuccessListener<Any?> { _ ->
                    Toast.makeText(this, "Got fileId successfully", Toast.LENGTH_LONG).show()

                    mDriveServiceHelper!!.downloadGoogleFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                "123.txt"
                            ), mDriveServiceHelper?.fileId
                        )
                        ?.addOnSuccessListener(OnSuccessListener<Unit> { _ ->
                            Toast.makeText(this, "Restore successfully", Toast.LENGTH_LONG).show()
                        })
                        ?.addOnFailureListener(OnFailureListener { _ ->
                            Toast.makeText(this, "Restore error", Toast.LENGTH_LONG).show()
                        })


                })
                .addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(this, "Got fileId error", Toast.LENGTH_LONG).show()

                })
        }

    }

    private fun getFile() : File {
        val databaseDirectory = Environment.getExternalStorageDirectory()
        val databaseFileName1 = "//Download//123456.txt"
        val databaseFile1 = File(databaseDirectory, databaseFileName1)
        return databaseFile1
    }

    override fun onStart() {
        super.onStart()
        val account =
            GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account == null) {
            signIn()
        } else {
            email!!.text = account.email
            mDriveServiceHelper = DriveServiceHelper1(
                getGoogleDriveService(
                    applicationContext,
                    account,
                    "appName"
                )
            )
        }
    }

    private fun signIn() {
        mGoogleSignInClient = buildGoogleSignInClient()
        startActivityForResult(
            mGoogleSignInClient!!.signInIntent,
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
                email!!.text = googleSignInAccount.email
                mDriveServiceHelper = DriveServiceHelper1(
                    getGoogleDriveService(
                        applicationContext,
                        googleSignInAccount,
                        "PMU"
                    )
                )
                Log.d(
                    TAG,
                    "handleSignInResult: $mDriveServiceHelper"
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
        private const val TAG = "GoogleDrive1"
    }
}

//createFolder!!.setOnClickListener(View.OnClickListener {
//            if (mDriveServiceHelper == null) {
//                return@OnClickListener
//            }
//            // you can provide  folder id in case you want to save this file inside some folder.
//            // if folder id is null, it will save file to the root
//            mDriveServiceHelper!!.createFolder("testDummyss", null)
//                .addOnSuccessListener(OnSuccessListener<Any?> { googleDriveFileHolder ->
//                    val gson = Gson()
//                    Log.d(
//                        TAG,
//                        "onSuccess: " + gson.toJson(googleDriveFileHolder)
//                    )
//                })
//                .addOnFailureListener(OnFailureListener { e ->
//                    Log.d(
//                        TAG,
//                        "onFailure: " + e.message
//                    )
//                })
//        })