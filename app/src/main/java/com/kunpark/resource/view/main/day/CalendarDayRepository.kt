package com.kunpark.resource.view.main.day

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.CalendarAgenda
import com.kunpark.resource.model.CalendarDay
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class CalendarDayRepository: BaseRepository() {
    fun getResourceDBByDay(day: String): LiveData<CalendarDay>? {
        return db?.getResourceDao()?.getDataDay(day)
    }

    fun saveResourceList(srcAgenda: CalendarDay) {
        db?.getResourceDao()?.saveCalendarDay(srcAgenda)
    }

    suspend fun getResourceDataFromServer(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getAllResource(param)}, errorMessage =  "Cannot get Resource data from server!")
    }
}