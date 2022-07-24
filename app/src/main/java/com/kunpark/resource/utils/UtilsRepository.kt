package com.kunpark.resource.utils

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.*
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class UtilsRepository: BaseRepository() {
    suspend fun getDeviceNotificationSettingsFromServer(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getDeviceNotificationSettings(params)}, errorMessage =  "Cannot get Notification Settings!")
    }

    suspend fun getTimeNotificationSettingsFromServer(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getTimeNotificationSettings(params)}, errorMessage =  "Cannot get Notification Settings!")
    }

    fun getDeviceNotificationSettings(): LiveData<List<DevicesNotification>>? {
        return db?.getUtilsDao()?.getDeviceNotificationSetting()
    }

    fun getTimeNotificationSettings(): LiveData<List<TimesNotification>>? {
        return db?.getUtilsDao()?.getTimeNotificationSetting()
    }

    fun saveTimeNotification(timesNotification: List<TimesNotification>) {
        db?.getUtilsDao()?.addMoreTimeNotifications(timesNotification)
    }

    fun saveDeviceNotification(devicesNotification: List<DevicesNotification>) {
        db?.getUtilsDao()?.addMoreDataDeviceNotifications(devicesNotification)
    }

    suspend fun getOrganization(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getOrganization(params)}, errorMessage =  "Cannot get Organization!")
    }

    fun getResourceTreeDB(): LiveData<ResourceTree>? {
        return db?.getUtilsDao()?.getResourceTree()
    }

    fun saveResourceTreeDB(resourceTree: ResourceTree) {
        db?.getUtilsDao()?.addResourceTree(resourceTree)
    }
}