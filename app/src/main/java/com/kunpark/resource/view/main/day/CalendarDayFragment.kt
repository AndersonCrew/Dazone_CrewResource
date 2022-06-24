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
import com.kunpark.resource.custom_view.VerticalViewPager
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarDayPagerAdapter
import com.kunpark.resource.view.main.CalendarMonthPagerAdapter
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class CalendarDayFragment : BaseFragment() {

    private var vpCalendar: VerticalViewPager?= null
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

        val list: ArrayList<LocalDate> = arrayListOf()
        for(i in 0 until totalDay / 7) {
            startLocalDate = startLocalDate.plusWeeks(i.toLong())
            list.add(startLocalDate)
        }

        vpCalendar = root.findViewById(R.id.vpCalendarDay)

        vpCalendar?.adapter =
            CalendarDayPagerAdapter(list,
                parentFragmentManager
            )
        val cal = Calendar.getInstance()
        vpCalendar?.currentItem = Utils.getPositionDayFromCalendar(cal)
        vpCalendar?.offscreenPageLimit = 1

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
                Event.onPageDayChange(Utils.getStrDateFromPositionDay(position))
            }

        })
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            vpCalendar?.currentItem = Utils.getPositionDayFromCalendar(Calendar.getInstance())
            Event.onPageDayChange(Utils.getStrDateFromPositionDay(vpCalendar?.currentItem?: 0))
        }
    }
}