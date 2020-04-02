package com.aco.pmu.appDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ClientsEntity::class, ProceduresEntity::class, PigmentsEntity::class, RecordsEntity::class], version = 9, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dataBaseDao (): AppDatabaseDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? =
                instance ?: synchronized(this) {
                    instance ?: Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "PMUs_database"
                    )
                        .addMigrations(Migration.MIGRATION_8_9)
                        .allowMainThreadQueries()
                        .build().also { instance = it }
                }
            }
        }
