package com.aco.pmu.records

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aco.pmu.appDatabase.RecordsEntity
import java.util.*


class RecordsViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: RecordsRepository = RecordsRepository(application)
    private var allRecords: LiveData<List<RecordsEntity>> = repository.getAllRecords()
    private var recordsCount: LiveData<Int> = repository.getRecordsCount()

    fun insert(recordsEntity: RecordsEntity) {
        repository.insert(recordsEntity)
    }

    fun update(recordsEntity: RecordsEntity) {
        repository.update(recordsEntity)
    }

    fun delete(recordsEntity: RecordsEntity) {
        repository.delete(recordsEntity)
    }

    fun getAllRecords(): LiveData<List<RecordsEntity>> {
        return allRecords
    }

    fun getRecordsCount(): LiveData<Int> {
        return recordsCount
    }

    fun getRecordsASelectedtDate(dayStart: Long, dayEnd: Long): LiveData<List<RecordsEntity>> {
        return repository.getRecordsAtSelectedDate(dayStart, dayEnd)
    }

    fun getAllRecordsOfCurrentClient(phoneNumber1:String): LiveData<List<RecordsEntity>> {
        return repository.getAllRecordsOfCurrentClient(phoneNumber1)
    }

    fun getRecordDates(): List<Long> {
        return repository.getRecordDates()
    }

    fun getPhotosPath(): List<String> {
        return repository.getPhotosPath()
    }

}
