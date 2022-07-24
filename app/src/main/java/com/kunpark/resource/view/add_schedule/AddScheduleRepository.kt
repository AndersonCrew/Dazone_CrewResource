package com.kunpark.resource.view.add_schedule

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.CalendarWeek
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.OrganizationChart
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class AddScheduleRepository: BaseRepository() {
    suspend fun insertResource(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().insertResource(param)}, errorMessage =  "Cannot insert new resource!")
    }

}