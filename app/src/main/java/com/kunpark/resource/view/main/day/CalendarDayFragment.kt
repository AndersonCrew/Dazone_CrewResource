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
import com.mohitdev.verticalviewpager.VerticalViewPager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class CalendarDayFragment : BaseFragment() {

    private var vpCalendar: VerticalViewPager?= null
    private lateinit var list: ArrayList<LocalDate>
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
        val localDate = LocalDate.now()

        var startLocalDate: LocalDate = localDate.minusDays((localDate.dayOfMonth - 1).toLong())
        startLocalDate = startLocalDate.minusMonths((localDate.month.value - 1).toLong())
        startLocalDate = startLocalDate.minusYears(100)

        Utils.getDayOfSunday(startLocalDate)?.let {
            startLocalDate = startLocalDate.plusDays((it.dayOfMonth - 1).toLong())
        }



        var totalDay = 0
        for(i in 1 until 200) {
            val currentLocalDate = LocalDate.now()
            if(i < 100) {
                currentLocalDate.minusYears(i.toLong())
            } else {
                currentLocalDate.plusYears(i.toLong())
            }

            totalDay += currentLocalDate.lengthOfYear()
        }

        list = arrayListOf()
        for(i in 0 until totalDay) {
            startLocalDate = startLocalDate.plusDays(1L)
            list.add(startLocalDate)
        }

        vpCalendar = root.findViewById(R.id.vpCalendarDay)

        vpCalendar?.adapter =
            CalendarDayPagerAdapter(list,
                parentFragmentManager
            )

        vpCalendar?.currentItem = getCurrentPosition(list)
        vpCalendar?.offscreenPageLimit = 1
        Event.onPageDayChange(getStrLocalDate(list[getCurrentPosition(list)]))

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
                Event.onPageDayChange(getStrLocalDate(list[position]))
            }

        })
    }

    private fun getStrLocalDate(localDate: LocalDate): String {
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    private fun getCurrentPosition(list: java.util.ArrayList<LocalDate>): Int  {
        val localDate = LocalDate.now()
        if(list.contains(list.find { it.isEqual(localDate)})) {
            return list.indexOf(list.find { it.isEqual(localDate)})
        }

        return 0
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            if(isResumed && !list.isNullOrEmpty()) {
                vpCalendar?.currentItem = getCurrentPosition(list)
                Event.onPageDayChange(getStrLocalDate(list[getCurrentPosition(list)]))
            }
        }

        it[Event.ON_PAGE_MAIN_CHANGED]?.let {
            Event.onPageDayChange(getStrLocalDate(list[getCurrentPosition(list)]))
        }
    }
}