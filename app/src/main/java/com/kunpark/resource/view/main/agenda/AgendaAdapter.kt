package com.kunpark.resource.view.main.agenda

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.dialog.DialogUtil
import com.kunpark.resource.model.AgendaItemType
import com.kunpark.resource.model.CalendarDto
import com.kunpark.resource.model.Resource
import com.kunpark.resource.utils.Config
import com.kunpark.resource.view.add_schedule.AddScheduleActivity

class AgendaAdapter(var listCalendarDto: List<CalendarDto>, private val itemClick: (type: AgendaItemType, resource: Resource?) -> Unit): RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>() {
    class AgendaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvDate: TextView?= null
        private var llResource: LinearLayout?= null
        init {
            tvDate = itemView.findViewById(R.id.tvDay)
            llResource = itemView.findViewById(R.id.llResource)
        }

        @SuppressLint("SimpleDateFormat")
        fun bindView(calendarDto: CalendarDto, itemClick: (type: AgendaItemType, resource: Resource?) -> Unit) {
            tvDate?.text = calendarDto.day?.toString()
            llResource?.removeAllViews()
            if(!calendarDto.listResource.isNullOrEmpty()) {
                for(src in calendarDto.listResource) {
                    if(llResource?.childCount?: 0 <= 2) {
                        val textViewItem = TextView(itemView.context)
                        textViewItem.text = src.title
                        textViewItem.setBackgroundColor(Color.parseColor(src.backgroundColor?: ""))
                        textViewItem.gravity = Gravity.CENTER
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