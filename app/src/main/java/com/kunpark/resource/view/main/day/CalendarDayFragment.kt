package com.kunpark.resource.view.main.day

import android.annotation.SuppressLint
import android.icu.util.LocaleData
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.util.Util
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarDayPagerAdapter
import com.prabhat1707.verticalpager.VerticalViewPager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class CalendarDayFragment : BaseFragment() {

    private var vpCalendar: VerticalViewPager?= null
    private lateinit var list: ArrayList<Calendar>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.frgament_calendar_day, container, false)
        initView(root)
        return root
    }


    @SuppressLint("SimpleDateFormat")
    private fun initView(root: View) {
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
        for(i in 0 until totalDay) {
            startCal.add(Calendar.DAY_OF_MONTH, 1)
            list.add(startCal)
        }

        vpCalendar = root.findViewById(R.id.vpCalendarDay)

        vpCalendar?.adapter =
            CalendarDayPagerAdapter(list,
                parentFragmentManager
            )

        vpCalendar?.currentItem = getCurrentPosition(list)
        vpCalendar?.offscreenPageLimit = 2
        Event.onPageDayChange(getStrCalendar(list[getCurrentPosition(list)]))

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
                Event.onPageDayChange(getStrCalendar(list[position]))
            }

        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getStrCalendar(calendar: Calendar): String {
        return SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    }

    private fun getCurrentPosition(list: ArrayList<Calendar>): Int  {
        val cal = Calendar.getInstance()
        cal.time = Date(System.currentTimeMillis())
        if(list.contains(list.find { it.time.equals(cal.time)})) {
            return list.indexOf(list.find { it.time.equals(cal.time)})
        }

        return 0
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            if(isResumed && !list.isNullOrEmpty()) {
                vpCalendar?.currentItem = getCurrentPosition(list)
                Event.onPageDayChange(getStrCalendar(list[getCurrentPosition(list)]))
            }
        }

        it[Event.ON_PAGE_MAIN_CHANGED]?.let {
            Event.onPageDayChange(getStrCalendar(list[getCurrentPosition(list)]))
        }
    }
}