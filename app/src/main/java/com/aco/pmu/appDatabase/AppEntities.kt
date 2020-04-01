package com.aco.pmu.appDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "clients_table")
data class ClientsEntity(

    var firstName: String,

    var lastName: String,

    var phoneNumber1: String,

    var phoneNumber2: String,

    var clientRemark: String
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(tableName = "procedures_table")
data class ProceduresEntity(

    var procedureName: String

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(tableName = "pigments_table")
data class PigmentsEntity(

    var pigmentName: String

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(tableName = "records_table")
data class RecordsEntity(

    @TypeConverters(Converters::class)
    var date: Long? = null,

    var time: String,

    var firstAndlastNames: String,

    var phoneNumber1: String,

    var phoneNumber2: String,

    var procedure: String,

    var pigmentName1: String,

    var pigmentName2: String,

    var pigmentName3: String,

    var pigmentQuantity1: Int,

    var pigmentQuantity2: Int,

    var pigmentQuantity3: Int,

    var price: String,

    var comments: String,

    var status: Boolean,

    var photoLength: String? = null,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var photos: ByteArray? = null

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordsEntity

        if (photos != null) {
            if (other.photos == null) return false
            if (!photos!!.contentEquals(other.photos!!)) return false
        } else if (other.photos != null) return false

        return true
    }

    override fun hashCode(): Int {
        return photos?.contentHashCode() ?: 0
    }


}
