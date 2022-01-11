package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "PARTICIPANT")
class Participant(
    @SerializedName("Type")
    @ColumnInfo(name = "type")
    var type: String? = null,

    @SerializedName("UserID")
    @ColumnInfo(name = "userID")
    var userID: String? = null,

    @SerializedName("Name")
    @ColumnInfo(name = "Name")
    var name: String? = null,

    @SerializedName("No")
    @ColumnInfo(name = "no")
    var no: Int? = null


): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}