package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RESOURCE_LIST")
class CalendarAgenda(
    @PrimaryKey
    @ColumnInfo(name = "monthList")
    var monthList: String = "",

    @ColumnInfo(name = "listResource")
    var list: List<CalendarDto>?= ArrayList()
) {
    @ColumnInfo(name = "hasData")
    var hasData: Boolean = false
}