package com.kunpark.resource.view.main.month

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.CalendarMonth
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class CalendarMonthRepository: BaseRepository() {
    fun getResourceDB(month: String): LiveData<CalendarMonth>? {
        return db?.getResourceDao()?.getDataMonth(month)
    }

    fun saveCalendarMonth(calendarMonth: CalendarMonth) {
        db?.getResourceDao()?.saveCalendarMonth(calendarMonth)
    }

    suspend fun getResourceDataFromServer(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getAllResource(param)}, errorMessage =  "Cannot get Resource data from server!")
    }
}