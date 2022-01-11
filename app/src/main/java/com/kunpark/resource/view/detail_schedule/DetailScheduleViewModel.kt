package com.kunpark.resource.view.detail_schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.*
import com.kunpark.resource.services.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailScheduleViewModel: BaseViewModel() {
    private val repository = DetailScheduleRepository()
    var resourceLiveDate: MutableLiveData<Resource> = MutableLiveData()
    var participantLiveDate: MutableLiveData<List<Participant>> = MutableLiveData()
    var notificationLiveDate: MutableLiveData<List<Notification>> = MutableLiveData()

    init {
        resourceLiveDate.value = null
        participantLiveDate.value = null
    }

    fun getDetailViewModel(params: JsonObject) = viewModelScope.launch(
        Dispatchers.IO) {
        when (val result = repository.getResourceDataFromServer(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: LinkedTreeMap<String, Any> = body["data"] as LinkedTreeMap<String, Any>?
                        ?: return@launch
                    val resourceLinkTree = data["Detail"]
                    val participantsLinkTree = data["ResourceParticipants"]
                    val notificationLinkTree = data["ResourceNotifications"]
                    val gSon = Gson()

                    val resource = gSon.fromJson(gSon.toJson(resourceLinkTree), Resource::class.java)
                    val participants = gSon.fromJson<List<Participant>>(gSon.toJson(participantsLinkTree), object : TypeToken<List<Participant>>() {}.type)
                    val notifications = gSon.fromJson<List<Notification>>(gSon.toJson(notificationLinkTree), object : TypeToken<List<Notification>>() {}.type)
                    resourceLiveDate.postValue(resource)
                    participantLiveDate.postValue(participants)
                    notificationLiveDate.postValue(notifications)
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