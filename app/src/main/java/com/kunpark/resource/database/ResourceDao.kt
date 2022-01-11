package com.kunpark.resource.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kunpark.resource.model.CalendarAgenda
import com.kunpark.resource.model.CalendarDay
import com.kunpark.resource.model.CalendarMonth

@Dao
interface ResourceDao {
    @Query("SELECT * FROM RESOURCE_LIST WHERE monthList = :monthList")
    fun getDataAgenda(monthList: String): LiveData<CalendarAgenda>

    @Query("SELECT * FROM CALENDAR_MONTH WHERE monthList = :monthList")
    fun getDataMonth(monthList: String): LiveData<CalendarMonth>

    @Query("SELECT * FROM RESOURCE_DAY WHERE dayList = :day")
    fun getDataDay(day: String): LiveData<CalendarDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCalendarAgenda(listCalendar: CalendarAgenda)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCalendarMonth(listCalendar: CalendarMonth)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCalendarDay(listCalendar: CalendarDay)
}