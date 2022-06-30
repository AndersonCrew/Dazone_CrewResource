package com.kunpark.resource.view.main.day

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.databinding.ItemHourBinding
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.view.detail_schedule.DetailScheduleActivity

/**
 * Created by BM Anderson on 30/06/2022.
 */
class DayAdapter(private val list: ArrayList<CalendarDto>): RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    class DayViewHolder(val binding: ItemHourBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val calendarDto = list[position]
        holder.binding.tvTime.text = calendarDto.timeString
        if(!calendarDto.listResource.isNullOrEmpty()) {
            holder.binding.llTime.removeAllViews()
            for(resource in calendarDto.listResource) {
                val view = CardView(holder.itemView.context)
                val tvContent = TextView(holder.itemView.context)
                tvContent.gravity = Gravity.CENTER
                tvContent.setPadding(10, 5, 10, 5)
                tvContent.textSize = 13f
                view.addView(tvContent)
                val param = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )

                param.leftMargin = 10
                tvContent.text = resource.title?: ""
                tvContent.setBackgroundColor(Color.parseColor(resource.backgroundColor))

                view.layoutParams = param

                view.setOnClickListener {
                    val intent = Intent(holder.itemView.context, DetailScheduleActivity::class.java)
                    intent.putExtra(Constants.RESOURCE, resource)
                    holder.itemView.context.startActivity(intent)
                }

                holder.binding.llTime.addView(view)
            }
        } else {
            holder.binding.llTime.removeAllViews()
        }

        if(position == 0) {
            holder.binding.tvTime.setBackgroundResource(R.drawable.bg_allday)
            holder.binding.llTime.setBackgroundResource(R.drawable.bg_allday)
        } else {
            holder.binding.tvTime.setBackgroundResource(R.drawable.bg_date_type_not_choosen)
            holder.binding.llTime.setBackgroundResource(R.drawable.bg_date_type_not_choosen)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}