package com.aco.pmu.records

import android.app.Application
import android.os.AsyncTask
import android.widget.CalendarView
import androidx.lifecycle.LiveData
import com.aco.pmu.appDatabase.AppDatabase
import com.aco.pmu.appDatabase.AppDatabaseDao
import com.aco.pmu.appDatabase.RecordsEntity
import io.reactivex.annotations.Nullable
import org.jetbrains.annotations.NotNull
import java.lang.Exception
import java.time.MonthDay
import java.util.*

class RecordsRepository(application: Application) {

    private var appDatabaseDao: AppDatabaseDao

    private var allRecords: LiveData<List<RecordsEntity>>

    private var recordsCount: LiveData<Int>

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        appDatabaseDao = appDatabase.dataBaseDao()
        allRecords = appDatabaseDao.getAllRecords()
        recordsCount = appDatabaseDao.getRecordsCount()
    }

    fun insert(recordsEntity: RecordsEntity) {
        val insertRecordAsyncTask = InsertRecordAsyncTask(
            appDatabaseDao
        ).execute(recordsEntity)
    }

    fun update(recordsEntity: RecordsEntity) {
        val updateRecordAsyncTask = UpdateRecordAsyncTask(
            appDatabaseDao
        ).execute(recordsEntity)
    }


    fun delete(recordsEntity: RecordsEntity) {
        val deleteRecordAsyncTask = DeleteRecordAsyncTask(
            appDatabaseDao
        ).execute(recordsEntity)
    }

    fun getAllRecords(): LiveData<List<RecordsEntity>> {
        return allRecords
    }

    fun getRecordsCount() : LiveData<Int> {
        return recordsCount
    }

    fun getRecordsAtSelectedDate(dayStart: Long, dayEnd: Long): LiveData<List<RecordsEntity>> {
        return appDatabaseDao.getRecordsAtSelectedDate(dayStart, dayEnd)
    }

    fun getAllRecordsOfCurrentClient(phoneNumber1:String): LiveData<List<RecordsEntity>> {
        return appDatabaseDao.getAllRecordsOfCurrentClient(phoneNumber1)
    }

    fun getRecordDates(): List<Long> {
        return appDatabaseDao.getRecordDates()
    }

    companion object {
        private class InsertRecordAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<RecordsEntity, Unit, Unit>() {
            val recordDao = appDatabaseDao

            override fun doInBackground(vararg p0: RecordsEntity?) {
                recordDao.insert(p0[0]!!)
            }
        }

        private class UpdateRecordAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<RecordsEntity, Unit, Unit>() {
            val recordDao = appDatabaseDao

            override fun doInBackground(vararg p0: RecordsEntity?) {
                recordDao.update(p0[0]!!)
            }
        }

        private class DeleteRecordAsyncTask(appDatabaseDao: AppDatabaseDao) : AsyncTask<RecordsEntity, Unit, Unit>() {
            val recordDao = appDatabaseDao

            override fun doInBackground(vararg p0: RecordsEntity?) {
                recordDao.delete(p0[0]!!)
            }
        }
    }
}