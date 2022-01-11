package com.kunpark.resource.view.main

import android.app.Application
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.model.User
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class HomeRepository(): BaseRepository() {
    fun getUserInfo(): LiveData<User>? {
        return db?.getUserDao()?.getUser()
    }

    fun getConditionSearch(): LiveData<List<ConditionSearch>>? {
        return db?.getConditionSearchDao()?.getData()
    }

    fun saveConditionSearch(conditionSearchList: List<ConditionSearch>) {
        db?.getConditionSearchDao()?.addMoreData(conditionSearchList)
    }

    suspend fun getConditionSearch(params: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getConditionSearch(params)}, errorMessage =  "Cannot get condition search!")
    }
}