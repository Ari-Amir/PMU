package com.aco.pmu.procedures

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.aco.pmu.appDatabase.AppDatabase
import com.aco.pmu.appDatabase.AppDatabaseDao
import com.aco.pmu.appDatabase.ProceduresEntity

class ProceduresRepository(application: Application) {

    private var appDatabaseDao: AppDatabaseDao

    private var allProcedures: LiveData<List<ProceduresEntity>>

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        appDatabaseDao = appDatabase.dataBaseDao()
        allProcedures = appDatabaseDao.getAllProcedures()
    }

    fun insert(proceduresEntity: ProceduresEntity) {
        val insertProcedureAsyncTask = InsertProcedureAsyncTask(
            appDatabaseDao
        ).execute(proceduresEntity)
    }

    fun update(proceduresEntity: ProceduresEntity) {
        val updateProcedureAsyncTask = UpdateProcedureAsyncTask(
            appDatabaseDao
        ).execute(proceduresEntity)
    }


    fun delete(proceduresEntity: ProceduresEntity) {
        val deleteProcedureAsyncTask = DeleteProcedureAsyncTask(
            appDatabaseDao
        ).execute(proceduresEntity)
    }

    fun getAllProcedures(): LiveData<List<ProceduresEntity>> {
        return allProcedures
    }

    companion object {
        private class InsertProcedureAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<ProceduresEntity, Unit, Unit>() {
            val procedureDao = appDatabaseDao

            override fun doInBackground(vararg p0: ProceduresEntity?) {
                procedureDao.insert(p0[0]!!)
            }
        }

        private class UpdateProcedureAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<ProceduresEntity, Unit, Unit>() {
            val procedureDao = appDatabaseDao

            override fun doInBackground(vararg p0: ProceduresEntity?) {
                procedureDao.update(p0[0]!!)
            }
        }

        private class DeleteProcedureAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<ProceduresEntity, Unit, Unit>() {
            val procedureDao = appDatabaseDao

            override fun doInBackground(vararg p0: ProceduresEntity?) {
                procedureDao.delete(p0[0]!!)
            }
        }
    }
}