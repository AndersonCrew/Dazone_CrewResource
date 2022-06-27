package com.kunpark.resource.view.main.week

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
import com.kunpark.resource.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class CalendarWeekViewModel : BaseViewModel() {

    private val repository = CalendarWeekRepository()

    fun getResourceDB(day: String): LiveData<CalendarWeek>? {
        return repository.getResourceDBByDay(day)
    }

    fun getAllResource(params: JsonObject, localDate: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
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
                        checkAddListResource(list, localDate)
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
    private fun checkAddListResource(list: List<Resource>, localDate: LocalDate) {
        uiScope.launch(Dispatchers.IO) {



        }
    }
}