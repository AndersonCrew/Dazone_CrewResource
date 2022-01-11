package com.kunpark.resource.utils

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
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class UtilsViewModel: BaseViewModel() {
    private val repository = UtilsRepository()

    fun getDeviceNotificationSetting(): LiveData<List<DevicesNotification>>? {
        return repository.getDeviceNotificationSettings()
    }

    fun getTimeNotificationSetting(): LiveData<List<TimesNotification>>? {
        return repository.getTimeNotificationSettings()
    }

    fun getDeviceNotificationFromServer(params: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = repository.getDeviceNotificationSettingsFromServer(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> = result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if(success == 1.0) {
                    val data: ArrayList<DevicesNotification> = body["data"] as ArrayList<DevicesNotification>

                    val gson = Gson()
                    val json = gson.toJson(data)
                    val list = gson.fromJson<List<DevicesNotification>>(json, object : TypeToken<List<DevicesNotification>>() {}.type)
                    repository.saveDeviceNotification(list)

                } else {
                    val error: LinkedTreeMap<String, Any> = body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }

            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }

    fun getTimeNotificationFromServer(params: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = repository.getTimeNotificationSettingsFromServer(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> = result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if(success == 1.0) {
                    val data: ArrayList<TimesNotification> = body["data"] as ArrayList<TimesNotification>

                    val gson = Gson()
                    val json = gson.toJson(data)
                    val list = gson.fromJson<List<TimesNotification>>(json, object : TypeToken<List<TimesNotification>>() {}.type)
                    repository.saveTimeNotification(list)


                } else {
                    val error: LinkedTreeMap<String, Any> = body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }

            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }

    fun getOrganizations(): LiveData<List<Organization>>? {
        return repository.getOrganizationDB()
    }

    fun getOrganizationFromServer(params: JsonObject) = uiScope.launch {
        when (val result = repository.getOrganization(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> = result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if(success == 1.0) {
                    val organizations: java.util.ArrayList<Organization> = body["data"] as java.util.ArrayList<Organization>
                    val gSon = Gson()
                    val json = gSon.toJson(organizations)
                    val list = gSon.fromJson<List<Organization>>(json, object : TypeToken<List<Organization>>() {}.type)
                    repository.saveOrganization(list)
                } else {
                    val error: LinkedTreeMap<String, Any> = body["error"] as LinkedTreeMap<String, Any>
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