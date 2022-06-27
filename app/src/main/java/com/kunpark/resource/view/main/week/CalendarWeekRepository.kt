package com.kunpark.resource.view.main.week

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.CalendarAgenda
import com.kunpark.resource.model.CalendarDay
import com.kunpark.resource.model.CalendarWeek
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class CalendarWeekRepository: BaseRepository() {
    fun getResourceDBByDay(day: String): LiveData<CalendarWeek>? {
        return db?.getResourceDao()?.getDataWeek(day)
    }

    fun saveResourceList(srcAgenda: CalendarWeek) {
        db?.getResourceDao()?.saveCalendarWeek(srcAgenda)
    }

    suspend fun getResourceDataFromServer(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getAllResource(param)}, errorMessage =  "Cannot get Resource data from server!")
    }
}