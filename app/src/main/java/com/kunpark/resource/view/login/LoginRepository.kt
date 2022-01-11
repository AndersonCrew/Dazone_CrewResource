package com.kunpark.resource.view.login

import android.app.Application
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.User
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class LoginRepository(): BaseRepository() {
    suspend fun login(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().login(param)}, errorMessage =  "Cannot login!")
    }

    suspend fun checkSSL(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.apiServiceNonBaseUrl.checkSSL(params)}, errorMessage =  "Cannot check SSL this domain!")
    }

    suspend fun insertAndroidDevice(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.apiServiceNonBaseUrl.insertAndroidDevice(params)}, errorMessage =  "Cannot check SSL this domain!")
    }

    fun setUser(user: User) {
        db?.getUserDao()?.addUserData(user)
    }
}