package com.kunpark.resource.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kunpark.resource.utils.Config
import com.kunpark.resource.view.main.agenda.AgendaFragment
import com.kunpark.resource.view.main.day.DayFragment
import com.kunpark.resource.view.main.month.MonthFragment
import com.kunpark.resource.view.main.week.WeekFragment
import java.text.ParseException
import java.util.*
import java.util.concurrent.TimeUnit

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
        cal.set(Calendar.getInstance().get(Calendar.YEAR) - 100, 1, 1)
        cal.add(Calendar.DATE, position)
        return WeekFragment(cal)
    }

    override fun getCount(): Int {
        val cal = Calendar.getInstance()
        val startCal = Calendar.getInstance()
        val endCal = Calendar.getInstance()

        startCal.set(cal.get(Calendar.YEAR) - 100, 1, 1)
        endCal.set(cal.get(Calendar.YEAR) + 100, 12, 1)
        val countDayOfEndCal = endCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        endCal.set(Calendar.DAY_OF_MONTH, countDayOfEndCal)

        try {
            val date1: Date = startCal.time
            val date2: Date = endCal.time
            val diff = date2.time - date1.time
            val count = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            return count.toInt() / 7
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }
}

class CalendarDayPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val cal = Calendar.getInstance()
        cal.set(Calendar.getInstance().get(Calendar.YEAR) - 100, 1, 1)
        cal.add(Calendar.DATE, position)
        return DayFragment(cal)
    }

    override fun getCount(): Int {
        val cal = Calendar.getInstance()
        val startCal = Calendar.getInstance()
        val endCal = Calendar.getInstance()

        startCal.set(cal.get(Calendar.YEAR) - 100, 1, 1)
        endCal.set(cal.get(Calendar.YEAR) + 100, 12, 1)
        val countDayOfEndCal = endCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        endCal.set(Calendar.DAY_OF_MONTH, countDayOfEndCal)

        try {
            val date1: Date = startCal.time
            val date2: Date = endCal.time
            val diff = date2.time - date1.time
            val count = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            return count.toInt()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }
}