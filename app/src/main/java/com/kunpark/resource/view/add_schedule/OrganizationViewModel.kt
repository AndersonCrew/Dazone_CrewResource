package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrganizationViewModel: BaseViewModel() {
    private var repository = OrganizationRepository()
    private var _organization: MutableSharedFlow<ArrayList<Organization>?> = MutableSharedFlow()
    val organization = _organization.asSharedFlow()

    private var _nonChanged: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val nonChanged = _nonChanged.asSharedFlow()

    @SuppressLint("SimpleDateFormat")
    fun getDepartmentsMod(params: JsonObject) = viewModelScope.launch (Dispatchers.IO) {
        when (val result = repository.getDepartmentMod(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: ArrayList<Organization> = body["data"] as ArrayList<Organization>

                    if(!data.isNullOrEmpty()) {
                        val params2 = JsonObject()
                        params2.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
                        params2.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes())
                        params2.addProperty("languageCode", Locale.getDefault().language.uppercase(Locale.getDefault()))
                        getDepartments(params2)
                    } else {
                        val params3 = JsonObject()
                        params3.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
                        params3.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes())
                        params3.addProperty("languageCode", Locale.getDefault().language.uppercase(Locale.getDefault()))
                        params3.addProperty("moddate", SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(Date(System.currentTimeMillis()))) //2022-06-29 10:43:23.286

                        getMemberMod(params3)
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

    private fun getMemberMod(params: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = repository.getMemberMod(params)) {
            is Result.Success -> {
                val body: LinkedTreeMap<String, Any> =
                    result.data.response as LinkedTreeMap<String, Any>
                val success = body["success"] as Double
                if (success == 1.0) {
                    val data: ArrayList<User> = body["data"] as ArrayList<User>

                    if(!data.isNullOrEmpty()) {
                        val params2 = JsonObject()
                        params2.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
                        params2.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes())
                        params2.addProperty("languageCode", Locale.getDefault().language.uppercase(Locale.getDefault()))
                        getDepartments(params2)
                    } else {
                        _nonChanged.emit(true)
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

    fun getDepartments(params: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
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
                Log.d("TIMMI", "forEach with ${it.departNo} and thread is ${Thread.currentThread().id}")
                getChild(it, data)

                it.childDepartments.forEach { child ->
                    withContext(Dispatchers.IO) {
                        checkChild(child, data)
                    }

                }
            }
        }

        job.join()
        Log.d("TIMMI", "DONE with ${data.size}")

        DazoneApplication.getInstance().mPref?.putListOrganization(data)

        _organization.emit(data)
    }

    private suspend fun checkChild(organization: Organization, data: ArrayList<Organization>) {
        Log.d("TIMMI", "checkChild with ${organization.departNo} and thread is ${Thread.currentThread().id}")
        getChild(organization, data)

        organization.childDepartments.forEach { child ->
            withContext(Dispatchers.IO) {
                checkChild(child, data)
            }
        }
    }

    private suspend fun getChild(organization: Organization, listOriginal: ArrayList<Organization>) {
        Log.d("TIMMI", "getChild with ${organization.departNo} and thread is ${Thread.currentThread().id}")
        val params = JsonObject()

        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
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
                            Log.d("TIMMI", "Has member with departNo = ${organization.departNo} and thread is ${Thread.currentThread().id}")
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
            Log.d("TIMMI", "updateMembers with ${organization.departNo} and thread is ${Thread.currentThread().id}")
            val it = iterator.next()
            if(it.departNo == organization.departNo) {
                it.childMembers = ArrayList(organization.childMembers)
            } else if(!it.childDepartments.isNullOrEmpty()) {
                updateMembers(organization, it.childDepartments)
            }
        }
    }
}