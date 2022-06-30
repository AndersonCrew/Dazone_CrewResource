package com.kunpark.resource.view.main.agenda

import android.annotation.SuppressLint
import android.content.Context
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
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.*
import com.kunpark.resource.view.add_schedule.AddScheduleActivity
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AgendaFragment(private val calendar: Calendar) : BaseFragment() {
    private lateinit var adapter: AgendaAdapter
    private val viewModel: CalendarAgendaViewModel by viewModels()
    private var rvAgenda: RecyclerView? = null
    private var list: ArrayList<CalendarDto>? = null
    private var hasCallRefreshData = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_agenda, container, false)
        initView(root)
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initViewModel(context)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onResume() {
        super.onResume()

        if(!list.isNullOrEmpty()) {
            val monthYear = SimpleDateFormat(Constants.MM_YYYY).format(calendar.time)
            viewModel.getResourceBDByMonth(monthYear)
                ?.observe(requireActivity(), androidx.lifecycle.Observer {
                    if (it != null) {
                        checkListData(it.list)
                    }

                    if (!hasCallRefreshData) {
                        hasCallRefreshData = true
                        getAllResource(null)
                    }
                })
        }

    }

    private fun initView(rootView: View) {
        rvAgenda = rootView.findViewById(R.id.rvAgenda)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initViewModel(context: Context) {
        val scope = CoroutineScope(Dispatchers.IO + Job())

        scope.launch {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val totalDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            list = arrayListOf()
            for (i in 0 until totalDay) {
                val calPosition = Calendar.getInstance()
                calPosition.time = calendar.time
                calPosition.set(Calendar.DAY_OF_MONTH, i + 1)
                val calendarDto =
                    CalendarDto(timeString = SimpleDateFormat(Constants.YY_MM_DD).format(calPosition.time))
                list?.add(calendarDto)
            }

            //CHECK SUNDAY
            val calPreviousMonth = Calendar.getInstance()
            calPreviousMonth.time = calendar.time
            for (i in 1 until 7) {
                if (calPreviousMonth.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calPreviousMonth.add(Calendar.DAY_OF_MONTH, -1)
                    val calendarDto = CalendarDto(
                        timeString = SimpleDateFormat(Constants.YY_MM_DD).format(calPreviousMonth.time)
                    )
                    list?.add(0, calendarDto)
                } else {
                    break
                }
            }

            //CHECK SATURDAY
            val calNextMonth = Calendar.getInstance()
            calNextMonth.time = calendar.time
            calNextMonth.set(
                Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            for (i in 1 until 7) {
                if (calNextMonth.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    calNextMonth.add(Calendar.DAY_OF_MONTH, 1)

                    val calendarDto = CalendarDto(
                        timeString = SimpleDateFormat(Constants.YY_MM_DD).format(calNextMonth.time)
                    )
                    list?.add(calendarDto)
                } else {
                    break
                }
            }

            withContext(Dispatchers.Main) {
                adapter = AgendaAdapter(list!!) { type, resource ->
                    when (type) {
                        AgendaItemType.ITEM_RESOURCE -> {
                            DetailScheduleActivity.start(
                                requireActivity() as BaseActivity,
                                resource!!
                            )
                        }

                        else -> {
                            callActivity(
                                requireActivity() as BaseActivity,
                                AddScheduleActivity::class.java
                            )
                        }
                    }
                }

                rvAgenda?.layoutManager = GridLayoutManager(context, Config.COLUMN_AGENDA)
                rvAgenda?.adapter = adapter

                //Fetch Data Resource
                //Get Data Resource
                if(isResumed) {
                    val monthYear = SimpleDateFormat(Constants.MM_YYYY).format(calendar.time)
                    viewModel.getResourceBDByMonth(monthYear)
                        ?.observe(requireActivity(), androidx.lifecycle.Observer {
                            if (it != null) {
                                checkListData(it.list)
                            }

                            if (!hasCallRefreshData) {
                                hasCallRefreshData = true
                                getAllResource(null)
                            }
                        })
                }

            }

        }
    }

    private fun checkListData(listResource: List<CalendarDto>?) {
        list?.listIterator()?.next()?.listResource?.clear()
        if(!list.isNullOrEmpty() && !listResource.isNullOrEmpty()) {
            for(calendarDto in listResource) {
                for(calDto in list!!) {
                    if(calendarDto.timeString == calDto.timeString) {
                        calDto.listResource.clear()
                        calDto.listResource.addAll(calendarDto.listResource)
                    }
                }
            }

            adapter.notifyDataSetChanged()
        }

    }

    private fun getAllResource(conditionSearch: ConditionSearch?) {
        val params = JsonObject()
        params.addProperty(
            "sessionId",
            DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, "")
        )
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("startDate", adapter.listCalendarDto[0].timeString ?: "")
        params.addProperty(
            "endDate",
            adapter.listCalendarDto[adapter.listCalendarDto.size - 1].timeString ?: ""
        )
        params.addProperty("rsvnStatus", conditionSearch?.key ?: "ALL")
        viewModel.getAllResource(params, calendar)
    }

    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)
    }
}