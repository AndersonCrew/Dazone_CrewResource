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

    fun getResourceBDByMonth(month: String): LiveData<CalendarAgenda>? {
        return repository.getResourceDBByMonth(month)
    }

    fun getAllResource(params: JsonObject, cal: Calendar) = viewModelScope.launch(Dispatchers.IO) {
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
                        checkAddListResource(list, cal)
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
    private fun checkAddListResource(list: List<Resource>, cal: Calendar) {
        uiScope.launch(Dispatchers.IO) {

            val hashMap: HashMap<String, ArrayList<Resource>> = hashMapOf()
            for(resource in list) {
                if(!hashMap.containsKey(resource.startStr)) {
                    val resources: ArrayList<Resource> = arrayListOf()
                    resources.add(resource)
                    hashMap[resource.startStr!!] = resources
                } else {
                    hashMap[resource.startStr!!]?.add(resource)
                }
            }

            val listCalendarDto: ArrayList<CalendarDto> = arrayListOf()
            for ((key, value) in hashMap) {
                val calendarDto = CalendarDto(timeString = key)
                calendarDto.listResource.clear()
                calendarDto.listResource.addAll(value)
                listCalendarDto.add(calendarDto)
            }

            val calendarList = CalendarAgenda(SimpleDateFormat(Constants.MM_YYYY).format(cal.time), listCalendarDto)
            repository.saveResourceList(calendarList)
        }
    }
}