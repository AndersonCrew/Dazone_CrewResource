package com.kunpark.resource.view.main.month

import android.annotation.SuppressLint
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
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.Config
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.main.agenda.AgendaAdapter
import com.kunpark.resource.view.main.agenda.CalendarAgendaViewModel
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment(private val calendar: Calendar): BaseFragment(){

    private var adapter: CalendarMonthAdapter?= null
    private var rvCalendarMonth: RecyclerView?= null
    private var tvNoData: TextView?= null
    private val viewModel: CalendarMonthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_month_resource, container, false)
        initView(root)
        initViewModel()
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun initViewModel() {
        adapter = CalendarMonthAdapter(arrayListOf())
        rvCalendarMonth?.adapter = adapter

        val month = SimpleDateFormat(Constants.MM_YYYY).format(calendar.time)
        viewModel.getResourceDB(month)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null) {
                if(!it.list.isNullOrEmpty()) {
                    tvNoData?.visibility = View.GONE
                    adapter?.updateList(it.list!!)
                } else {
                    tvNoData?.visibility = View.VISIBLE
                }
            } else {
                tvNoData?.visibility = View.VISIBLE
                getAllResource(null)
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val params = JsonObject()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val totalDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val calStart = Calendar.getInstance()
        calStart.set(Calendar.YEAR, year)
        calStart.set(Calendar.MONTH, month)
        calStart.set(Calendar.DAY_OF_MONTH, 1)

        val calEnd = Calendar.getInstance()
        calEnd.set(Calendar.YEAR, year)
        calEnd.set(Calendar.MONTH, month)
        calEnd.set(Calendar.DAY_OF_MONTH, totalDayInMonth)
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", SimpleDateFormat(Constants.Format_api_datetime).format(calStart.time))
        params.addProperty("endDate", SimpleDateFormat(Constants.Format_api_datetime).format(calEnd.time))
        params.addProperty("rsvnStatus", conditionSearch?.key?: "ALL")
        viewModel.getAllResource(params, SimpleDateFormat(Constants.MM_YYYY).format(calendar.time))
    }

    private fun initView(root: View?) {
        rvCalendarMonth = root?.findViewById(R.id.rvCalendarMonth)
        tvNoData = root?.findViewById(R.id.tvNoData)
    }
}