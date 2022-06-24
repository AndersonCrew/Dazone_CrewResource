package com.kunpark.resource.view.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kunpark.resource.utils.Config
import com.kunpark.resource.utils.checkNamNhuan
import com.kunpark.resource.view.main.agenda.AgendaFragment
import com.kunpark.resource.view.main.day.DayFragment
import com.kunpark.resource.view.main.month.MonthFragment
import com.kunpark.resource.view.main.week.WeekFragment
import java.text.ParseException
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AgendaPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val lastFirst = currentYear - 100
        val positionMonth = position % 12
        val positionYear = position / 12

        cal.set(positionYear + lastFirst, positionMonth, 1)
        return AgendaFragment(cal)
    }


    override fun getCount(): Int {
        return Config.COUNT_YEAR * 12
    }
}

class CalendarMonthPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val lastFirst = currentYear - 100
        val positionMonth = position % 12
        val positionYear = position / 12

        cal.set(positionYear + lastFirst, positionMonth, 1)
        return MonthFragment(cal)
    }

    override fun getCount(): Int {
        return Config.COUNT_YEAR * 12
    }
}

class CalendarWeekPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val cal = Calendar.getInstance()

        val year = when {
            position < count/2 -> {
                cal.get(Calendar.YEAR) - 100 + position / 48
            }

            position > count/2 -> {
                cal.get(Calendar.YEAR)  - 100 + position / 48
            }
            else -> cal.get(Calendar.YEAR)
        }
        return WeekFragment(cal)
    }

    override fun getCount(): Int {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        var totalDay = 0

        cal.set(Calendar.YEAR, 2022)
        cal.set(Calendar.MONTH, 6)
        cal.set(Calendar.WEEK_OF_MONTH, 2)


        for(i in currentYear - 100 until currentYear + 100) {
            totalDay += if(checkNamNhuan(i)) {
                366
            } else 365
        }

        return totalDay / 7
    }
}

class CalendarDayPagerAdapter(private val list: ArrayList<LocalDate>, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return DayFragment(list[position])
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCount(): Int {

        return list.size
    }
}