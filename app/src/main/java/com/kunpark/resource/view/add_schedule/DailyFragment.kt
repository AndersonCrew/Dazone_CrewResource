package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.databinding.FragmentDailyBinding
import com.kunpark.resource.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DailyFragment: BaseFragment() {
    private lateinit var binding: FragmentDailyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
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

        val list = resources.getStringArray(R.array.arr_daily)
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.layout_spinner, list
        )

        binding.spFrequencyValue.adapter = adapter
    }
}