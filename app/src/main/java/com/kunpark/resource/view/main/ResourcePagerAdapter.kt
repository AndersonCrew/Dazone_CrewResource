package com.kunpark.resource.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kunpark.resource.utils.Config
import com.kunpark.resource.view.main.agenda.AgendaFragment
import com.kunpark.resource.view.main.day.DayFragment
import com.kunpark.resource.view.main.month.MonthFragment
import com.kunpark.resource.view.main.week.WeekFragment
import java.util.*
import kotlin.collections.ArrayList


class AgendaPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val lastFirst = currentYear - 10
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

class CalendarDayPagerAdapter(private val list: ArrayList<Calendar>, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return DayFragment(list[position])
    }

    override fun getCount(): Int {
        return list.size
    }
}

class CalendarWeekPagerAdapter(private val list: ArrayList<Calendar>, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return WeekFragment(list[position])
    }

    override fun getCount(): Int {
        return list.size
    }
}

class CalendarAgendaPagerAdapter(private val list: ArrayList<Calendar>, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return AgendaFragment(list[position])
    }

    override fun getCount(): Int {
        return list.size
    }
}