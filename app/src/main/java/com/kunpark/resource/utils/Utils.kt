package com.kunpark.resource.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
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

    fun getPositionDayFromCalendar(calendar: Calendar): Int {
        val cal = Calendar.getInstance()
        val startCal = Calendar.getInstance()

        startCal.set(cal.get(Calendar.YEAR) - 100, 1, 1)

        try {
            val date1: Date = startCal.time
            val date2: Date = calendar.time
            val diff = date2.time - date1.time
            val count = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            return count.toInt()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    fun getStrDateFromPosition(position: Int): String {
        val month = position % 12
        val year = position / 12 + 1921
        val day = if(Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance().get(Calendar.MONTH) + 1 == month) Calendar.getInstance().get(Calendar.DAY_OF_MONTH) else 1
        return "$day-$month-$year"
    }

    @SuppressLint("SimpleDateFormat")
    fun getStrDateFromPositionDay(position: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.getInstance().get(Calendar.YEAR) - 100, 1, 1)
        cal.add(Calendar.DATE, position)
        return SimpleDateFormat("dd/MM/yyyy").format(cal.time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate?>? {
        val days = ArrayList<LocalDate?>()
        var current = sundayForDate(selectedDate)
        val endDate = current!!.plusWeeks(1)
        while (current!!.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sundayForDate(current: LocalDate): LocalDate? {
        var current = current
        val nextWeek = current.plusDays(1)
        while (current.isBefore(nextWeek)) {
            if (current.dayOfWeek == DayOfWeek.SUNDAY) return current
            current = current.plusDays(1)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfSunday(current: LocalDate): LocalDate? {
        for(i in 1 until 7) {
            if(current.dayOfWeek == DayOfWeek.SUNDAY)
                return current
            else {
                current.plusDays(1)
            }
        }

        return null
    }
}