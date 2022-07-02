package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.*
import com.kunpark.resource.services.Result
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrganizationViewModel: BaseViewModel() {
    private var repository = OrganizationRepository()
    fun getOrganization(): LiveData<OrganizationChart>? {
        return repository.getOrganization()
    }

    fun getDepartments(params: JsonObject) = viewModelScope.launch(
        Dispatchers.IO) {
        when (val result = repository.getDepartments(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: ArrayList<Organization> = body["data"] as ArrayList<Organization>
                    val gson = Gson()
                    val json = gson.toJson(data)

                    val list = gson.fromJson<ArrayList<Organization>>(
                        json,
                        object : TypeToken<ArrayList<Organization>>() {}.type
                    )
                    if(!list.isNullOrEmpty()) {
                        getChildData(list)
                    }

                } else {
                    val error: LinkedTreeMap<String, Any> =
                        body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }
            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private suspend fun getChildData(data: ArrayList<Organization>) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            data.forEach {
                Log.d("TIMMI", "forEach with ${it.name} and thread is ${Thread.currentThread().id}")
                getChild(it, data)

                it.childDepartments.forEach { child ->
                    withContext(Dispatchers.IO) {
                        getChild(child, data)
                    }

                }
            }
        }

        job.join()
        Log.d("TIMMI", "DONE")
        val organizationChart = OrganizationChart()
        organizationChart.timeUpdated = SimpleDateFormat(Constants.YY_MM_DD).format(Date(System.currentTimeMillis()))
        organizationChart.list = ArrayList(data)
        repository.saveOrganization(organizationChart)
    }

    private suspend fun getChild(organization: Organization, listOriginal: ArrayList<Organization>) {
        Log.d("TIMMI", "getChild with ${organization.name} and thread is ${Thread.currentThread().id}")
        val params = JsonObject()

        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(
            Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("departNo", organization.departNo)
        when (val result = repository.getUserByDepartmentNo(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: ArrayList<User> = body["data"] as ArrayList<User>

                    val gson = Gson()
                    val json = gson.toJson(data)

                    val list = gson.fromJson<ArrayList<User>>(
                        json,
                        object : TypeToken<ArrayList<User>>() {}.type
                    )

                    if(!list.isNullOrEmpty()) {
                        withContext(Dispatchers.IO) {
                            organization.childMembers = ArrayList(list)
                            updateMembers(organization, listOriginal)
                        }

                    }

                } else {
                    val error: LinkedTreeMap<String, Any> =
                        body["error"] as LinkedTreeMap<String, Any>
                    val message = error["message"] as String
                    errorMessage.postValue(message)
                }
            }

            is Result.Error -> {
                errorMessage.postValue(result.exception)
            }
        }
    }

    private fun updateMembers(organization: Organization, data: ArrayList<Organization>)  {
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            Log.d("TIMMI", "updateMembers with ${organization.name} and thread is ${Thread.currentThread().id}")
            val it = iterator.next()
            if(it.departNo == organization.departNo) {
                it.childMembers = ArrayList(organization.childMembers)
            } else if(!it.childDepartments.isNullOrEmpty()) {
                updateMembers(organization, it.childDepartments)
            }
        }

    }
}