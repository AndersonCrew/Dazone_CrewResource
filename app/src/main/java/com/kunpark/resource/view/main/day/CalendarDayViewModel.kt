package com.kunpark.resource.view.main.day

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

class CalendarDayViewModel : BaseViewModel() {

    private val repository = CalendarDayRepository()

    fun getResourceDB(day: String): LiveData<CalendarDay>? {
        return repository.getResourceDBByDay(day)
    }

    fun getAllResource(params: JsonObject, day: String) = viewModelScope.launch(Dispatchers.IO) {
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

                    if (!list.isNullOrEmpty()) {
                        checkAddListResource(list, day)
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
    private fun checkAddListResource(list: List<Resource>, day: String) {
        uiScope.launch(Dispatchers.IO) {
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat(Constants.DD_MM_YYYY).parse(day)
            val listCalendarDto: ArrayList<CalendarDto> = arrayListOf()

            for(i in 0 until 24) {
                val resources: ArrayList<Resource> = arrayListOf()
                for(resource in list) {
                    val startHour = Integer.parseInt(resource.startTime?.split(":")?.get(0))
                    val endHour = Integer.parseInt(resource.endTime?.split(":")?.get(0))
                    if(i in startHour..endHour) {
                        resources.add(resource)
                    }
                }

                if(!resources.isNullOrEmpty()) {
                    val startHour = if(i.toString().length == 1) "0$i" else i
                    val calendarDto = CalendarDto(
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.YEAR),
                        startHour.toString()
                    )
                    calendarDto.listResource.addAll(resources)
                    listCalendarDto.add(calendarDto)
                }
            }

            val calendarDay = CalendarDay(day, listCalendarDto)
            repository.saveResourceList(calendarDay)
        }
    }
}