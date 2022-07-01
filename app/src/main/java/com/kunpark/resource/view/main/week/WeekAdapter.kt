package com.kunpark.resource.view.main.week

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.databinding.ItemWeekBinding
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.utils.Constants
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeekAdapter(private val list: ArrayList<CalendarDto>,private val calendar: Calendar): RecyclerView.Adapter<WeekAdapter.WeekCalendarViewHolder>() {

    private var sizeItem = 0
    class WeekCalendarViewHolder(val binding: ItemWeekBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekCalendarViewHolder {
        val binding = ItemWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekCalendarViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(listNew: ArrayList<CalendarDto>) {
        list.clear()
        list.addAll(listNew)
        notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: WeekCalendarViewHolder, position: Int) {
        if(!list.isNullOrEmpty()) {
            val calendarDto = list[position]
            holder.binding.llTime.removeAllViews()
            for(i in 0 until 8) {
                if(i == 0) {
                    val textView = TextView(holder.binding.root.context)
                    textView.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.colorText))
                    textView.textSize = 14f
                    textView.gravity = Gravity.CENTER
                    textView.setBackgroundResource(if(calendarDto.timeString == "All Day") R.drawable.bg_allday else R.drawable.bg_date_type_not_choosen)
                    textView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    textView.text = calendarDto.timeString
                    holder.binding.llTime.addView(textView)
                } else {
                    val calPosition = Calendar.getInstance()
                    calPosition.time = calendar.time
                    calPosition.add(Calendar.DAY_OF_MONTH, i)

                    val llChild = LinearLayout(holder.binding.root.context)
                    llChild.orientation = LinearLayout.VERTICAL
                    llChild.gravity = Gravity.CENTER
                    llChild.tag = SimpleDateFormat(Constants.Format_api_datetime).format(calPosition.time)
                    llChild.setBackgroundResource(if(calendarDto.timeString == "All Day") R.drawable.bg_allday else R.drawable.bg_date_type_not_choosen)
                    llChild.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    holder.binding.llTime.addView(llChild)



                    calendarDto.listResource.filter { it.startStr == SimpleDateFormat(Constants.Format_api_datetime).format(calPosition.time) }.forEach { resource ->
                        if(holder.binding.llTime.children.find {
                                it is LinearLayout && it.tag == resource.startStr
                            } != null) {
                            val llChild = holder.binding.llTime.children.find {
                                it is LinearLayout && it.tag == resource.startStr
                            } as LinearLayout

                            val textView = TextView(holder.binding.root.context)
                            textView.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.colorText))
                            textView.setBackgroundColor(Color.parseColor(resource.backgroundColor))
                            textView.text = resource.title
                            textView.textSize = 12f
                            textView.gravity = Gravity.CENTER
                            textView.maxLines = 1
                            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
                            textView.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.colorWhite))
                            llChild.addView(textView)
                        }
                    }
                }
            }
        }
    }
}