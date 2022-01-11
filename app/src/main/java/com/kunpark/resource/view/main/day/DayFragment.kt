package com.kunpark.resource.view.main.day

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.CalendarDay
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

class DayFragment(private val calendar: Calendar): BaseFragment() {
    private var llTime: LinearLayout?= null
    private val viewModel: CalendarDayViewModel by viewModels()
    private var hasBindData = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_day_resource, container, false)
        initView(root)
        initViewModel()
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun initViewModel() {

        val day = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)
        viewModel.getResourceDB(day)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null) {
                if(!hasBindData) {
                    hasBindData = true
                    bindData(it)
                }

            } else {
                getAllResource(null)
            }
        })
    }

    private fun bindData(it: CalendarDay) {
        if(!it.list.isNullOrEmpty()) {
            for(calendarDto in it.list!!) {
                val time = Integer.parseInt(calendarDto.timeString)
                val llChild = llTime?.children?.find { view -> view.id == time }
                if(llChild != null && !calendarDto.listResource.isNullOrEmpty()) {
                    val llTimeCalendar: LinearLayout? = llChild.findViewById(R.id.llTime)
                    llTimeCalendar?.removeAllViews()
                    for(resource in calendarDto.listResource) {
                        val view = CardView(requireContext())
                        val tvContent = TextView(requireContext())
                        tvContent.gravity = Gravity.CENTER
                        tvContent.textSize = 13f
                        view.addView(tvContent)
                        val param = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )

                        param.leftMargin = 10
                        tvContent.text = resource.title?: ""
                        tvContent.setBackgroundColor(Color.parseColor(resource.backgroundColor))

                        view.layoutParams = param

                        llTimeCalendar?.addView(view)
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val params = JsonObject()
        val time =  SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)

        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", time)
        params.addProperty("endDate", time)
        params.addProperty("rsvnStatus", conditionSearch?.key?: "ALL")
        viewModel.getAllResource(params, time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView(root: View?) {
        llTime = root?.findViewById(R.id.llTime)

        for(i in 0 until 24) {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_hour, null)
            val tvTime: TextView? = view?.findViewById(R.id.tvTime)
            val time = if(i <  10) "0$i" else i
            tvTime?.text = "$time:00"
            view.id = i
            llTime?.addView(view)
        }
    }
}