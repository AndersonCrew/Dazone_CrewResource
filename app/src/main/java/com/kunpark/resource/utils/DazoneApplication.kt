package com.kunpark.resource.utils

import android.app.Application
import io.reactivex.rxjava3.subjects.PublishSubject

class DazoneApplication: Application() {
    companion object {
        private var instance: DazoneApplication?= null
        val eventBus: PublishSubject<HashMap<String, Any?>> = PublishSubject.create()
        @JvmStatic
        fun getInstance(): DazoneApplication {
            if(instance == null) {
                instance = DazoneApplication()
            }
            return instance!!
        }
    }
    var mPref: SharePreferencesUtils? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        mPref = SharePreferencesUtils(this)
    }


}