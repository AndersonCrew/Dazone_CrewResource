package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "DEVICE_NOTIFICATION")
class DevicesNotification(
    @ColumnInfo(name = "notificationTypeName")
    @SerializedName("NotificationTypeName")
    val notificationTypeName : String,

    @ColumnInfo(name = "notificationTypeNo")
    @SerializedName("NotificationTypeNo")
    val notificationTypeNo: String) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?= null
}