package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.databinding.FragmentMonthlyBinding
import com.kunpark.resource.databinding.FragmentSpecialDayBinding
import com.kunpark.resource.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class MonthlyFragment: BaseFragment() {
    private lateinit var binding: FragmentMonthlyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initView() {
        val cal = Calendar.getInstance()
        cal.time = Date(System.currentTimeMillis())
        binding.tvStartDay.text = SimpleDateFormat(Constants.YY_MM_DD_SHOW).format(cal.time)
        binding.tvEndDay.text = SimpleDateFormat(Constants.YY_MM_DD_SHOW).format(cal.time)

        val rightNow = Calendar.getInstance()
        val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 24 hrs format (ranging from 0-23)

        var startHour: String = if(currentHourIn24Format < 23 ) {
            (currentHourIn24Format + 1).toString()
        } else {
            (24 - (currentHourIn24Format + 1)).toString()
        }

        var endHour: String = if(currentHourIn24Format < 22 ) {
            (currentHourIn24Format + 3).toString()
        } else {
            (24 - (currentHourIn24Format + 2)).toString()
        }

        if(startHour.length == 1) {
            startHour = "0$startHour"
        }

        if(endHour.length == 1) {
            endHour = "0$endHour"
        }


        binding.tvStartTime.text = "$startHour:00"
        binding.tvEndTime.text = "$endHour:00"

        binding.spinnerFrequency.adapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_spinner, resources.getStringArray(R.array.arr_monthly)
        )


        binding.spDay.adapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_spinner, resources.getStringArray(R.array.arr_monthly_day)
        )

        val currentCal = Calendar.getInstance()
        currentCal.time = Date(System.currentTimeMillis())
        binding.spDay.setSelection(currentCal.get(Calendar.DAY_OF_MONTH) - 1)


        binding.ckDay.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.ck5Date.isChecked = false
                binding.ckLastDate.isChecked = false
            }
        }

        binding.ck5Date.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.ckDay.isChecked = false
                binding.ckLastDate.isChecked = false
            }
        }

        binding.ckLastDate.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.ckDay.isChecked = false
                binding.ck5Date.isChecked = false
            }
        }

        val strDate = when(currentCal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            else -> "Sat"
        }

        binding.ck5Date.text = "5 $strDate Month"
        binding.ckLastDate.text = "Last $strDate Month"
    }
}