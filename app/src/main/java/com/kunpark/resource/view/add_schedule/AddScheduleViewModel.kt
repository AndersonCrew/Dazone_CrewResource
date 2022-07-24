package com.kunpark.resource.view.add_schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.*
import com.kunpark.resource.services.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.ArrayList

class AddScheduleViewModel: BaseViewModel() {
    private var repository = AddScheduleRepository()
    var requestSpecial = InsertResourceRequest()
    var requestDaily = InsertResourceRequest()
    var requestWeekly = InsertResourceRequest()
    var requestMonthly = InsertResourceRequest()
    var requestAnnualy = InsertResourceRequest()
    var insertStateShareFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()

    fun insertResource(params: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = repository.insertResource(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    insertStateShareFlow.emit(true)
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
}