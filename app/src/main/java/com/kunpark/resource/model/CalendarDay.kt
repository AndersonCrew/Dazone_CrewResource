package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RESOURCE_DAY")
class CalendarDay(
    @PrimaryKey
    @ColumnInfo(name = "dayList")
    var dayList: String = "",

    @ColumnInfo(name = "listResource")
    var list: List<CalendarDto>? = ArrayList()
)