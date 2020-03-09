package com.aco.pmu.clients

import android.app.Application
import android.app.Instrumentation
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.RoomDatabase
import com.aco.pmu.appDatabase.AppDatabase
import com.aco.pmu.appDatabase.AppDatabaseDao
import com.aco.pmu.appDatabase.AppDatabaseDao_Impl
import com.aco.pmu.appDatabase.ClientsEntity

class ClientsRepository(application: Application) {

    private var clientsDAOApp: AppDatabaseDao

    private var allClients: LiveData<List<ClientsEntity>>

    private var clientsCount: LiveData<Int>

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        clientsDAOApp = appDatabase.dataBaseDao()
        allClients = clientsDAOApp.getAllClients()
        clientsCount = clientsDAOApp.getClientsCount()
    }

    fun getClientsCount() : LiveData<Int> {
        return clientsCount
    }

    fun insert(clientsEntity: ClientsEntity) {
        val insertNoteAsyncTask = InsertNoteAsyncTask(
            clientsDAOApp
        ).execute(clientsEntity)
    }

    fun update(clientsEntity: ClientsEntity) {
        val updateNoteAsyncTask = UpdateNoteAsyncTask(
            clientsDAOApp
        ).execute(clientsEntity)
    }


    fun delete(clientsEntity: ClientsEntity) {
        val deleteNoteAsyncTask = DeleteNoteAsyncTask(
            clientsDAOApp
        ).execute(clientsEntity)
    }

    fun getAllClients(): LiveData<List<ClientsEntity>> {
        return allClients
    }

    companion object {
        private class InsertNoteAsyncTask(clientsDAOApp: AppDatabaseDao) : AsyncTask<ClientsEntity, Unit, Unit>() {
            val clientDao = clientsDAOApp

            override fun doInBackground(vararg p0: ClientsEntity?) {
                clientDao.insert(p0[0]!!)
            }
        }

        private class UpdateNoteAsyncTask(clientsDAOApp: AppDatabaseDao) : AsyncTask<ClientsEntity, Unit, Unit>() {
            val clientDao = clientsDAOApp

            override fun doInBackground(vararg p0: ClientsEntity?) {
                clientDao.update(p0[0]!!)
            }
        }

        private class DeleteNoteAsyncTask(clientsDAOApp: AppDatabaseDao) : AsyncTask<ClientsEntity, Unit, Unit>() {
            val clientDao = clientsDAOApp

            override fun doInBackground(vararg p0: ClientsEntity?) {
                clientDao.delete(p0[0]!!)
            }
        }
    }
}