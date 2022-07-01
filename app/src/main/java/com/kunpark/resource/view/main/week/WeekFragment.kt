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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.contains
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.fragment_week_resource.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
class WeekFragment(private val calendar: Calendar): BaseFragment() {

    private var tv1: TextView?= null
    private var tv2: TextView?= null
    private var tv3: TextView?= null
    private var tv4: TextView?= null
    private var tv5: TextView?= null
    private var tv6: TextView?= null
    private var tv7: TextView?= null
    private var rvWeek: RecyclerView?= null
    private val viewModel: CalendarWeekViewModel by viewModels()
    private lateinit var adapter: WeekAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_week_resource, container, false)
        initView(root)
        initRecyclerView()
        initViewModel()
        return root
    }

    private fun initRecyclerView() {
        adapter = WeekAdapter(arrayListOf(), calendar)
        rvWeek?.adapter = adapter
    }

    private fun initView(view: View) {
        rvWeek = view.findViewById(R.id.rvWeek)
        tv1 = view.findViewById(R.id.tv1)
        tv2 = view.findViewById(R.id.tv2)
        tv3 = view.findViewById(R.id.tv3)
        tv4 = view.findViewById(R.id.tv4)
        tv5 = view.findViewById(R.id.tv5)
        tv6 = view.findViewById(R.id.tv6)
        tv7 = view.findViewById(R.id.tv7)

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
    private fun initViewModel() {
        val firstDay = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)
        viewModel.getResourceDB(firstDay)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null && !it.list.isNullOrEmpty()) {
                //UpdateList
                adapter.updateList(ArrayList(it.list!!))
            }

            if(!hasCallRefreshData) {
                hasCallRefreshData = true
                getAllResource(null)
            }
        })
    }

    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val firstDay = SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time)
        val lastCal: Calendar = Calendar.getInstance()
        lastCal.time = calendar.time
        lastCal.add(Calendar.DAY_OF_MONTH, 6)
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