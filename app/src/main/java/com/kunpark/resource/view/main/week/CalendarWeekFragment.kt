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
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CalendarWeekFragment : BaseFragment() {
    private var vpCalendar: VerticalViewPager? = null
    private lateinit var list: ArrayList<Calendar>

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
        cal.time = Date(System.currentTimeMillis())
        val currentYear = cal.get(Calendar.YEAR)

        val startCal = Calendar.getInstance()
        startCal.set(Calendar.DAY_OF_MONTH, 1)
        startCal.set(Calendar.MONTH, 1)
        startCal.set(Calendar.YEAR, currentYear - 100)

        val endCal = Calendar.getInstance()

        endCal.set(Calendar.MONTH, 12)
        endCal.set(Calendar.YEAR, currentYear + 100)
        endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH))

        Utils.getDayOfSunday(startCal)?.let {
            startCal.set(Calendar.DAY_OF_MONTH, it.get(Calendar.DAY_OF_MONTH))
        }

        val diff: Long = endCal.time.time - startCal.time.time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        var totalDay = hours / 24

        list = arrayListOf()
        for (i in 0 until totalDay / 7) {
            startCal.add(Calendar.DAY_OF_MONTH, 7)
            list.add(startCal)
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
                Event.onPageWeekChange(getStrFromCalendar(list[position]))
            }

        })

    }

    private fun getCurrentPosition(list: ArrayList<Calendar>): Int  {
        val cal = Calendar.getInstance()
        cal.time = Date(System.currentTimeMillis())
        if(!list.filter { it.time.before(cal.time) }.isNullOrEmpty()) {
            if(list.contains(list.filter { it.time.before(cal.time) }.max()!!)) {
              return list.indexOf(list.filter { it.time.before(cal.time) }.max()!!)
            }
        }


        return 0
    }

    private fun getStrFromCalendar(calendar: Calendar): String {
        val month = if (calendar.get(Calendar.MONTH).toString().length == 1) {
            "0" + calendar.get(Calendar.MONTH).toString()
        } else calendar.get(Calendar.MONTH).toString()

        return "$month-${calendar.get(Calendar.YEAR)}"
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            if(isResumed && !list.isNullOrEmpty()) {
                vpCalendar?.currentItem = getCurrentPosition(list)
                Event.onPageWeekChange(getStrFromCalendar(list[getCurrentPosition(list)]))
            }
        }
    }
}