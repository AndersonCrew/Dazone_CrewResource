package com.kunpark.resource.model

import android.annotation.SuppressLint
import com.kunpark.resource.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

enum class CalendarType(val type: Int, val strFormatDate: String) {
    AGENDA(0, Constants.MM_YYYY), DAY(1, Constants.DD_MM_YYYY), WEEK(2, Constants.MM_YYYY), MONTH(3, Constants.MM_YYYY), UNKNOW(-1, "");
    companion object {
        fun getType(mType: CalendarType) : Int? {
            return values().find { cType -> cType.type == mType.type }?.type
        }

        @SuppressLint("SimpleDateFormat")
        fun getStrDate(cType: CalendarType, cal: Calendar): String {
            return SimpleDateFormat(cType.strFormatDate).format(cal.time)
        }
    }
}

enum class AgendaItemType {
    ITEM_RESOURCE, ITEM_CALENDAR;
}