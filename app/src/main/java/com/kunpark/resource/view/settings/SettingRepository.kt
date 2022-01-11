package com.kunpark.resource.view.settings

import androidx.lifecycle.LiveData
import com.kunpark.resource.base.BaseRepository
import com.kunpark.resource.model.User

class SettingRepository: BaseRepository() {
    fun getUserInfo(): LiveData<User>? {
        return db?.getUserDao()?.getUser()
    }
}