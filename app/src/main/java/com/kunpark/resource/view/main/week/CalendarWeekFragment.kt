package com.kunpark.resource.view.main.week

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.event.Event
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DialogUtil
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarWeekPagerAdapter
import com.prabhat1707.verticalpager.VerticalViewPager
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarWeekFragment : BaseFragment() {
    private var vpCalendar: VerticalViewPager? = null
    private lateinit var list: ArrayList<Calendar>
    private var todayPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar_week, container, false)
        getDateOfWeek(root)
        return root
    }

    override fun onResume() {
        super.onResume()
        if(isResumed && !list.isNullOrEmpty()) {
            Event.onTitleDateChange(getStrFromCalendar(list[todayPosition]))
        }
    }

    private fun getDateOfWeek(root: View) {
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

            Utils.getDayOfSunday(startCal)?.let {
                startCal.set(Calendar.DAY_OF_MONTH, it.get(Calendar.DAY_OF_MONTH))
            }

            val diff: Long = endCal.time.time - startCal.time.time

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60

            var totalDay = hours / 24 / 7

            list = arrayListOf()
            for (i in 0 until totalDay) {
                val calPosition = Calendar.getInstance()
                calPosition.time = startCal.time
                calPosition.add(Calendar.DAY_OF_MONTH, (i * 7).toInt())
                list.add(calPosition)
            }

            if(todayPosition == 0) {
                checkCurrentPosition()
            }

            withContext(Dispatchers.Main) {
                // Get the number of days in that month
                vpCalendar = root.findViewById(R.id.vpCalendarWeek)
                vpCalendar?.adapter =
                    CalendarWeekPagerAdapter(
                        list,
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
                        if(isResumed) {
                            Event.onTitleDateChange(getStrFromCalendar(list[position]))
                        }
                    }

                })
            }

        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun checkCurrentPosition() {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch {
            list.filter { it.time.before(Date(System.currentTimeMillis())) }.max()?.let {
                todayPosition = list.indexOf(it)
                withContext(Dispatchers.Main) {
                    DialogUtil.hideLoading()
                    vpCalendar?.currentItem = todayPosition
                    Log.d("DONE", "Today Position i = $todayPosition")
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getStrFromCalendar(calendar: Calendar): String {
        return SimpleDateFormat(Constants.MM_YYYY).format(calendar.time)
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            vpCalendar?.currentItem = todayPosition
        }
    }
}