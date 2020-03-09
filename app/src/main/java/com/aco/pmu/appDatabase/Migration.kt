package com.aco.pmu.appDatabase

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration {
    companion object {
        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `records_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER, `time` TEXT NOT NULL, `firstAndlastNames` TEXT NOT NULL, `phoneNumber1` TEXT NOT NULL, `phoneNumber2` TEXT NOT NULL, `procedure` TEXT NOT NULL, `pigmentName1` TEXT NOT NULL, `pigmentName2` TEXT NOT NULL, `pigmentName3` TEXT NOT NULL, `pigmentQuantity1` INTEGER NOT NULL, `pigmentQuantity2` INTEGER NOT NULL, `pigmentQuantity3` INTEGER NOT NULL, `price` INTEGER, `comments` TEXT NOT NULL, `status` INTEGER NOT NULL)")
            }
        }
    }
}
