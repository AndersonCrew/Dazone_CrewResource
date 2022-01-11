package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity(tableName = "RESOURCE")
class Resource: Serializable {
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int? = null

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String?= null

    @ColumnInfo(name = "startStr")
    @SerializedName("startStr")
    var startStr: String?= null

    @ColumnInfo(name = "endStr")
    @SerializedName("endStr")
    var endStr: String?= null

    @ColumnInfo(name = "allDay")
    @SerializedName("allDay")
    var allDay: Boolean?= null

    @ColumnInfo(name = "backgroundColor")
    @SerializedName("backgroundColor")
    var backgroundColor: String?= null

    @ColumnInfo(name = "description")
    @SerializedName("description")
    var description: String?= null

    @ColumnInfo(name = "starttime")
    @SerializedName("starttime")
    var startTime: String?= null

    @ColumnInfo(name = "endtime")
    @SerializedName("endtime")
    var endTime: String?= null

    @ColumnInfo(name = "rsvnUserName")
    @SerializedName("RsvnUserName")
    var rsvnUserName: String?= null

    @ColumnInfo(name = "content")
    @SerializedName("Content")
    var content: String?= null

    @ColumnInfo(name = "resourceName")
    @SerializedName("ResourceName")
    var resourceName: String?= null

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idKey")
    var idKey: Int? = null
}