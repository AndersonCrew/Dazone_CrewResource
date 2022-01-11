package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "NOTIFICATION")
class Notification(
    @SerializedName("NotificationNo")
    @ColumnInfo(name = "notificationNo")
    var notificationNo: Int? = null,

    @SerializedName("ResourceNo")
    @ColumnInfo(name = "resourceNo")
    var resourceNo: Int? = null,

    @SerializedName("NotificationType")
    @ColumnInfo(name = "notificationType")
    var notificationType: String? = null,

    @SerializedName("AlarmTime")
    @ColumnInfo(name = "alarmTime")
    var alarmTime: Int? = null
): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}