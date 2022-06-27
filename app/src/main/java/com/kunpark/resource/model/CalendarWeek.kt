package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RESOURCE_WEEK")
class CalendarWeek(
    @PrimaryKey
    @ColumnInfo(name = "firstDay")
    var firstDay: String = "",

    @ColumnInfo(name = "listResource")
    var list: List<CalendarDto>? = ArrayList()
)