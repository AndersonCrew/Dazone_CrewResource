package com.kunpark.resource.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.model.User
import com.kunpark.resource.services.Result
import kotlinx.coroutines.launch
import java.util.*


class HomeViewModel: BaseViewModel() {
    private val repository = HomeRepository()

    fun getUser(): LiveData<User>? {
        return repository.getUserInfo()
    }

    fun getConditionSearchDB(): LiveData<List<ConditionSearch>>? {
        return repository.getConditionSearch()
    }


    fun getConditionSearch(params: JsonObject) = uiScope.launch {
        when (val result = repository.getConditionSearch(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> = result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if(success == 1.0) {
                    val conditionSearchList: ArrayList<ConditionSearch> = body["data"] as ArrayList<ConditionSearch>

                    val gson = Gson()
                    val json = gson.toJson(conditionSearchList)
                    val list = gson.fromJson<List<ConditionSearch>>(json, object : TypeToken<List<ConditionSearch>>() {}.type)
                    repository.saveConditionSearch(list)
                } else {
                    val error: LinkedTreeMap<String, Any> = body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }
            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }
}