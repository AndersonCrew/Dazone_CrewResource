package com.kunpark.resource.view.main.day

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.CalendarDay
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity
import java.text.SimpleDateFormat
import java.util.*

class DayFragment(private val calendar: Calendar): BaseFragment() {
    private var llTime: LinearLayout?= null
    private var scrollView: NestedScrollView?= null
    private val viewModel: CalendarDayViewModel by viewModels()
    private var hasCallRefreshData = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_day_resource, container, false)
        initView(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initViewModel(context)
    }
    @SuppressLint("SimpleDateFormat")
    private fun initViewModel(context: Context) {

        val day = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)
        viewModel.getResourceDB(day)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null) {
                bindData(it, context)
            }

            if(!hasCallRefreshData) {
                hasCallRefreshData = true
                getAllResource(null)
            }
        })
    }

    private fun bindData(it: CalendarDay, context: Context) {
        if(!it.list.isNullOrEmpty()) {
            for(calendarDto in it.list!!) {
                calendarDto.timeString?.let {
                    val time = Integer.parseInt(it)
                    val llChild = llTime?.children?.find { view -> view.id == time }
                    if(llChild != null && !calendarDto.listResource.isNullOrEmpty()) {
                        val llTimeCalendar: LinearLayout? = llChild.findViewById(R.id.llTime)
                        llTimeCalendar?.removeAllViews()
                        for(resource in calendarDto.listResource) {
                            val view = CardView(context)
                            val tvContent = TextView(context)
                            tvContent.gravity = Gravity.CENTER
                            tvContent.setPadding(10, 5, 10, 5)
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

                            view.setOnClickListener {
                                val intent = Intent(requireContext(), DetailScheduleActivity::class.java)
                                intent.putExtra(Constants.RESOURCE, resource)
                                requireActivity().startActivity(intent)
                            }

                            llTimeCalendar?.addView(view)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val params = JsonObject()
        val time = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)

        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", time)
        params.addProperty("endDate", time)
        params.addProperty("rsvnStatus", conditionSearch?.key?: "ALL")
        viewModel.getAllResource(params, calendar)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView(root: View?) {
        llTime = root?.findViewById(R.id.llTime)

        for(i in -1 until 24) {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_hour, null)
            val tvTime: TextView? = view?.findViewById(R.id.tvTime)
            val llTimeChild: LinearLayout? = view?.findViewById(R.id.llTime)
            if(i == -1) {
                tvTime?.text = getString(R.string.allday)
                tvTime?.setBackgroundResource(R.color.colorGrayBackground)
                llTimeChild?.setBackgroundResource(R.color.colorGrayBackground)
            } else {
                val time = if(i <  10) "0$i" else i
                tvTime?.text = "$time:00"
            }

            view.id = i
            llTime?.addView(view)
        }
    }
}
