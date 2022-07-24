package com.kunpark.resource.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.model.DevicesNotification
import com.kunpark.resource.model.Notification
import com.kunpark.resource.model.ResourceNotification
import com.kunpark.resource.model.TimesNotification
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.UtilsViewModel
import kotlinx.android.synthetic.main.layout_notifications_setting.view.*
import java.util.*
import kotlin.collections.ArrayList


class NotificationSettings : ConstraintLayout, LifecycleObserver {

    constructor(mContext: Context) : super(mContext)
    private var devices: List<DevicesNotification>? = null
    private var times: List<TimesNotification>? = null
    private var viewModel: UtilsViewModel?= null

    constructor(mContext: Context, attrs: AttributeSet) : super(mContext, attrs) {
        initAttributes(attrs)
    }

    constructor(mContext: Context, attrs: AttributeSet, defStyleAttr: Int) : super(mContext, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_notifications_setting, this, false)
        addView(view)
    }

    private var isAdd = true

    @SuppressLint("Recycle")
    private fun initAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NotificationSettings, 0, 0)
        isAdd = typedArray.getBoolean(R.styleable.NotificationSettings_isAdd, true)
        initViews()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initViewModel() {
        var activity = context as FragmentActivity
        viewModel = ViewModelProviders.of(activity).get(UtilsViewModel::class.java)

        viewModel?.getDeviceNotificationSetting()?.observe(activity, Observer {
            if(it.isNullOrEmpty()) {
                val params = JsonObject()
                params.addProperty("languageCode", Locale.getDefault().language)
                params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
                viewModel?.getDeviceNotificationFromServer(params)
            } else {
                devices = it
            }
        })

        viewModel?.getTimeNotificationSetting()?.observe(activity, Observer {
            if(it.isNullOrEmpty()) {
                val params = JsonObject()
                params.addProperty("languageCode", Locale.getDefault().language)
                params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
                viewModel?.getTimeNotificationFromServer(params)
            } else {
                times = it
            }
        })
    }

    private fun initViews() {
        tvTitle?.text = if (isAdd) resources.getString(R.string.string_alarm) else resources.getString(R.string.notification)
        btnNotification?.setOnClickListener {
            if (isAdd && devices?.isNullOrEmpty() == false && times?.isNullOrEmpty() == false) {
                llNone?.visibility = View.GONE
                llNotificationSettings?.visibility = View.VISIBLE
                llNotificationSettings.removeAllViews()
                addChildView()
            }
        }
    }

    private fun addChildView() {
        if (llNotificationSettings.childCount >= 4)
            return

        if (devices?.isNullOrEmpty() == true && times?.isNullOrEmpty() == true) {
            llNone?.visibility = View.VISIBLE
            llNotificationSettings?.visibility = View.GONE
            llNotificationSettings.removeAllViews()
            return
        }


        val itemView = LayoutInflater.from(context).inflate(R.layout.item_notification_setting, llNotificationSettings, false)
        llNotificationSettings.addView(itemView)

        val spinnerNotificationDevices = itemView.findViewById<Spinner>(R.id.spinnerNotificationDevices)
        val spinnerNotificationTimes = itemView.findViewById<Spinner>(R.id.spinnerNotificationTimes)
        val btnDelete = itemView.findViewById<FrameLayout>(R.id.btnDelete)
        val btnAdd = itemView.findViewById<FrameLayout>(R.id.btnAdd)

        devices?.let {
            spinnerNotificationDevices?.adapter = NotificationSettingAdapter(context, R.layout.item_notification_settings, it)
        }

        times?.let {
            spinnerNotificationTimes?.adapter = NotificationSettingAdapter(context, R.layout.item_notification_settings, it)
        }

        btnDelete.setOnClickListener {
            if (llNotificationSettings.childCount == 1) {
                llNone?.visibility = View.VISIBLE
                llNotificationSettings.removeAllViews()
                llNotificationSettings?.visibility = View.GONE
            } else llNotificationSettings.removeView(itemView)
        }

        btnAdd?.setOnClickListener {
            addChildView()
        }
    }

    fun addListSchedule(resourceNotifications: List<Notification>?) {
        if (!resourceNotifications.isNullOrEmpty()) {
            llNone?.visibility = View.GONE
            llNotificationSettings?.visibility = View.VISIBLE
            llNotificationSettings.removeAllViews()
            addListNotifications(resourceNotifications)
        } else {
            llNone?.visibility = View.VISIBLE
            llNotificationSettings?.visibility = View.GONE
            llNotificationSettings.removeAllViews()
        }
    }

    private fun addListNotifications(resourceNotifications: List<Notification>) {
        for (notification in resourceNotifications) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_notification_setting, llNotificationSettings, false)
            itemView?.findViewById<FrameLayout>(R.id.btnDelete)?.visibility = View.INVISIBLE
            itemView?.findViewById<FrameLayout>(R.id.btnAdd)?.visibility = View.INVISIBLE

            val spinnerNotificationDevices = itemView?.findViewById<Spinner>(R.id.spinnerNotificationDevices)
            val spinnerNotificationTimes = itemView?.findViewById<Spinner>(R.id.spinnerNotificationTimes)

            devices?.let {
                spinnerNotificationDevices?.adapter = NotificationSettingAdapter(context, R.layout.item_notification_settings, it)
                spinnerNotificationDevices?.isEnabled = false
                val device = it.find { de -> de.notificationTypeNo == notification.notificationType}
                device?.let { _ ->
                    spinnerNotificationDevices?.setSelection(it.indexOf(device))
                }
            }

            times?.let {
                spinnerNotificationTimes?.adapter = NotificationSettingAdapter(context, R.layout.item_notification_settings, it)
                spinnerNotificationTimes?.isEnabled = false
                val time = it.find { ti -> ti.alarmTimeNo == notification.alarmTime}
                time?.let { _ ->
                    spinnerNotificationTimes?.setSelection(it.indexOf(time))
                }
            }

            llNotificationSettings.addView(itemView)
        }
    }

    fun getListNotificationsSetting(): List<ResourceNotification>? {
        var listSchedule: ArrayList<ResourceNotification>? = ArrayList()
        for (i in 0 until llNotificationSettings.childCount) {
            val spinnerDevice = llNotificationSettings.getChildAt(i).findViewById<Spinner>(R.id.spinnerNotificationDevices)
            val spinnerTimes = llNotificationSettings.getChildAt(i).findViewById<Spinner>(R.id.spinnerNotificationTimes)

            devices?.let { device ->
                times?.let { time ->
                    listSchedule?.add(ResourceNotification(notificationType = device[spinnerDevice.selectedItemPosition].notificationTypeNo, alarmTime =  time[spinnerTimes.selectedItemPosition].alarmTimeNo))
                }
            }
        }
        return listSchedule
    }

}