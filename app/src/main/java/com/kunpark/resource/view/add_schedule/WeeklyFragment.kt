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
import com.kunpark.resource.databinding.FragmentWeeklyBinding
import com.kunpark.resource.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class WeeklyFragment: BaseFragment() {
    private lateinit var binding: FragmentWeeklyBinding
    private lateinit var viewModel : AddScheduleViewModel
    private var calStartChosen = Calendar.getInstance()
    private var calEndChosen = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)
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
                viewModel.requestWeekly.EndDate = ""
            } else {
                binding.tvEndDay.isEnabled = true
                binding.tvEndDay.alpha = 1f
                viewModel.requestWeekly.EndDate = binding.tvEndDay.text.toString()
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
                viewModel.requestWeekly.StartDate = binding.tvStartDay.text.toString()
            }, currentYear, currentMonth, currentDay)

            datePicker.show()
        }

        binding.tvEndDay.setOnClickListener {
            var datePicker = DatePickerDialog(requireContext(), { dialog, year, month, day ->
                calEndChosen.set(Calendar.DAY_OF_MONTH, day)
                calEndChosen.set(Calendar.MONTH, month)
                calEndChosen.set(Calendar.YEAR, year)
                binding.tvEndDay.text = SimpleDateFormat(Constants.Format_api_datetime).format(calEndChosen.time)

                viewModel.requestWeekly.EndDate = binding.tvEndDay.text.toString()
            }, calStartChosen.get(Calendar.YEAR), calStartChosen.get(Calendar.MONTH), calStartChosen.get(Calendar.DAY_OF_MONTH))


            datePicker.datePicker.minDate = calStartChosen.time.time
            datePicker.show()
        }

        binding.tvStartTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(requireContext(), { view, hour, minute ->
                calStartChosen.set(Calendar.HOUR, hour)
                calStartChosen.set(Calendar.MINUTE, minute)
                binding.tvStartTime.text = SimpleDateFormat("HH:mm").format(calStartChosen.time)
                viewModel.requestWeekly.StartTime = binding.tvStartTime.text.toString()
            }, calStartChosen.get(Calendar.HOUR), calStartChosen.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }

        binding.tvEndTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(requireContext(), { view, hour, minute ->
                calEndChosen.set(Calendar.HOUR, hour)
                calEndChosen.set(Calendar.MINUTE, minute)
                binding.tvStartTime.text = SimpleDateFormat("HH:mm").format(calStartChosen.time)
                viewModel.requestWeekly.EndTime = binding.tvEndTime.text.toString()
            }, calStartChosen.get(Calendar.HOUR), calStartChosen.get(Calendar.MINUTE), true)

            timePickerDialog.show()
        }

        binding.ckAllDay.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.requestWeekly.IsAllDay = isChecked

            if(isChecked) {
                binding.tvStartTime.isEnabled = false
                binding.tvEndTime.isEnabled = false

                binding.tvStartTime.alpha = 0.5f
                binding.tvEndTime.alpha = 0.5f
                viewModel.requestWeekly.StartTime = "00:00"
                viewModel.requestWeekly.EndTime = "00:00"
            } else {
                binding.tvStartTime.isEnabled = true
                binding.tvEndTime.isEnabled = true

                binding.tvStartTime.alpha = 1f
                binding.tvEndTime.alpha = 1f
                viewModel.requestWeekly.StartTime = binding.tvStartTime.text.toString()
                viewModel.requestWeekly.EndTime = binding.tvEndTime.text.toString()
            }
        }

        binding.ckMon.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs + 2
            } else {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs - 2
            }
        }

        binding.ckTue.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs + 4
            } else {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs - 4
            }
        }

        binding.ckWed.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs + 8
            } else {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs - 8
            }
        }

        binding.ckThu.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs + 16
            } else {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs - 16
            }
        }

        binding.ckFri.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs + 32
            } else {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs - 32
            }
        }

        binding.ckSat.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs + 64
            } else {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs - 64
            }
        }

        binding.ckSun.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs + 1
            } else {
                viewModel.requestWeekly.RepeatDOWs = viewModel.requestWeekly.RepeatDOWs - 1
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initView() {
        viewModel = ViewModelProviders.of(requireActivity()).get(AddScheduleViewModel::class.java)
        viewModel.requestWeekly.RepeatType = 2
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

        val list = resources.getStringArray(R.array.arr_weekly)
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.layout_spinner, list
        )

        binding.spinnerFrequency.adapter = adapter

        binding.spinnerFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.requestWeekly.RepeatCount = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.requestWeekly.RepeatCount = 1
            }

        }

        viewModel.requestWeekly.StartDate = binding.tvStartDay.text.toString()
        viewModel.requestWeekly.EndDate = binding.tvEndDay.text.toString()
        viewModel.requestWeekly.StartTime = binding.tvStartTime.text.toString()
        viewModel.requestWeekly.EndTime = binding.tvEndTime.text.toString()
    }
}