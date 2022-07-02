package com.kunpark.resource.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.model.*

object ConverterConditionSearch {
    @TypeConverter
    @JvmStatic
    fun restoreListConditionSearch(listOfString: String): List<ConditionSearch>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<ConditionSearch>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveListConditionSearch(listOfString: List<ConditionSearch>?): String {
        if (listOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(listOfString)
    }
}

object ConverterConditionCalendar {
    @TypeConverter
    @JvmStatic
    fun restoreListConditionCalendar(listOfString: String): List<CalendarDto>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<CalendarDto>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveListConditionCalendar(listOfString: List<CalendarDto>?): String {
        if (listOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(listOfString)
    }
}

object ConverterConditionResourceList {
    @TypeConverter
    @JvmStatic
    fun restoreListConditionResourceList(listOfString: String): List<CalendarAgenda>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<CalendarDto>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveListConditionResourceList(agendaOfString: List<CalendarAgenda>?): String {
        if (agendaOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(agendaOfString)
    }
}

object ConverterConditionResource {
    @TypeConverter
    @JvmStatic
    fun restoreListConditionResource(listOfString: String): List<Resource>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<Resource>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveListConditionResource(listOfString: List<Resource>?): String {
        if (listOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(listOfString)
    }
}

object ConverterDeviceNotification {
    @TypeConverter
    @JvmStatic
    fun restoreListDeviceNotification(listOfString: String): List<DevicesNotification>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<DevicesNotification>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveListDeviceNotification(listOfString: List<DevicesNotification>?): String {
        if (listOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(listOfString)
    }
}

object ConverterTimeNotification {
    @TypeConverter
    @JvmStatic
    fun restoreListTimeNotification(listOfString: String): List<TimesNotification>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<TimesNotification>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveListTimeNotification(listOfString: List<TimesNotification>?): String {
        if (listOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(listOfString)
    }
}

object ConverterOrganization {
    @TypeConverter
    @JvmStatic
    fun restoreListOrganization(listOfString: String): ArrayList<Organization>? {
        return Gson().fromJson(listOfString, object : TypeToken<ArrayList<Organization>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveListOrganization(listOfString: ArrayList<Organization>?): String {
        if (listOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(listOfString)
    }
}

object ConverterCondition {
    @TypeConverter
    @JvmStatic
    fun restoreCondition(listOfString: String): ConditionSearch? {
        return Gson().fromJson(listOfString, ConditionSearch::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun saveCondition(listOfString: ConditionSearch?): String {
        if (listOfString == null)
            return ""
        return Gson().toJson(listOfString)
    }
}


object ConverterMembers {
    @TypeConverter
    @JvmStatic
    fun restoreMembers(listOfString: String): ArrayList<User>? {
        return Gson().fromJson(listOfString, object : TypeToken<ArrayList<User>>() {
        }.type)
    }

    @TypeConverter
    @JvmStatic
    fun saveMembers(listOfString: ArrayList<User>?): String {
        if (listOfString.isNullOrEmpty())
            return ""
        return Gson().toJson(listOfString)
    }
}
