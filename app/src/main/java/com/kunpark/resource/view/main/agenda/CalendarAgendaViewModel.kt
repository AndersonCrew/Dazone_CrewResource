package com.kunpark.resource.view.main.agenda

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.*
import com.kunpark.resource.services.Result
import com.kunpark.resource.utils.Config
import com.kunpark.resource.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAgendaViewModel : BaseViewModel() {

    private val repository = CalendarAgendaRepository()
    private val listCal: ArrayList<CalendarDto> = arrayListOf()

    fun setListCal(list: List<CalendarDto>) {
        listCal.clear()
        listCal.addAll(list)
    }

    @SuppressLint("SimpleDateFormat")
    fun getListDay(cal: Calendar) = viewModelScope.launch(Dispatchers.IO) {
        val listDay: ArrayList<CalendarDto> = arrayListOf()
        val firstDayOfCurrentMonth: Int = cal.get(Calendar.DAY_OF_WEEK)
        val totalDayInMonth: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (firstDayOfCurrentMonth != Calendar.SUNDAY) {
            val priYear = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            if (month == 0) {
                priYear.set(year - 1, 11, 1)
            } else {
                priYear.set(year, month - 1, 1)
            }

            val totalDayInMonthPri: Int = priYear.getActualMaximum(Calendar.DAY_OF_MONTH)
            for (i in firstDayOfCurrentMonth - 2 downTo 0) {
                priYear.set(Calendar.DAY_OF_MONTH, totalDayInMonthPri - i)
                listDay.add(
                    CalendarDto(
                        totalDayInMonthPri - i,
                        priYear.get(Calendar.MONTH) + 1,
                        priYear.get(Calendar.YEAR),
                        SimpleDateFormat(Constants.Format_api_datetime).format(priYear.time)
                    )
                )
            }
        }

        for (i in 1 until totalDayInMonth + 1) {
            cal.set(Calendar.DAY_OF_MONTH, i)
            listDay.add(
                CalendarDto(
                    i,
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.YEAR),
                    SimpleDateFormat(Constants.Format_api_datetime).format(cal.time)
                )
            )
        }

        if (listDay.size < Config.COUNT_DAY_OF_AGENDA) {
            val countLack = Config.COUNT_DAY_OF_AGENDA - listDay.size
            val nextYear = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            if (month == 12) {
                nextYear.set(year + 1, 0, 1)
            } else {
                nextYear.set(year, month + 1, 1)
            }

            for (i in 1 until countLack + 1) {
                nextYear.set(Calendar.DAY_OF_MONTH, i)
                listDay.add(
                    CalendarDto(
                        i,
                        nextYear.get(Calendar.MONTH) + 1,
                        nextYear.get(Calendar.YEAR),
                        SimpleDateFormat(Constants.Format_api_datetime).format(nextYear.time)
                    )
                )
            }
        }

        repository.saveResourceList(
            CalendarAgenda(
                SimpleDateFormat(Constants.MM_YYYY).format(cal.time),
                listDay
            )
        )
    }


    fun getResourceBDByMonth(month: String): LiveData<CalendarAgenda>? {
        return repository.getResourceDBByMonth(month)
    }

    fun getAllResource(params: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = repository.getResourceDataFromServer(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: Any = body["data"] ?: return@launch

                    val gson = Gson()
                    val json = gson.toJson(data)

                    val list = gson.fromJson<List<Resource>>(
                        json,
                        object : TypeToken<List<Resource>>() {}.type
                    )
                    if (list != null) {
                        checkAddListResource(list)
                    }
                } else {
                    val error: LinkedTreeMap<String, Any> =
                        body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }

            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkAddListResource(list: List<Resource>) {
        uiScope.launch(Dispatchers.IO) {

            val listTemp: ArrayList<CalendarDto> = arrayListOf()
            listTemp.addAll(listCal)

            val iterate = listTemp.listIterator()
            while (iterate.hasNext()) {
                val oldValue = iterate.next()
                if (oldValue.listResource.size > 0) {
                    oldValue.listResource = arrayListOf()
                }
            }

            if (list.isNotEmpty()) {
                for (listCalItem in listTemp) {
                    val resources = list.filter { resource ->resource.startStr == listCalItem.timeString }

                    if (!resources.isNullOrEmpty()) {
                        listCalItem.listResource.addAll(resources)
                    }
                }
            }

            val dateTime = SimpleDateFormat(Constants.Format_api_datetime).parse(
                listTemp[listTemp.size / 2].timeString ?: ""
            )
            val calendarList =
                CalendarAgenda(SimpleDateFormat(Constants.MM_YYYY).format(dateTime), listTemp)
            calendarList.hasData = true
            repository.saveResourceList(calendarList)
        }
    }
}