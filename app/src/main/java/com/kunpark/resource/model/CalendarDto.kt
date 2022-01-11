package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "CALENDAR_DTO")
class CalendarDto(
    @ColumnInfo(name = "day")
    var day: Int? = null,

    @ColumnInfo(name = "month")
    var month: Int?= null,

    @ColumnInfo(name = "year")
    var year: Int?= null,

    @ColumnInfo(name = "timeString")
    var timeString: String?= null
) {
    @ColumnInfo(name = "listResource")
    var listResource: ArrayList<Resource> = arrayListOf()

    @ColumnInfo(name = "isResource")
    var isResource: Boolean = true

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

}