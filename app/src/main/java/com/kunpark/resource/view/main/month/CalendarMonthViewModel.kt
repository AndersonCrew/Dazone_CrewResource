package com.kunpark.resource.view.main.month

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.CalendarMonth
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.model.Resource
import com.kunpark.resource.services.Result
import com.kunpark.resource.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class CalendarMonthViewModel : BaseViewModel() {

    private val repository = CalendarMonthRepository()

    fun getResourceDB(month: String): LiveData<CalendarMonth>? {
        return repository.getResourceDB(month)
    }

    fun getAllResource(params: JsonObject, month: String) =
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            when (val result = repository.getResourceDataFromServer(params)) {
                is Result.Success -> {
                    val body: LinkedTreeMap<String, Any> =
                        result.data.response as LinkedTreeMap<String, Any>
                    val success = body["success"] as Double
                    if (success == 1.0) {
                        val data: Any = body["data"] ?: return@launch

                        val json = Gson().toJson(data)
                        val list = Gson().fromJson<List<Resource>>(
                            json,
                            object : TypeToken<List<Resource>>() {}.type
                        )
                        list?.let { checkAddListResource(list, month) }
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
    private fun checkAddListResource(
        list: List<Resource>,
        month: String
    ) {
        uiScope.launch(Dispatchers.IO) {
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat(Constants.MM_YYYY).parse(month)
            val countDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val listCalendarDto: ArrayList<CalendarDto> = arrayListOf()
            for(i in 1 until countDay) {
                cal.set(Calendar.DATE, 1)
                cal.add(Calendar.DATE, i - 1)
                val startDate = SimpleDateFormat(Constants.Format_api_datetime).format(cal.time)
                val resources = list.filter { src -> src.startStr == startDate }

                if(!resources.isNullOrEmpty()) {
                    val calendarDto = CalendarDto(i, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR), startDate)
                    calendarDto.listResource.addAll(resources)
                    listCalendarDto.add(calendarDto)
                }
            }

            val calendarMonth = CalendarMonth(month, listCalendarDto)
            repository.saveCalendarMonth(calendarMonth)
        }
    }
}