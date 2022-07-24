package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment
import com.kunpark.resource.databinding.FragmentMonthlyBinding
import com.kunpark.resource.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class MonthlyFragment: BaseFragment() {
    private lateinit var binding: FragmentMonthlyBinding
    private lateinit var viewModel : AddScheduleViewModel
    private var calStartChosen = Calendar.getInstance()
    private var calEndChosen = Calendar.getInstance()
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
        initListener()
    }

    private fun initListener() {
        binding.ckInfiniteLoop.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.tvEndDay.isEnabled = false
                binding.tvEndDay.alpha = 0.5f
                viewModel.requestMonthly.EndDate = ""
            } else {
                binding.tvEndDay.isEnabled = true
                binding.tvEndDay.alpha = 1f
                viewModel.requestMonthly.EndDate = binding.tvEndDay.text.toString()
            }
        }

        binding.tvStartDay.setOnClickListener {
            val currentDay = calStartChosen.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calStartChosen.get(Calendar.MONTH)
            val currentYear = calStartChosen.get(Calendar.YEAR)
            var datePicker = DatePickerDialog(requireContext(), { dialog, year, month, day ->
                calStartChosen.set(Calendar.DAY_OF_MONTH, day)
                calStartChosen.set(Calendar.MONTH, month)
                calStartChosen.set(Calendar.YEAR, year)
                binding.tvStartDay.text = SimpleDateFormat(Constants.Format_api_datetime).format(calStartChosen.time)
                viewModel.requestMonthly.StartDate = binding.tvStartDay.text.toString()
            }, currentYear, currentMonth, currentDay)

            datePicker.show()
        }

        binding.tvEndDay.setOnClickListener {
            var datePicker = DatePickerDialog(requireContext(), { dialog, year, month, day ->
                calEndChosen.set(Calendar.DAY_OF_MONTH, day)
                calEndChosen.set(Calendar.MONTH, month)
                calEndChosen.set(Calendar.YEAR, year)
                binding.tvEndDay.text = SimpleDateFormat(Constants.Format_api_datetime).format(calEndChosen.time)

                viewModel.requestMonthly.EndDate = binding.tvEndDay.text.toString()
            }, calStartChosen.get(Calendar.YEAR), calStartChosen.get(Calendar.MONTH), calStartChosen.get(Calendar.DAY_OF_MONTH))


            datePicker.datePicker.minDate = calStartChosen.time.time
            datePicker.show()
        }

        binding.tvStartTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(requireContext(), { view, hour, minute ->
                calStartChosen.set(Calendar.HOUR, hour)
                calStartChosen.set(Calendar.MINUTE, minute)
                binding.tvStartTime.text = SimpleDateFormat("HH:mm").format(calStartChosen.time)
                viewModel.requestMonthly.StartTime = binding.tvStartTime.text.toString()
            }, calStartChosen.get(Calendar.HOUR), calStartChosen.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }

        binding.tvEndTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(requireContext(), { view, hour, minute ->
                calEndChosen.set(Calendar.HOUR, hour)
                calEndChosen.set(Calendar.MINUTE, minute)
                binding.tvStartTime.text = SimpleDateFormat("HH:mm").format(calStartChosen.time)
                viewModel.requestMonthly.EndTime = binding.tvEndTime.text.toString()
            }, calStartChosen.get(Calendar.HOUR), calStartChosen.get(Calendar.MINUTE), true)

            timePickerDialog.show()
        }

        binding.ckAllDay.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.requestMonthly.IsAllDay = isChecked

            if(isChecked) {
                binding.tvStartTime.isEnabled = false
                binding.tvEndTime.isEnabled = false

                binding.tvStartTime.alpha = 0.5f
                binding.tvEndTime.alpha = 0.5f
                viewModel.requestMonthly.StartTime = "00:00"
                viewModel.requestMonthly.EndTime = "00:00"
            } else {
                binding.tvStartTime.isEnabled = true
                binding.tvEndTime.isEnabled = true

                binding.tvStartTime.alpha = 1f
                binding.tvEndTime.alpha = 1f
                viewModel.requestMonthly.StartTime = binding.tvStartTime.text.toString()
                viewModel.requestMonthly.EndTime = binding.tvEndTime.text.toString()
            }
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

        binding.spinnerFrequency.adapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_spinner, resources.getStringArray(R.array.arr_monthly)
        )

        binding.spinnerFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.requestMonthly.RepeatCount = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.requestMonthly.RepeatCount = 1
            }

        }


        binding.spDay.adapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_spinner, resources.getStringArray(R.array.arr_monthly_day)
        )

        val currentCal = Calendar.getInstance()
        currentCal.time = Date(System.currentTimeMillis())
        binding.spDay.setSelection(currentCal.get(Calendar.DAY_OF_MONTH) - 1)

        binding.spDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.requestMonthly.RepeatDay = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.requestMonthly.RepeatCount = 1
            }

        }


        binding.ckDay.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.ck5Date.isChecked = false
                binding.ckLastDate.isChecked = false
                viewModel.requestMonthly.RepeatDay = currentCal.get(Calendar.DAY_OF_MONTH)
                viewModel.requestMonthly.IsFiveWeek = 0
                viewModel.requestMonthly.IsLastDay = 0
            }
        }

        binding.ck5Date.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.ckDay.isChecked = false
                binding.ckLastDate.isChecked = false
                viewModel.requestMonthly.IsFiveWeek = 1
                viewModel.requestMonthly.IsLastDay = 0
            }
        }

        binding.ckLastDate.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.ckDay.isChecked = false
                binding.ck5Date.isChecked = false
                viewModel.requestMonthly.IsFiveWeek = 0
                viewModel.requestMonthly.IsLastDay = 1
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

        viewModel = ViewModelProviders.of(requireActivity()).get(AddScheduleViewModel::class.java)

        viewModel.requestMonthly.StartDate = binding.tvStartDay.text.toString()
        viewModel.requestMonthly.EndDate = binding.tvEndDay.text.toString()
        viewModel.requestMonthly.StartTime = binding.tvStartTime.text.toString()
        viewModel.requestMonthly.EndTime = binding.tvEndTime.text.toString()
        viewModel.requestMonthly.RepeatType = 3
        viewModel.requestMonthly.IsFiveWeek = 0
        viewModel.requestMonthly.IsLastDay = 0
    }
}