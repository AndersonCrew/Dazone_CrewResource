package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "TIME_NOTIFICATION")
class TimesNotification(
    @ColumnInfo(name = "alarmTimeNo")
    @SerializedName("AlarmTimeNo")
    val alarmTimeNo : Int,

    @ColumnInfo(name = "alarmTimeName")
    @SerializedName("AlarmTimeName")
    val alarmTimeName : String) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?= null
}