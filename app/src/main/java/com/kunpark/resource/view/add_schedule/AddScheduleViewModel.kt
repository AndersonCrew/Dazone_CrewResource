package com.kunpark.resource.view.add_schedule

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.Organization
import com.kunpark.resource.services.Result
import kotlinx.coroutines.launch
import java.util.ArrayList

class AddScheduleViewModel: BaseViewModel() {
    private var repository = AddScheduleRepository()
}