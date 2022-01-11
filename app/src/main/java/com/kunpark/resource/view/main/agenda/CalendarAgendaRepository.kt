package com.kunpark.resource.view.main.agenda

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.base.BaseResponse
import com.kunpark.resource.model.CalendarAgenda
import com.kunpark.resource.services.Result
import com.kunpark.resource.services.RetrofitFactory

class CalendarAgendaRepository: BaseRepository() {
    fun getResourceDBByMonth(month: String): LiveData<CalendarAgenda>? {
        return db?.getResourceDao()?.getDataAgenda(month)
    }

    fun saveResourceList(srcAgenda: CalendarAgenda) {
        db?.getResourceDao()?.saveCalendarAgenda(srcAgenda)
    }

    suspend fun getResourceDataFromServer(param: JsonObject): Result<BaseResponse> {
        return safeApiCall(call = { RetrofitFactory.getApiService().getAllResource(param)}, errorMessage =  "Cannot get Resource data from server!")
    }
}