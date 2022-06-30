package com.kunpark.resource.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utils {
    fun setServerSite(domain: String): String? {
        var domain = domain
        val domains = domain.split("[.]".toRegex()).toTypedArray()
        if (domain.contains(".bizsw.co.kr") && !domain.contains("8080")) {
            domain = domain.replace(".bizsw.co.kr", ".bizsw.co.kr:8080")
        }
        if (domains.size == 1) {
            domain = domains[0] + ".crewcloud.net"
        }
        if (domain.startsWith("http://")) {
            domain = domain.replace("http://", "")
        }
        if (domain.startsWith("https://")) {
            domain = domain.replace("https://", "")
        }
        val head = if (DazoneApplication.getInstance().mPref?.getBoolean(Constants.HAS_SSL, false) == true
        ) "https://" else "http://"
        val domainCompany = head + domain
        DazoneApplication.getInstance().mPref?.setString(Constants.DOMAIN, domainCompany)
        DazoneApplication.getInstance().mPref?.setString(Constants.COMPANY_NAME, domain)
        return domainCompany
    }

    fun getPositionAgendaFromCalendar(calendar: Calendar): Int {
        val cal = Calendar.getInstance()
        val firstYear = cal.get(Calendar.YEAR) - 100
        val yearFocus = calendar.get(Calendar.YEAR)
        val monthFocus = calendar.get(Calendar.MONTH)
        return (yearFocus - firstYear ) * 12 + monthFocus
    }


    fun getStrDateFromPosition(calendar: Calendar): String {
        val month = calendar.get(Calendar.MONTH + 1)
        val year = calendar.get(Calendar.YEAR)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$day-$month-$year"
    }

    @SuppressLint("SimpleDateFormat")
    fun getStrDateFromPositionDay(position: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.getInstance().get(Calendar.YEAR) - 100, 1, 1)
        cal.add(Calendar.DATE, position)
        return SimpleDateFormat("dd/MM/yyyy").format(cal.time)
    }


    fun getDayOfSunday(cal: Calendar): Calendar? {
        for(i in 1 until 7) {
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                return cal
            else {
                cal.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        return null
    }
}