package com.kunpark.resource.utils

import java.util.*

object TimeUtils {
    fun getTimezoneOffsetInMinutes(): Int {
        val tz = TimeZone.getDefault()
        return tz.rawOffset / 60000
    }
}