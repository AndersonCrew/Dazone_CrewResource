package com.kunpark.resource.view.main.day

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.CalendarDay
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class DayFragment(private val calendar: Calendar): BaseFragment() {
    private val viewModel: CalendarDayViewModel by viewModels()
    private var hasCallRefreshData = false
    private var adapter: DayAdapter?= null
    private var data: ArrayList<CalendarDto>?= null
    private var rvDays: RecyclerView?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_day_resource, container, false)
        rvDays = root.findViewById(R.id.rvDays)
        initRecyclerView()
        initViewModel()
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun initViewModel() {
        val day = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)
        Log.d("CALENDAR_DAY", "initViewModel = $day")
        viewModel.getResourceDB(day)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null && !it.list.isNullOrEmpty()) {
                //TODO Update list
                updateDataList(it.list!!)

            }

            if(!hasCallRefreshData) {
                hasCallRefreshData = true
                getAllResource(null)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateDataList(list: List<CalendarDto>) {
        Log.d("UPDATE", "${list.size}")
        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch {
            data?.forEach { cal ->
                cal.listResource.clear()
                list.forEach { calNex ->
                    if(cal.timeString == calNex.timeString) {
                        cal.listResource.addAll(calNex.listResource)
                        Log.d("UPDATE", " == ${calNex.timeString}")
                    }
                }
            }

            withContext(Dispatchers.Main) {
                adapter?.notifyDataSetChanged()
            }
        }

    }

    private fun initRecyclerView() {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch {
            data = arrayListOf()
            for(i in -1 until 24) {
                val time = when(i) {
                    -1 -> "All Day"

                    0,1,2,3,4,5,6,7,8,9 -> {
                        "0$i:00"
                    }

                    else -> "$i:00"
                }

                val calDto = CalendarDto(timeString = time)
                data?.add(calDto)
            }

            adapter = DayAdapter(data!!)
            Log.d("CALENDAR_DAY", "InitRecyclerView")
            withContext(Dispatchers.Main) {
                if(isAdded) {
                    Log.d("CALENDAR_DAY", "SetAdapter")
                    rvDays?.adapter = adapter
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
}
