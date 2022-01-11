package com.kunpark.resource.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.model.AgendaItemType
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity

class DialogUtil (private val context: Context){
    companion object {
        fun with(context: Context): DialogUtil {
            return DialogUtil(context)
        }
    }

    fun createMultipleDialog(calendarDTO: CalendarDto, strTime: String): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_multiple_resource)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(true)

        val imgClose: ImageView = dialog.findViewById(R.id.imgClose)
        val tvTime: TextView = dialog.findViewById(R.id.tvTime)
        val llAddResource: LinearLayout = dialog.findViewById(R.id.llAddResource)
        val llResource: LinearLayout = dialog.findViewById(R.id.llResource)

        imgClose.setOnClickListener { dialog.dismiss() }
        llAddResource.setOnClickListener {  }
        tvTime.text = strTime

        for(resource in calendarDTO.listResource) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_card_resource_month, null)
            val tvContent: TextView? = view?.findViewById(R.id.tvContent)
            val tvTime: TextView? = view?.findViewById(R.id.tvTime)
            val llBackground: LinearLayout? = view?.findViewById(R.id.llBackground)

            tvContent?.text = resource.title?: ""
            tvTime?.text = "${resource.startTime} - ${resource.endTime}"
            llBackground?.setBackgroundColor(Color.parseColor(resource.backgroundColor?: ""))

            view.setOnClickListener {
                DetailScheduleActivity.start(context as BaseActivity, resource)
            }

            llResource.addView(view)
        }

        return dialog
    }
}