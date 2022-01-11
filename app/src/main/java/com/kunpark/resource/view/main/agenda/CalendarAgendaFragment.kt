package com.kunpark.resource.view.main.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.AgendaPagerAdapter
import java.util.*

class CalendarAgendaFragment : BaseFragment() {

    private var vpCalendar : ViewPager?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar_agenda, container, false)
        initView(root)
        return root
    }

    private fun initView(rootView: View) {
        vpCalendar = rootView.findViewById(R.id.vpCalendar)
        vpCalendar?.adapter = AgendaPagerAdapter(
            parentFragmentManager
        )
        val cal = Calendar.getInstance()
        vpCalendar?.currentItem = Utils.getPositionAgendaFromCalendar(cal)
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
                Event.onPageMonthChange(Utils.getStrDateFromPosition(position))
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