package com.kunpark.resource.view.main.agenda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.event.Event
import com.kunpark.resource.model.AgendaItemType
import com.kunpark.resource.model.CalendarAgenda
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.Config
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.add_schedule.AddScheduleActivity
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity
import java.text.SimpleDateFormat
import java.util.*

class AgendaFragment(private val calendar: Calendar) : BaseFragment() {
    private lateinit var adapter: AgendaAdapter
    private val calendarAgendaViewModel: CalendarAgendaViewModel by viewModels ()
    private var rvAgenda: RecyclerView?= null
    private var calendarAgenda: CalendarAgenda?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_agenda, container, false)
        initView(root)
        initViewModel()
        return root
    }

    private fun initView(rootView: View) {
        rvAgenda = rootView.findViewById(R.id.rvAgenda)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initViewModel() {
        adapter = AgendaAdapter(arrayListOf()) { type, resource ->
            when(type) {
                AgendaItemType.ITEM_RESOURCE -> {
                    DetailScheduleActivity.start(requireActivity() as BaseActivity, resource!!)
                }

                else -> {
                    callActivity(requireActivity() as BaseActivity, AddScheduleActivity::class.java)
                }
            }
        }

        rvAgenda?.layoutManager = GridLayoutManager(requireContext(), Config.COLUMN_AGENDA)
        rvAgenda?.adapter = adapter

        val month = SimpleDateFormat(Constants.MM_YYYY).format(calendar.time)
        calendarAgendaViewModel.getResourceBDByMonth(month)?.observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null) {
                calendarAgenda = it
                if(!calendarAgenda?.list.isNullOrEmpty()) {
                    adapter.updateList(it.list!!)
                    if(!it.hasData) {
                        calendarAgendaViewModel.setListCal(it.list!!)
                        getAllResource(null)
                    }
                }
            } else {
                calendarAgendaViewModel.getListDay(calendar)
            }
        })
    }

    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val params = JsonObject()
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", adapter.listCalendarDto[0].timeString?: "")
        params.addProperty("endDate", adapter.listCalendarDto[adapter.listCalendarDto.size - 1].timeString?: "")
        params.addProperty("rsvnStatus", conditionSearch?.key?: "ALL")
        calendarAgendaViewModel.getAllResource(params)
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)
        it[Event.UPDATE_CONDITION_SEARCH]?.let {
            calendarAgendaViewModel.setListCal(adapter.listCalendarDto)
            getAllResource(it as ConditionSearch)
        }
    }
}