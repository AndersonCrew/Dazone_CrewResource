package com.kunpark.resource.view.main.week

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.contains
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.CalendarWeek
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.model.Resource
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
class WeekFragment(private val calendar: Calendar): BaseFragment() {

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
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initViewModel(context)
    }

    private fun initView(view: View) {
        llTime = view.findViewById(R.id.llTime)
        tv1 = view.findViewById(R.id.tv1)
        tv2 = view.findViewById(R.id.tv2)
        tv3 = view.findViewById(R.id.tv3)
        tv4 = view.findViewById(R.id.tv4)
        tv5 = view.findViewById(R.id.tv5)
        tv6 = view.findViewById(R.id.tv6)
        tv7 = view.findViewById(R.id.tv7)

        for(i in 0 until 8) {
            val llTimeWeek = LinearLayout(requireContext())
            llTimeWeek.id = i
            llTimeWeek.orientation = LinearLayout.VERTICAL
            llTimeWeek.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            for(j in -1 until 24) {
                if(i == 0) {
                    val tvDate = TextView(requireContext())
                    tvDate.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 140)
                    tvDate.textSize = 13f
                    tvDate.setBackgroundResource(if(j == -1) R.drawable.bg_allday else R.drawable.bg_allday_week)
                    tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
                    tvDate.gravity = Gravity.CENTER
                    if(j == -1) {
                        tvDate.text = getString(R.string.all_day)
                    } else {
                        val time = if(j <  10) "0$j" else j
                        tvDate.text = "$time:00"
                    }
                    llTimeWeek.addView(tvDate)
                } else {
                    val llChild = LinearLayout(requireContext())
                    llChild.id = j
                    llChild.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 140)
                    llChild.orientation = LinearLayout.VERTICAL
                    llChild.gravity = Gravity.CENTER
                    llChild.setBackgroundResource(if(j == -1) R.drawable.bg_allday else R.drawable.bg_allday_week)
                    llTimeWeek.addView(llChild)
                }
            }

            llTime?.addView(llTimeWeek)
        }

        for(i in 0 until 7) {

            val cal = Calendar.getInstance()
            cal.time = calendar.time
            cal.add(Calendar.DAY_OF_MONTH, i)
            when(i) {
                0 -> {
                    tv1?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                }

                1 -> {
                    tv2?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                }

                2 -> {
                    tv3?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                }

                3 -> {
                    tv4?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                }

                4 -> {
                    tv5?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                }

                5 -> {
                    tv6?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                }

                6 -> {
                    tv7?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                }
            }
        }
    }

    private var hasCallRefreshData = false
    @SuppressLint("SimpleDateFormat")
    private fun initViewModel(context: Context) {
        val firstDay = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)
        viewModel.getResourceDB(firstDay)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null) {
                bindData(it, context)
            }

            if(!hasCallRefreshData) {
                hasCallRefreshData = true
                getAllResource(null)
            }
        })
    }


    private fun bindData(it: CalendarWeek, context: Context) {
        if(!it.list.isNullOrEmpty()) {
            for(i in 0 until 7) {
                val cal = Calendar.getInstance()
                cal.time = calendar.time
                cal.add(Calendar.DAY_OF_MONTH, i)
                for(calendarDto in it.list!!) {
                    if(SimpleDateFormat(Constants.YY_MM_DD).format(cal.time) == calendarDto.timeString) {
                        bindingDataChild(calendarDto, i + 1, context)
                        break
                    }
                }
            }
        }
    }

    private fun bindingDataChild(calendarDto: CalendarDto, i: Int, context: Context) {
        llTime?.children?.find { view -> view.id == i }?.let { llChild ->
            removeAllView(llChild)
            val listCheck: ArrayList<Resource> = arrayListOf()
            for(resource in calendarDto.listResource) {
                var startTime = resource.startTime?.split(":")?.get(0)?.toInt()?: 0
                var endTime = resource.endTime?.split(":")?.get(0)?.toInt()?: 0

                for(child in (llChild as LinearLayout).children) {
                    if(child.id in startTime..endTime && !listCheck.contains(resource)) {
                        val view = CardView(context)
                        val tvContent = TextView(context)
                        tvContent.gravity = Gravity.CENTER
                        tvContent.setPadding(10, 5, 10, 5)
                        tvContent.textSize = 9f
                        tvContent.maxLines = 3
                        view.addView(tvContent)
                        val param = LinearLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            0, 1f
                        )

                        tvContent.text = resource.title?: ""
                        tvContent.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                        tvContent.setBackgroundColor(Color.parseColor(resource.backgroundColor))

                        view.layoutParams = param

                        view.setOnClickListener {
                            val intent = Intent(requireContext(), DetailScheduleActivity::class.java)
                            intent.putExtra(Constants.RESOURCE, resource)
                            requireActivity().startActivity(intent)
                        }

                        if(!(child as LinearLayout).contains(view)) {
                            child.addView(view)
                            listCheck.add(resource)
                        }
                    }
                }
            }
        }
    }

    private fun removeAllView(llChild: View) {
        for(childLinear in (llChild as LinearLayout).children) {
            (childLinear as LinearLayout).removeAllViews()
        }
    }

    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val firstDay = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)
        val lastCal: Calendar = Calendar.getInstance()
        lastCal.time = calendar.time
        lastCal.add(Calendar.DAY_OF_MONTH, 7)
        val lastDay = SimpleDateFormat(Constants.Format_api_datetime).format(lastCal.time)

        val params = JsonObject()
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", firstDay)
        params.addProperty("endDate", lastDay)
        params.addProperty("rsvnStatus", conditionSearch?.key?: "ALL")
        viewModel.getAllResource(params, calendar)
    }
}