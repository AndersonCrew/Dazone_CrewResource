package com.kunpark.resource.view.main.month

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarMonthPagerAdapter
import com.prabhat1707.verticalpager.VerticalViewPager
import java.util.*

class CalendarMonthFragment : BaseFragment() {

    private var vpCalendar: VerticalViewPager?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frgament_calendar_month, container, false)
        initView(root)
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView(root: View) {
        vpCalendar = root.findViewById(R.id.vpCalendarMonth)
        vpCalendar?.adapter =
            CalendarMonthPagerAdapter(
                parentFragmentManager
            )
        val cal = Calendar.getInstance()
        vpCalendar?.currentItem = Utils.getPositionAgendaFromCalendar(cal)
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
                Event.onTitleDateChange(Utils.getStrDateFromPosition(cal))
            }

        })
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            vpCalendar?.currentItem = Utils.getPositionAgendaFromCalendar(Calendar.getInstance())
        }
    }
}