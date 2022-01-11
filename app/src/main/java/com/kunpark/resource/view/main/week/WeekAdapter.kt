package com.kunpark.resource.view.main.week

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.model.CalendarDto

class WeekAdapter(private val list: List<CalendarDto>): RecyclerView.Adapter<WeekAdapter.WeekCalendarViewHolder>() {

    class WeekCalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var llResource: LinearLayout?= null
        init {
            llResource = itemView.findViewById(R.id.llResource)
        }

        fun bind(dto: CalendarDto) {
            if(!dto.listResource.isNullOrEmpty()) {
                for(resource in dto.listResource) {
                    val tvResource = TextView(itemView.context)
                    tvResource.text = resource.title
                    tvResource.setBackgroundColor(Color.parseColor(resource.backgroundColor))
                    llResource?.addView(tvResource)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekCalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week_resource, parent, false)
        val params = view.layoutParams
        params.width = parent.measuredWidth / 8
        params.height = parent.measuredWidth / 8
        view.layoutParams = params
        return WeekCalendarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WeekCalendarViewHolder, position: Int) {
        if(!list.isNullOrEmpty()) {
            holder.bind(list[position])
        }
    }
}