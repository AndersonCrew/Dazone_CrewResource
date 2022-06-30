package com.kunpark.resource.view.main.day

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.R
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
import kotlin.collections.HashMap

class CalendarDayViewModel : BaseViewModel() {

    private val repository = CalendarDayRepository()

    fun getResourceDB(day: String): LiveData<CalendarDay>? {
        return repository.getResourceDBByDay(day)
    }

    fun getAllResource(params: JsonObject, calendar: Calendar) = viewModelScope.launch(Dispatchers.IO) {
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
                        checkAddListResource(list, calendar)
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
    private fun checkAddListResource(list: List<Resource>, calendar: Calendar) {
        uiScope.launch(Dispatchers.IO) {

            val hashMap: HashMap<String, ArrayList<Resource>> = hashMapOf()
           list.forEach { resource ->
               val startTime = resource.startTime?.split(":")?.get(0)
               val strTime = when {
                   resource.allDay == true -> "All Day"
                   startTime?.length == 1 -> "0$startTime:00"
                   else -> "$startTime:00"
               }

               if(hashMap.containsKey(strTime)) {
                   hashMap[strTime]?.add(resource)
               } else {
                   val listResource: ArrayList<Resource> = arrayListOf()
                   listResource.add(resource)
                   hashMap[strTime] = listResource
               }
           }

            val listCalendarDto: ArrayList<CalendarDto> = arrayListOf()
            hashMap.forEach {
                val calendarDto = CalendarDto(timeString = it.key)
                calendarDto.listResource.clear()
                calendarDto.listResource.addAll(it.value)
                listCalendarDto.add(calendarDto)
            }

            val time = SimpleDateFormat(Constants.YY_MM_DD).format(calendar.time)
            val calendarDay = CalendarDay(time, listCalendarDto)
            repository.saveResourceList(calendarDay)
        }
    }
}