package com.kunpark.resource.view.detail_schedule

import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class DetailScheduleRepository: BaseRepository() {
    suspend fun getResourceDataFromServer(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getDetailResource(param)}, errorMessage =  "Cannot get Detail Resource data from server!")
    }
}