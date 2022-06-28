package com.kunpark.resource.view.main.day

import android.annotation.SuppressLint
import android.icu.util.LocaleData
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DialogUtil
import com.kunpark.resource.utils.Utils
import com.kunpark.resource.view.main.CalendarDayPagerAdapter
import com.prabhat1707.verticalpager.VerticalViewPager
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarDayFragment : BaseFragment() {

    private var vpCalendar: VerticalViewPager? = null
    private lateinit var list: ArrayList<Calendar>
    private var todayPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.frgament_calendar_day, container, false)
        initView(root)
        return root
    }

    override fun onResume() {
        super.onResume()
        if (isResumed && todayPosition == 0) {
            DialogUtil.displayLoadingWithText(requireContext(), "Please wait...", false)
        } else {
            DialogUtil.hideLoading()
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
            startCal.set(Calendar.YEAR, currentYear - 100)

            val endCal = Calendar.getInstance()

            endCal.set(Calendar.MONTH, 11)
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
            for (i in 0 until totalDay) {
                val calPosition = Calendar.getInstance()
                calPosition.time = startCal.time
                calPosition.add(Calendar.DAY_OF_MONTH, i.toInt())
                list.add(calPosition)

                if (todayPosition == 0) {
                    checkCurrentPosition(calPosition, i.toInt())
                }
            }

            withContext(Dispatchers.Main) {
                vpCalendar = root.findViewById(R.id.vpCalendarDay)

                vpCalendar?.adapter =
                    CalendarDayPagerAdapter(
                        list,
                        parentFragmentManager
                    )

                vpCalendar?.currentItem = todayPosition
                vpCalendar?.offscreenPageLimit = 2
                Event.onPageDayChange(getStrCalendar(list[todayPosition]))

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
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun checkCurrentPosition(calendar: Calendar, position: Int) {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch {
            val strCurrentDate =
                SimpleDateFormat(Constants.YY_MM_DD).format(Date(System.currentTimeMillis()))
            val strPositionDate = SimpleDateFormat(Constants.YY_MM_DD).format(calendar.time)
            if (strCurrentDate == strPositionDate) {
                todayPosition = position

                withContext(Dispatchers.Main) {
                    DialogUtil.hideLoading()
                    vpCalendar?.currentItem = todayPosition
                    Log.d("DONE", "Today Position i = $strPositionDate")
                }

            }
            Log.d("CHECK", "CHECK Position i = $strPositionDate")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getStrCalendar(calendar: Calendar): String {
        return SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.MOVE_TODAY]?.let {
            if (isResumed && !list.isNullOrEmpty()) {
                vpCalendar?.currentItem = todayPosition
                Event.onPageDayChange(getStrCalendar(list[todayPosition]))
            }
        }

        it[Event.ON_PAGE_MAIN_CHANGED]?.let {
            Event.onPageDayChange(getStrCalendar(list[todayPosition]))
        }
    }
}