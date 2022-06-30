package com.kunpark.resource.view.main.month

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.CalendarMonth
import com.kunpark.resource.model.Resource
import com.kunpark.resource.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CalendarMonthAdapter(private var listResource: List<CalendarDto>, private val listener: (Resource) -> Unit): RecyclerView.Adapter<CalendarMonthAdapter.CalendarMonthViewHolder>() {

    class CalendarMonthViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvDay: TextView?= null
        private var tvStrDay: TextView?= null
        private var llListResource: LinearLayout?= null

        init {
            tvDay = itemView.findViewById(R.id.tvDay)
            tvStrDay = itemView.findViewById(R.id.tvStrDay)
            llListResource = itemView.findViewById(R.id.llListResource)
        }

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(calendarDTO: CalendarDto, listener: (Resource) -> Unit) {
            tvDay?.text = SimpleDateFormat("dd").format(SimpleDateFormat(Constants.YY_MM_DD).parse(calendarDTO.timeString))

            if(!calendarDTO.listResource.isNullOrEmpty()) {
                val date = SimpleDateFormat(Constants.Format_api_datetime).parse(calendarDTO.listResource[0].startStr)
                val cal = Calendar.getInstance()
                cal.time = date
                val strDayOfWeek = when(cal.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> "Mon"
                    Calendar.TUESDAY -> "Tue"
                    Calendar.WEDNESDAY -> "Wed"
                    Calendar.THURSDAY -> "Thu"
                    Calendar.SATURDAY -> "Sat"
                    else -> "Sun"
                }

                tvStrDay?.text = strDayOfWeek
                llListResource?.removeAllViews()

            }

            for(resource in calendarDTO.listResource) {
                val view = LayoutInflater.from(itemView.context).inflate(R.layout.item_card_resource_month, null)
                val tvContent: TextView? = view?.findViewById(R.id.tvContent)
                val tvTime: TextView? = view?.findViewById(R.id.tvTime)
                val llBackground: LinearLayout? = view?.findViewById(R.id.llBackground)

                tvContent?.text = resource.title?: ""
                tvTime?.text = "${resource.startTime} - ${resource.endTime}"
                llBackground?.setBackgroundColor(Color.parseColor(resource.backgroundColor?: ""))

                view.setOnClickListener {
                    listener.invoke(resource)
                }
                llListResource?.addView(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarMonthViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month_calendar, parent, false)
        return CalendarMonthViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listResource.size
    }

    override fun onBindViewHolder(holder: CalendarMonthViewHolder, position: Int) {
        if(!listResource.isNullOrEmpty()) {
            val resource = listResource[position]
            holder.bind(resource, listener)
        }
    }
}