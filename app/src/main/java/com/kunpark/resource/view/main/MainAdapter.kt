package com.kunpark.resource.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.CalendarType
import com.kunpark.resource.view.main.agenda.CalendarAgendaFragment
import com.kunpark.resource.view.main.day.CalendarDayFragment
import com.kunpark.resource.view.main.month.CalendarMonthFragment
import com.kunpark.resource.view.main.week.CalendarWeekFragment
import java.util.*

class MainAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
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
}