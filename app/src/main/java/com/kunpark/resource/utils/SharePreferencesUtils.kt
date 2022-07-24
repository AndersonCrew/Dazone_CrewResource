package com.kunpark.resource.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.model.Organization

class SharePreferencesUtils constructor(context: Context){
    private var mPref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun logout() {
        mPref.edit().remove(Constants.ACCESS_TOKEN).apply()
    }

    /**Get Set Boolean value*/
    fun setBoolean(key: String, value : Boolean) {
        mPref?.edit()?.putBoolean(key, value)?.apply()
    }
    fun getBoolean(key: String, valueDefault: Boolean): Boolean {
        return mPref.getBoolean(key, valueDefault)
    }

    /**Get Set Int value*/
    fun setInt(key: String, value : Int) {
        mPref?.edit()?.putInt(key, value)?.apply()
    }
    fun getInt(key: String, defaultValue: Int): Int {
        return mPref?.getInt(key, defaultValue) ?: defaultValue
    }

    /**Get Set String value*/
    fun setString(key: String, value: String) {
        mPref?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return mPref?.getString(key, defaultValue) ?: defaultValue
    }
    fun putListOrganization(list: ArrayList<Organization>) {
        val gson = Gson()
        val arrayData = gson.toJson(list)
        setString(Constants.ORGANIZATION_TREE, arrayData)
    }

    fun getListOrganization(): List<Organization>? {
        val userListType = object : TypeToken<ArrayList<Organization?>?>() {}.type
        return Gson().fromJson<ArrayList<Organization>>(
            getString(Constants.ORGANIZATION_TREE, ""),
            userListType
        )
    }

}