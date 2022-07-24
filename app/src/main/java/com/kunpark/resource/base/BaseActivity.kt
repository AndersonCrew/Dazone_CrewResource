package com.kunpark.resource.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.dialog.DialogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers


abstract class BaseActivity: AppCompatActivity() {
    var eventDisposable: CompositeDisposable = CompositeDisposable()
    private var progressBar: Dialog? = null

    open fun showProgressDialog() {
        if(progressBar == null) {
            progressBar = DialogUtil(this).getProgressDialog()
        }
        progressBar?.show()
    }

    open fun dismissProgressDialog() {
        if(progressBar?.isShowing == true) {
            progressBar?.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(progressBar == null) {
            progressBar = DialogUtil(this).getProgressDialog()
        }

        DazoneApplication.eventBus
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onEventReceive(it) }
            .addTo(eventDisposable)

        initView()
        initEvent()
        initViewModel()
    }

    open fun onEventReceive(it: Map<String, Any?>) {

    }

    open fun callActivity(cls: Class<*>?) {
        val newIntent = Intent(this, cls)
        newIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(newIntent)
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }

    abstract fun initView()
    abstract fun initEvent()
    abstract fun initViewModel()
}