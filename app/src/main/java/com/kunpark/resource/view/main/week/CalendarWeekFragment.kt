package com.kunpark.resource.view.main.week

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarWeekPagerAdapter
import com.prabhat1707.verticalpager.VerticalViewPager
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CalendarWeekFragment : BaseFragment() {
    private var vpCalendar: VerticalViewPager? = null
    private lateinit var list: ArrayList<LocalDate>

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
        val localDate = LocalDate.now()

        var startLocalDate: LocalDate = localDate.minusDays((localDate.dayOfMonth - 1).toLong())
        startLocalDate = startLocalDate.minusMonths((localDate.month.value - 1).toLong())
        startLocalDate = startLocalDate.minusYears(100)

        Utils.getDayOfSunday(startLocalDate)?.let {
            startLocalDate = startLocalDate.plusDays((it.dayOfMonth - 1).toLong())
        }


        var totalDay = 0
        for (i in 1 until 200) {
            val currentLocalDate = LocalDate.now()
            if (i < 100) {
                currentLocalDate.minusYears(i.toLong())
            } else {
                currentLocalDate.plusYears(i.toLong())
            }

            totalDay += currentLocalDate.lengthOfYear()
        }

        list = arrayListOf()
        for (i in 0 until totalDay / 7) {
            startLocalDate = startLocalDate.plusDays(if(i == 0) 0L else 7L)
            list.add(startLocalDate)
        }

        // Get the number of days in that month
        vpCalendar = root.findViewById(R.id.vpCalendarWeek)
        vpCalendar?.adapter =
            CalendarWeekPagerAdapter(
                list,
                parentFragmentManager
            )

        vpCalendar?.currentItem = getCurrentPosition(list)
        vpCalendar?.offscreenPageLimit = 2



        vpCalendar?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                Event.onPageWeekChange(getStrFromLocalDate(list[position]))
            }

        })

    }

    private fun getCurrentPosition(list: ArrayList<LocalDate>): Int  {
        val localDate = LocalDate.now()
        if(!list.filter { it.isBefore(localDate) }.isNullOrEmpty()) {
            if(list.contains(list.filter { it.isBefore(localDate)}.max()!!)) {
              return list.indexOf(list.filter { it.isBefore(localDate)}.max()!!)
            }
        }


        return 0
    }

    private fun getStrFromLocalDate(localDate: LocalDate): String {
        val month = if (localDate.month.value.toString().length == 1) {
            "0" + localDate.month.value
        } else localDate.month.value.toString()

        return "$month-${localDate.year}"
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            if(isResumed && !list.isNullOrEmpty()) {
                vpCalendar?.currentItem = getCurrentPosition(list)
                Event.onPageWeekChange(getStrFromLocalDate(list[getCurrentPosition(list)]))
            }
        }
    }
}