package com.kunpark.resource.services

import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.utils.Config
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DazoneService {
    @POST(Config.LOGIN_V5)
    suspend fun login(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.CHECK_SSL)
    suspend fun checkSSL(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.INSERT_ANDROID_DEVICE)
    suspend fun insertAndroidDevice(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_CONDITION_SEARCH)
    suspend fun getConditionSearch(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_ALL_RESOURCE)
    suspend fun getAllResource(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_DEVICE_NOTIFICATION_SETTINGS)
    suspend fun getDeviceNotificationSettings(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_TIME_NOTIFICATION_SETTINGS)
    suspend fun getTimeNotificationSettings(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_ORGANIZATION)
    suspend fun getOrganization(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_DETAIL_RESOURCE)
    suspend fun getDetailResource(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_DEPARTMENTS)
    suspend fun getDepartments(@Body param: JsonObject): Response<BaseResponse>

    @POST(Config.GET_USER_BY_DEPARTMENT)
    suspend fun getUserByDepartmentNo(@Body param: JsonObject): Response<BaseResponse>



}
