package com.kunpark.resource.view.add_schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.Notification
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.Participant
import com.kunpark.resource.model.Resource
import com.kunpark.resource.services.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class AddScheduleViewModel: BaseViewModel() {
    private var repository = AddScheduleRepository()

    fun getDepartments(params: JsonObject) = viewModelScope.launch(
        Dispatchers.IO) {
        when (val result = repository.getDepartments(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: LinkedTreeMap<String, Any> = body["data"] as LinkedTreeMap<String, Any>?
                        ?: return@launch

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