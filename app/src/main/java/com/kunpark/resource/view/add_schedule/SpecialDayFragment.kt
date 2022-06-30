package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.databinding.FragmentSpecialDayBinding
import com.kunpark.resource.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class SpecialDayFragment: BaseFragment() {
    private lateinit var binding: FragmentSpecialDayBinding
    private var calStartChosen = Calendar.getInstance()
    private var calEndChosen = Calendar.getInstance()

    private var dayEndChosen = 0
    private var monthEndChosen = 0
    private var yearEndChosen = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpecialDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvents()
    }

    @SuppressLint("SimpleDateFormat")
    private fun initEvents() {
        binding.tvStartDay.setOnClickListener {
            val currentDay = calStartChosen.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calStartChosen.get(Calendar.MONTH)
            val currentYear = calStartChosen.get(Calendar.YEAR)
            var datePicker = DatePickerDialog(requireContext(), { dialog, year, month, day ->
                calStartChosen.set(Calendar.DAY_OF_MONTH, day)
                calStartChosen.set(Calendar.MONTH, month)
                calStartChosen.set(Calendar.YEAR, year)
                binding.tvStartDay.text = SimpleDateFormat(Constants.YY_MM_DD_SHOW).format(calStartChosen.time)
            }, currentYear, currentMonth, currentDay)

            datePicker.show()
        }

        binding.tvEndDay.setOnClickListener {
            var datePicker = DatePickerDialog(requireContext(), { dialog, year, month, day ->
                calEndChosen.set(Calendar.DAY_OF_MONTH, day)
                calEndChosen.set(Calendar.MONTH, month)
                calEndChosen.set(Calendar.YEAR, year)
                binding.tvEndDay.text = SimpleDateFormat(Constants.YY_MM_DD_SHOW).format(calEndChosen.time)
            }, calStartChosen.get(Calendar.YEAR), calStartChosen.get(Calendar.MONTH), calStartChosen.get(Calendar.DAY_OF_MONTH))


            datePicker.datePicker.minDate = calStartChosen.time.time
            datePicker.show()
        }

        binding.tvStartTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(requireContext(), { view, hour, minute ->
                calStartChosen.set(Calendar.HOUR, hour)
                calStartChosen.set(Calendar.MINUTE, minute)
                binding.tvStartTime.text = SimpleDateFormat("hh:mm").format(calStartChosen.time)
            }, calStartChosen.get(Calendar.HOUR), calStartChosen.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }

        binding.tvEndTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(requireContext(), { view, hour, minute ->
                calEndChosen.set(Calendar.HOUR, hour)
                calEndChosen.set(Calendar.MINUTE, minute)
                binding.tvStartTime.text = SimpleDateFormat("hh:mm").format(calStartChosen.time)
            }, calStartChosen.get(Calendar.HOUR), calStartChosen.get(Calendar.MINUTE), true)

            timePickerDialog.show()
        }
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
    }
}