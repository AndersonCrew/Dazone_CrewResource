package com.kunpark.resource.view.main.agenda

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
import com.kunpark.resource.view.main.AgendaPagerAdapter
import com.kunpark.resource.view.main.CalendarAgendaPagerAdapter
import com.prabhat1707.verticalpager.VerticalViewPager
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarAgendaFragment : BaseFragment() {

    private var vpCalendar: VerticalViewPager? = null
    private lateinit var list: ArrayList<Calendar>
    private var todayPosition = 0
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

        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch {
            val cal = Calendar.getInstance()
            cal.time = Date(System.currentTimeMillis())
            val currentYear = cal.get(Calendar.YEAR)

            val startCal = Calendar.getInstance()
            startCal.set(Calendar.DAY_OF_MONTH, 1)
            startCal.set(Calendar.MONTH, 0)
            startCal.set(Calendar.YEAR, currentYear - 6)

            val endCal = Calendar.getInstance()

            endCal.set(Calendar.MONTH, 11)
            endCal.set(Calendar.YEAR, currentYear + 2)
            endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH))

            list = arrayListOf()
            for (i in 0 until 12 * 8) {
                val calPosition = Calendar.getInstance()
                calPosition.time = startCal.time
                calPosition.add(Calendar.MONTH, 1)
                list.add(calPosition)

                if (todayPosition == 0) {
                    checkCurrentPosition(calPosition, i)
                }
            }

            withContext(Dispatchers.Main) {
                vpCalendar = rootView.findViewById(R.id.vpCalendar)
                vpCalendar?.adapter = CalendarAgendaPagerAdapter(list,
                    parentFragmentManager
                )
                vpCalendar?.currentItem = todayPosition
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
                        Event.onPageMonthChange(Utils.getStrDateFromPosition(list[position]))
                    }

                })
            }
        }


        /*vpCalendar = rootView.findViewById(R.id.vpCalendar)
        vpCalendar?.adapter = AgendaPagerAdapter(
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
                Event.onPageMonthChange(Utils.getStrDateFromPosition(position))
            }

        })*/
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkCurrentPosition(calPosition: Calendar, i: Int) {
        val cal = Calendar.getInstance()
        cal.time = Date(System.currentTimeMillis())
        if (SimpleDateFormat("MM/yyyy").format(calPosition.time) == SimpleDateFormat("MM/yyyy").format(
                cal.time
            )
        ) {
            todayPosition = i
            vpCalendar?.currentItem = i
        }
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            vpCalendar?.currentItem = Utils.getPositionAgendaFromCalendar(Calendar.getInstance())
        }
    }
}