package com.kunpark.resource.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.kunpark.resource.R
import com.kunpark.resource.utils.DazoneApplication
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers


abstract class BaseActivity: AppCompatActivity() {
    var eventDisposable: CompositeDisposable = CompositeDisposable()
    private var mProgressDialog: Dialog? = null
    open fun showProgressDialog() {
        if (null == mProgressDialog || !mProgressDialog!!.isShowing) {
            mProgressDialog = Dialog(this, R.style.ProgressCircleDialog)
            mProgressDialog!!.setTitle(getString(R.string.loading_content))
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.setOnCancelListener(null)
            mProgressDialog!!.addContentView(
                ProgressBar(this),
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            mProgressDialog!!.show()
        }
    }

    open fun dismissProgressDialog() {
        if (null != mProgressDialog && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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