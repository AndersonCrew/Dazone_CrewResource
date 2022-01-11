package com.kunpark.resource.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.CalendarType
import com.kunpark.resource.view.main.agenda.CalendarAgendaFragment
import com.kunpark.resource.view.main.day.CalendarDayFragment
import com.kunpark.resource.view.main.month.CalendarMonthFragment
import com.kunpark.resource.view.main.week.CalendarWeekFragment
import java.util.*

class MainAdapter(fr: FragmentManager): FragmentStatePagerAdapter(fr, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val fragment: BaseFragment
        when(position) {
            0 -> {
                fragment = CalendarAgendaFragment()
            }

            1-> {
                fragment = CalendarDayFragment()
            }

            2-> {
                fragment = CalendarWeekFragment()
            }

            else -> {
                fragment = CalendarMonthFragment()
            }
        }

        return fragment
    }

    override fun getCount(): Int {
        return CalendarType.values().size - 1
    }
}