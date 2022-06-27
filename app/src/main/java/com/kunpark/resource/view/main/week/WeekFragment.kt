package com.kunpark.resource.view.main.week

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.main.day.CalendarDayViewModel
import kotlinx.android.synthetic.main.fragment_daily.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class WeekFragment(private val localDate: LocalDate): BaseFragment() {

    private var llAllDay: LinearLayout?= null
    private var llTime: LinearLayout?= null
    private var tv1: TextView?= null
    private var tv2: TextView?= null
    private var tv3: TextView?= null
    private var tv4: TextView?= null
    private var tv5: TextView?= null
    private var tv6: TextView?= null
    private var tv7: TextView?= null
    private val viewModel: CalendarWeekViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_week_resource, container, false)
        initView(root)
        initViewModel()
        return root
    }


    private fun initView(view: View) {
        llAllDay = view.findViewById(R.id.llAllDay)
        llTime = view.findViewById(R.id.llTime)
        tv1 = view.findViewById(R.id.tv1)
        tv2 = view.findViewById(R.id.tv2)
        tv3 = view.findViewById(R.id.tv3)
        tv4 = view.findViewById(R.id.tv4)
        tv5 = view.findViewById(R.id.tv5)
        tv6 = view.findViewById(R.id.tv6)
        tv7 = view.findViewById(R.id.tv7)

        val tvAllDay = TextView(requireContext())
        tvAllDay.text = "All Day"
        val param = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        tvAllDay.layoutParams = param
        tvAllDay.textSize = 13f
        tvAllDay.setBackgroundResource(R.drawable.bg_allday)
        tvAllDay.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
        tvAllDay.gravity = Gravity.CENTER
        llAllDay?.addView(tvAllDay)

        for(i in 1 until 8) {
            val tvDate = TextView(requireContext())
            tvDate.text = ""
            tvDate.id = i
            val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            tvDate.layoutParams = param
            tvDate.textSize = 13f
            tvDate.setBackgroundResource(R.drawable.bg_allday)
            tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            tvDate.gravity = Gravity.CENTER
            llAllDay?.addView(tvDate)
        }

        for(i in 0 until 25) {
            val llTimeWeek = LinearLayout(requireContext())
            for(j in 0 until 8) {
                val tvDate = TextView(requireContext())
                tvDate.id = j
                val param = LinearLayout.LayoutParams(0, 140, 1f)
                tvDate.layoutParams = param
                tvDate.textSize = 13f
                tvDate.setBackgroundResource(R.drawable.bg_allday_week)
                tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
                tvDate.gravity = Gravity.CENTER
                if(j == 0) {
                    val time = if(i <  10) "0$i" else i
                    tvDate.text = "$time:00"
                } else {
                    tvDate.text = ""
                }

                llTimeWeek.addView(tvDate)
            }

            llTimeWeek.id = i
            llTime?.addView(llTimeWeek)
        }

        for(i in 0 until 7) {
            var localDateItem : LocalDate = localDate
            localDateItem = localDateItem.plusDays(i.toLong())
            when(i) {
                0 -> {
                    tv1?.text = localDateItem.dayOfMonth.toString()
                }

                1 -> {
                    tv2?.text = localDateItem.dayOfMonth.toString()
                }

                2 -> {
                    tv3?.text = localDateItem.dayOfMonth.toString()
                }

                3 -> {
                    tv4?.text = localDateItem.dayOfMonth.toString()
                }

                4 -> {
                    tv5?.text = localDateItem.dayOfMonth.toString()
                }

                5 -> {
                    tv6?.text = localDateItem.dayOfMonth.toString()
                }

                6 -> {
                    tv7?.text = localDateItem.dayOfMonth.toString()
                }
            }
        }
    }

    private var hasCallRefreshData = false
    private fun initViewModel() {
        val firstDay = localDate.format(DateTimeFormatter.ofPattern(Constants.Format_api_datetime))
        viewModel.getResourceDB(firstDay)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null) {
                //bindData(it, context)
            }

            if(!hasCallRefreshData) {
                hasCallRefreshData = true
                getAllResource(null)
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val firstDay = localDate.format(DateTimeFormatter.ofPattern(Constants.Format_api_datetime))
        val lastLocalDate: LocalDate = localDate.plusDays(7)
        val lastDay = lastLocalDate.format(DateTimeFormatter.ofPattern(Constants.Format_api_datetime))

        val params = JsonObject()
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", firstDay)
        params.addProperty("endDate", lastDay)
        params.addProperty("rsvnStatus", conditionSearch?.key?: "ALL")
        viewModel.getAllResource(params, localDate)
    }
}