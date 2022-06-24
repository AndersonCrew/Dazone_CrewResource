package com.kunpark.resource.custom_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.kunpark.resource.R
import com.kunpark.resource.model.DevicesNotification
import com.kunpark.resource.model.TimesNotification


class NotificationSettingAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val listScheduleNotifications: List<Any>):
        ArrayAdapter<Any>(context, layoutResource, listScheduleNotifications) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification_settings, parent, false) as TextView
        if(listScheduleNotifications[position] is DevicesNotification) {
            val device = listScheduleNotifications[position] as DevicesNotification
            view.text = device.notificationTypeName
        } else if(listScheduleNotifications[position] is TimesNotification) {
            val time = listScheduleNotifications[position] as TimesNotification
            view.text = time.alarmTimeName
        }

        return view
    }
}