package com.kunpark.resource.view.main.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarMonthPagerAdapter
import com.kunpark.resource.view.main.CalendarWeekPagerAdapter
import java.util.*

class CalendarWeekFragment : BaseFragment() {
    private var vpCalendar: ViewPager?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar_week, container, false)
        getDateOfWeek(root)
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun getDateOfWeek(root: View) {
        val cal = Calendar.getInstance()
        val firstDate = cal.firstDayOfWeek
        // Get the number of days in that month

        // Get the number of days in that month
        vpCalendar = root.findViewById(R.id.vpCalendarWeek)
        vpCalendar?.adapter =
            CalendarWeekPagerAdapter(
                parentFragmentManager
            )
        vpCalendar?.currentItem = Utils.getPositionAgendaFromCalendar(cal)
        vpCalendar?.offscreenPageLimit = 1
    }
}