package com.aco.pmu.appDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDatabaseDao {

    @Insert
    fun insert(clientsEntity: ClientsEntity)

    @Update
    fun update(clientsEntity: ClientsEntity)

    @Delete
    fun delete(clientsEntity: ClientsEntity)

    @Query("SELECT * FROM clients_table ORDER BY firstName ASC")
    fun getAllClients(): LiveData<List<ClientsEntity>>

    @Query("SELECT COUNT(*) FROM clients_table")
    fun getClientsCount(): LiveData<Int>




    @Insert
    fun insert(proceduresEntity: ProceduresEntity)

    @Update
    fun update(proceduresEntity: ProceduresEntity)

    @Delete
    fun delete(proceduresEntity: ProceduresEntity)

    @Query("SELECT * FROM procedures_table ORDER BY procedureName ASC")
    fun getAllProcedures(): LiveData<List<ProceduresEntity>>



    @Insert
    fun insert(pigmentsEntity: PigmentsEntity)

    @Update
    fun update(pigmentsEntity: PigmentsEntity)

    @Delete
    fun delete(pigmentsEntity: PigmentsEntity)

    @Query("SELECT * FROM pigments_table ORDER BY pigmentName ASC")
    fun getAllPigments(): LiveData<List<PigmentsEntity>>



    @Insert
    fun insert(recordsEntity: RecordsEntity)

    @Update
    fun update(recordsEntity: RecordsEntity)

    @Delete
    fun delete(recordsEntity: RecordsEntity)

    @Query("SELECT * FROM records_table ORDER BY time ASC")
    fun getAllRecords(): LiveData<List<RecordsEntity>>

    @Query("SELECT COUNT(*) FROM records_table")
    fun getRecordsCount(): LiveData<Int>

    @Query("SELECT * FROM records_table WHERE date BETWEEN :dayStart AND :dayEnd ORDER BY time ASC")
    fun getRecordsAtSelectedDate(dayStart: Long, dayEnd: Long): LiveData<List<RecordsEntity>>

    @Query("SELECT * FROM records_table WHERE phoneNumber1 = :phoneNumber1 ORDER BY date DESC")
    fun getAllRecordsOfCurrentClient(phoneNumber1: String): LiveData<List<RecordsEntity>>

    @Query("SELECT date FROM records_table")
    fun getRecordDates(): List<Long>

    @Query("SELECT photosPaths FROM records_table")
    fun getPhotosPath(): List<String>
}
