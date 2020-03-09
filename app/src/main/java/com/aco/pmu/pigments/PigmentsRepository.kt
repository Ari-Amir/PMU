package com.aco.pmu.pigments

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.aco.pmu.appDatabase.AppDatabase
import com.aco.pmu.appDatabase.AppDatabaseDao
import com.aco.pmu.appDatabase.PigmentsEntity

class PigmentsRepository(application: Application) {

    private var appDatabaseDao: AppDatabaseDao

    private var allPigments: LiveData<List<PigmentsEntity>>

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        appDatabaseDao = appDatabase.dataBaseDao()
        allPigments = appDatabaseDao.getAllPigments()
    }

    fun insert(pigmentsEntity: PigmentsEntity) {
        val insertPigmentsAsyncTask = InsertPigmentsAsyncTask(
            appDatabaseDao
        ).execute(pigmentsEntity)
    }

    fun update(pigmentsEntity: PigmentsEntity) {
        val updatePigmentsAsyncTask = UpdatePigmentsAsyncTask(
            appDatabaseDao
        ).execute(pigmentsEntity)
    }


    fun delete(pigmentsEntity: PigmentsEntity) {
        val deletePigmentsAsyncTask = DeletePigmentsAsyncTask(
            appDatabaseDao
        ).execute(pigmentsEntity)
    }

    fun getAllPigments(): LiveData<List<PigmentsEntity>> {
        return allPigments
    }

    companion object {
        private class InsertPigmentsAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<PigmentsEntity, Unit, Unit>() {
            val pigmentsDao = appDatabaseDao

            override fun doInBackground(vararg p0: PigmentsEntity?) {
                pigmentsDao.insert(p0[0]!!)
            }
        }

        private class UpdatePigmentsAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<PigmentsEntity, Unit, Unit>() {
            val pigmentsDao = appDatabaseDao

            override fun doInBackground(vararg p0: PigmentsEntity?) {
                pigmentsDao.update(p0[0]!!)
            }
        }

        private class DeletePigmentsAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<PigmentsEntity, Unit, Unit>() {
            val pigmentsDao = appDatabaseDao

            override fun doInBackground(vararg p0: PigmentsEntity?) {
                pigmentsDao.delete(p0[0]!!)
            }
        }
    }
}