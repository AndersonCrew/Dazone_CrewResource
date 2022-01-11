package com.kunpark.resource.view.settings

import androidx.lifecycle.LiveData
import com.kunpark.resource.base.BaseViewModel
import com.kunpark.resource.model.User

class SettingViewModel: BaseViewModel() {
    private val repository = SettingRepository()

    fun getUser(): LiveData<User>? {
        return repository.getUserInfo()
    }
}