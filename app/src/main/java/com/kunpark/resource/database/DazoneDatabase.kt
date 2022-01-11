package com.kunpark.resource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kunpark.resource.model.*
import com.kunpark.resource.utils.Config

@Database(
    entities = [User::class, ConditionSearch::class, CalendarDto::class, CalendarAgenda::class,
        Resource::class, DevicesNotification::class, TimesNotification::class,
        Organization::class, CalendarMonth::class, CalendarDay::class, Participant::class, Notification::class],
    version = Config.VERSION_DB,
    exportSchema = false
)

@TypeConverters(
    value = [ConverterConditionSearch::class, ConverterConditionResource::class, ConverterConditionResourceList::class,
        ConverterConditionCalendar::class, ConverterDeviceNotification::class, ConverterTimeNotification::class, ConverterOrganization::class, ConverterCondition::class]
)

abstract class DazoneDatabase : RoomDatabase() {
    abstract fun getUtilsDao(): UtilsDao
    abstract fun getUserDao(): UserDao
    abstract fun getConditionSearchDao(): ConditionSearchDao
    abstract fun getResourceDao(): ResourceDao

    companion object {
        @Volatile
        private var INSTANSE: DazoneDatabase? = null

        fun getDatabase(context: Context): DazoneDatabase {
            val temInstanse = INSTANSE
            if (temInstanse != null) {
                return temInstanse
            }

            synchronized(this) {
                val instanse = Room.databaseBuilder(
                    context.applicationContext,
                    DazoneDatabase::class.java,
                    "DazoneDatabase"
                ).build()
                INSTANSE = instanse
                return instanse
            }
        }
    }


}
