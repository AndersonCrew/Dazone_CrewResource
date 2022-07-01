package com.kunpark.resource.view.main.month

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.model.CalendarAgenda
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.Config
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity
import com.kunpark.resource.view.main.agenda.AgendaAdapter
import com.kunpark.resource.view.main.agenda.CalendarAgendaViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonthFragment(private val calendar: Calendar): BaseFragment(){

    private var rvCalendarMonth: RecyclerView?= null
    private val viewModel: CalendarMonthViewModel by viewModels()
    private var hasCallAPI = false
    private lateinit var adapter: CalendarMonthAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_month_resource, container, false)
        initRecyclerView(root)
        initView()
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val params = JsonObject()
        val totalDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)


        val calEnd = Calendar.getInstance()
        calEnd.time = calendar.time
        calEnd.set(Calendar.DAY_OF_MONTH, totalDayInMonth)
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", SimpleDateFormat(Constants.Format_api_datetime).format(calendar.time))
        params.addProperty("endDate", SimpleDateFormat(Constants.Format_api_datetime).format(calEnd.time))
        params.addProperty("rsvnStatus", conditionSearch?.key?: "ALL")
        viewModel.getAllResource(params, calendar)
    }

    private fun initRecyclerView(root: View?) {
        rvCalendarMonth = root?.findViewById(R.id.rvCalendarMonth)

        adapter = CalendarMonthAdapter(arrayListOf()) {
            val intent = Intent(requireContext(), DetailScheduleActivity::class.java)
            intent.putExtra(Constants.RESOURCE, it)
            requireActivity().startActivity(intent)
        }

        rvCalendarMonth?.adapter = adapter
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() {
        val month = SimpleDateFormat(Constants.MM_YYYY).format(calendar.time)
        viewModel.getResourceDB(month)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null && !it.list.isNullOrEmpty()) {
               //TODO Update list
                adapter.updateList(ArrayList(it.list!!))
            }

            if(!hasCallAPI) {
                hasCallAPI = true
                getAllResource(null)
            }
        })
    }
}