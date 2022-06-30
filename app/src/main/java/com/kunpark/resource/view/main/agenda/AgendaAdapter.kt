package com.kunpark.resource.view.main.agenda

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.dialog.DialogUtil
import com.kunpark.resource.model.AgendaItemType
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.Resource
import com.kunpark.resource.utils.Config
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.view.add_schedule.AddScheduleActivity
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AgendaAdapter(var listCalendarDto: List<CalendarDto>, private val itemClick: (type: AgendaItemType, resource: Resource?) -> Unit): RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>() {
    class AgendaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvDate: TextView?= null
        private var llResource: LinearLayout?= null
        private var csItem: ConstraintLayout?= null
        init {
            tvDate = itemView.findViewById(R.id.tvDay)
            llResource = itemView.findViewById(R.id.llResource)
            csItem = itemView.findViewById(R.id.csItem)
        }

        @SuppressLint("SimpleDateFormat")
        fun bindView(calendarDto: CalendarDto, itemClick: (type: AgendaItemType, resource: Resource?) -> Unit) {
            SimpleDateFormat("dd").format(SimpleDateFormat(Constants.YY_MM_DD).parse(calendarDto.timeString))
            tvDate?.text = SimpleDateFormat("dd").format(SimpleDateFormat(Constants.YY_MM_DD).parse(calendarDto.timeString))
            val srtToday = SimpleDateFormat(Constants.YY_MM_DD).format(Date(System.currentTimeMillis()))
            csItem?.setBackgroundResource(if(srtToday == calendarDto.timeString) R.drawable.bg_today else R.drawable.bg_date_type_not_choosen)
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat(Constants.YY_MM_DD).parse(calendarDto.timeString)
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                tvDate?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
            } else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                tvDate?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
            } else {
                tvDate?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorText))
            }

            llResource?.removeAllViews()
            if(!calendarDto.listResource.isNullOrEmpty()) {
                for(src in calendarDto.listResource) {
                    if(llResource?.childCount?: 0 <= 2) {
                        val textViewItem = TextView(itemView.context)
                        textViewItem.text = src.title
                        textViewItem.maxLines = 1
                        textViewItem.setBackgroundColor(Color.parseColor(src.backgroundColor?: ""))
                        textViewItem.gravity = Gravity.LEFT and Gravity.TOP
                        val param = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0,
                            1.0f
                        )

                        param.topMargin = 3

                        textViewItem.id = src.id?: 0
                        textViewItem.textSize = 11f
                        textViewItem.layoutParams = param

                        textViewItem.setOnClickListener {
                            itemClick.invoke(AgendaItemType.ITEM_RESOURCE, src)
                        }
                        llResource?.addView(textViewItem)
                    }
                }

                if(calendarDto.listResource.size > 3) {
                    val textViewItem = TextView(itemView.context)
                    textViewItem.text = "+${calendarDto.listResource.size - 3}"
                    textViewItem.gravity = Gravity.CENTER
                    textViewItem.setTextColor(Color.BLACK)
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1.0f
                    )

                    textViewItem.textSize = 13f
                    textViewItem.layoutParams = param
                    llResource?.addView(textViewItem)
                }
            }
        }
    }

    fun updateList(list: List<CalendarDto>) {
        listCalendarDto = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        val params = rootView.layoutParams
        params.width = parent.measuredWidth / Config.COLUMN_AGENDA
        params.height = parent.measuredHeight / Config.ROW
        rootView.layoutParams = params
        return AgendaViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return listCalendarDto.size
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        if(!listCalendarDto.isNullOrEmpty()) {
            holder.bindView(listCalendarDto[position], itemClick)

            holder.itemView.setOnClickListener {
                if(listCalendarDto[position].listResource.isNullOrEmpty()) {
                    itemClick.invoke(AgendaItemType.ITEM_CALENDAR, null)
                } else {
                    DialogUtil.with(holder.itemView.context).createMultipleDialog(listCalendarDto[position], listCalendarDto[position].timeString?: "").show()
                }
            }
        }
    }
}