package com.kunpark.resource.view.main.day

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.custom_view.VerticalViewPager
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarDayPagerAdapter
import com.kunpark.resource.view.main.CalendarMonthPagerAdapter
import java.util.*

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
        vpCalendar = root.findViewById(R.id.vpCalendarDay)
        vpCalendar?.adapter =
            CalendarDayPagerAdapter(
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