package com.kunpark.resource.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kunpark.resource.utils.DazoneApplication
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers


abstract class BaseFragment: Fragment() {

    var eventDisposable: CompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DazoneApplication.eventBus
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onEventReceive(it) }
            .addTo(eventDisposable)

    }

    open fun onEventReceive(it: Map<String, Any?>) {

    }

    open fun callActivity(baseBaseActivity: BaseActivity, cls: Class<*>?) {
        val newIntent = Intent(baseBaseActivity, cls)
        newIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(newIntent)
    }

}