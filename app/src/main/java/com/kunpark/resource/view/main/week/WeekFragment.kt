package com.kunpark.resource.view.main.week

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_daily.*
import java.util.*

class WeekFragment(private val calendar: Calendar): BaseFragment() {

    private var llAllDay: LinearLayout?= null
    private var llTime: LinearLayout?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_week_resource, container, false)
        initView(root)
        initViewModel()
        return root
    }

    private fun initView(view: View) {
        llAllDay = view.findViewById(R.id.llAllDay)
        llTime = view.findViewById(R.id.llTime)

        val tvAllDay = TextView(requireContext())
        tvAllDay.text = "All Day"
        val param = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        tvAllDay.layoutParams = param
        tvAllDay.textSize = 13f
        tvAllDay.setBackgroundResource(R.drawable.bg_allday)
        tvAllDay.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
        tvAllDay.gravity = Gravity.CENTER
        llAllDay?.addView(tvAllDay)

        for(i in 0 until 7) {
            val tvDate = TextView(requireContext())
            tvDate.text = ""
            tvDate.id = i
            val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            tvDate.layoutParams = param
            tvDate.textSize = 13f
            tvDate.setBackgroundResource(R.drawable.bg_allday)
            tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            tvDate.gravity = Gravity.CENTER
            llAllDay?.addView(tvDate)
        }

        for(i in 0 until 25) {
            val llTimeWeek = LinearLayout(requireContext())
            for(j in 0 until 8) {
                val tvDate = TextView(requireContext())
                tvDate.id = j
                val param = LinearLayout.LayoutParams(0, 140, 1f)
                tvDate.layoutParams = param
                tvDate.textSize = 13f
                tvDate.setBackgroundResource(R.drawable.bg_allday_week)
                tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
                tvDate.gravity = Gravity.CENTER
                if(j == 0) {
                    val time = if(i <  10) "0$i" else i
                    tvDate.text = "$time:00"
                } else {
                    tvDate.text = ""
                }

                llTimeWeek.addView(tvDate)
            }

            llTimeWeek.id = i
            llTime?.addView(llTimeWeek)
        }
    }

    private fun initViewModel() {

    }
}