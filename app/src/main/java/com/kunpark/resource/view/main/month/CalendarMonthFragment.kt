package com.kunpark.resource.view.main.month

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarAgendaPagerAdapter
import com.kunpark.resource.view.main.CalendarMonthPagerAdapter
import com.prabhat1707.verticalpager.VerticalViewPager
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarMonthFragment : BaseFragment() {

    private var vpCalendar: VerticalViewPager?= null
    private lateinit var list: ArrayList<Calendar>
    private var todayPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frgament_calendar_month, container, false)
        initView(root)
        return root
    }

    override fun onResume() {
        super.onResume()
        if(isAdded && ::list.isInitialized && !list.isNullOrEmpty()) {
            Event.onTitleDateChange(getStrCalendar(list[todayPosition]))
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView(root: View) {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch {
            val cal = Calendar.getInstance()
            cal.time = Date(System.currentTimeMillis())
            val currentYear = cal.get(Calendar.YEAR)

            val startCal = Calendar.getInstance()
            startCal.set(Calendar.DAY_OF_MONTH, 1)
            startCal.set(Calendar.MONTH, 0)
            startCal.set(Calendar.YEAR, currentYear - 3)


            list = arrayListOf()
            for (i in 0 until 12 * 5) {
                val calPosition: Calendar = Calendar.getInstance()
                calPosition.time = startCal.time
                calPosition.add(Calendar.MONTH, i)
                list.add(calPosition)

                if (todayPosition == 0) {
                    checkCurrentPosition(calPosition, i)
                }
            }

            withContext(Dispatchers.Main) {
                vpCalendar = root.findViewById(R.id.vpCalendarMonth)
                vpCalendar?.adapter = CalendarMonthPagerAdapter(list,
                    parentFragmentManager
                )
                if(todayPosition != 0) {
                    vpCalendar?.currentItem = todayPosition
                }

                vpCalendar?.offscreenPageLimit = 1
                if(isResumed) {
                    Event.onTitleDateChange(getStrCalendar(list[todayPosition]))
                }

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
                        if(isResumed) {
                            Event.onTitleDateChange(getStrCalendar(list[position]))
                        }
                    }

                })
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getStrCalendar(calendar: Calendar): String {
        return SimpleDateFormat("MM-yyyy").format(calendar.time)
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
            Log.d("ANDERSON", "todayPosition = $todayPosition \n timeStr = ${SimpleDateFormat("MM/yyyy").format(calPosition.time)}")
        }

        Log.d("ANDERSON", "CHECK position = ${SimpleDateFormat("MM/yyyy").format(calPosition.time)}")
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            vpCalendar?.currentItem = todayPosition
        }
    }
}