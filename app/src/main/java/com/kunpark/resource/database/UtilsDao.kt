package com.kunpark.resource.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kunpark.resource.model.*
import java.sql.Time

@Dao
interface UtilsDao {
    @Query("SELECT * FROM DEVICE_NOTIFICATION")
    fun getDeviceNotificationSetting(): LiveData<List<DevicesNotification>>

    @Query("SELECT * FROM TIME_NOTIFICATION")
    fun getTimeNotificationSetting(): LiveData<List<TimesNotification>>

    @Query("SELECT * FROM ORGANIZATION")
    fun getOrganizations(): LiveData<List<Organization>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMoreDataDeviceNotifications(conditionSearch: List<DevicesNotification>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMoreTimeNotifications(conditionSearch: List<TimesNotification>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMoreOrganizations (conditionSearch: List<Organization>)
}