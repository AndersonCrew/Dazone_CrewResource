package com.kunpark.resource.view.add_schedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.databinding.ActivityAddScheduleBinding
import com.kunpark.resource.model.ResourceTree
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.activity_add_schedule.notificationSetting
import kotlinx.android.synthetic.main.activity_detail_schedule.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*


class AddScheduleActivity : BaseActivity() {
    private lateinit var viewModel : AddScheduleViewModel
    private val organizationViewModel: OrganizationViewModel by viewModels()
    private var binding: ActivityAddScheduleBinding?= null
    private var resourceTree: ResourceTree?= null
    private var REQUEST_CODE_RESOURCE = 111
    private var REQUEST_CODE_ORGANIZATION = 112
    private var selected: ArrayList<User> = arrayListOf()
    override fun initEvent() {
        if(!DazoneApplication.getInstance().mPref?.getListOrganization().isNullOrEmpty()) {
            //Check Modify
            showProgressDialog()
            val params = JsonObject()
            params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
            params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes())
            params.addProperty("languageCode", Locale.getDefault().language.uppercase(Locale.getDefault()))
            params.addProperty("moddate", SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(Date(System.currentTimeMillis())))

            organizationViewModel.getDepartmentsMod(params)
        } else {
            //Check Modify
            showProgressDialog()
            val params = JsonObject()
            params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
            params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes())
            params.addProperty("languageCode", Locale.getDefault().language.uppercase(Locale.getDefault()))
            organizationViewModel.getDepartments(params)
        }

        viewModel = ViewModelProviders.of(this).get(AddScheduleViewModel::class.java)
        imgShowContent?.setOnClickListener {
            if(etContent?.visibility == View.GONE) {
                etContent?.visibility = View.VISIBLE
                imgShowContent?.setImageResource(R.drawable.ic_drop_up)
            } else if(etContent?.visibility == View.VISIBLE) {
                etContent?.visibility = View.GONE
                imgShowContent?.setImageResource(R.drawable.ic_drop_down)
            }
        }

        binding?.imgBack?.setOnClickListener { onBackPressed() }
        binding?.vpTab?.adapter = TabAddResource(supportFragmentManager)
        binding?.vpTab?.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        binding?.tabLayout?.addTab(tabLayout.newTab())
        binding?.tabLayout?.setupWithViewPager(vpTab)

        binding?.tvResource?.setOnClickListener {
            val intent = Intent(this, ResourceChartActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_RESOURCE)
        }

        binding?.tvShareValue?.setOnClickListener {
            val intent = Intent(this, OrganizationActivity::class.java)
            intent.putExtra(Constants.SELECTED_LIST, Gson().toJson(selected))
            startActivityForResult(intent, REQUEST_CODE_ORGANIZATION)
        }

        changeLayoutParams(0)

        binding?.vpTab?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                changeLayoutParams(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        binding?.imgDone?.setOnClickListener {
            checkValidation()
        }
    }

    private fun checkValidation() {
        if(binding?.etTitle?.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please input title!", Toast.LENGTH_LONG).show()
            return
        }

         if(binding?.tvResource?.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select a publish resource!", Toast.LENGTH_LONG).show()
            return
        }

        //Check Modify
        showProgressDialog()
        val params = JsonObject()
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes())
        params.addProperty("languageCode", Locale.getDefault().language.uppercase(Locale.getDefault()))

        params.addProperty("Title", binding?.etTitle?.text.toString())
        params.addProperty("Content", binding?.etContent?.text.toString())
        params.addProperty("ResourceNo", resourceTree?.resourceNo)
        if(!selected.isNullOrEmpty()) {
            var participant = ""
            selected.forEach {
                participant += ";${it.userNo}"
            }

            params.addProperty("ParticipantNo", participant.substring(1, participant.length))
        }

        params.addProperty("ListNotification", Gson().toJson(binding?.notificationSetting?.getListNotificationsSetting()))

        when(binding?.vpTab?.currentItem) {
            0 -> {
                params.addProperty("IsLunar", viewModel.requestSpecial.IsLunar)
                params.addProperty("RepeatType", 0)
                params.addProperty("StartTime", viewModel.requestSpecial.StartTime)
                params.addProperty("EndTime", viewModel.requestSpecial.EndTime)
                params.addProperty("StartDate", viewModel.requestSpecial.StartDate)
                params.addProperty("EndDate", viewModel.requestSpecial.EndDate)
                params.addProperty("IsAllDay", viewModel.requestSpecial.IsAllDay)
                params.addProperty("RepeatDOWs", 0)
                params.addProperty("IsNotiPopup", false)
                params.addProperty("NotiTimeType", 0)
                params.addProperty("RepeatMonth", 0)
                params.addProperty("ReservationNo", 0)
                params.addProperty("RepeatCount", 0)
                params.addProperty("RepeatWeek", 0)
                params.addProperty("RepeatDay", 0)
                params.addProperty("IsNotiMail", false)
                params.addProperty("IsNotiSMS", false)
                params.addProperty("IsNotiNote", false)
            }

            1 -> {
                params.addProperty("RepeatType", 1)
                params.addProperty("StartTime", viewModel.requestDaily.StartTime)
                params.addProperty("EndTime", viewModel.requestDaily.EndTime)
                params.addProperty("StartDate", viewModel.requestDaily.StartDate)
                params.addProperty("EndDate", viewModel.requestDaily.EndDate)
                params.addProperty("IsAllDay", viewModel.requestDaily.IsAllDay)
                params.addProperty("RepeatDOWs", 0)
                params.addProperty("IsNotiPopup", false)
                params.addProperty("NotiTimeType", 0)
                params.addProperty("RepeatMonth", 0)
                params.addProperty("ReservationNo", 0)
                params.addProperty("RepeatCount", viewModel.requestDaily.RepeatCount)
                params.addProperty("RepeatWeek", 0)
                params.addProperty("RepeatDay", 0)
                params.addProperty("IsNotiMail", false)
                params.addProperty("IsNotiSMS", false)
                params.addProperty("IsNotiNote", false)
            }

            2 -> {
                params.addProperty("RepeatType", 2)
                params.addProperty("StartTime", viewModel.requestWeekly.StartTime)
                params.addProperty("EndTime", viewModel.requestWeekly.EndTime)
                params.addProperty("StartDate", viewModel.requestWeekly.StartDate)
                params.addProperty("EndDate", viewModel.requestWeekly.EndDate)
                params.addProperty("IsAllDay", viewModel.requestWeekly.IsAllDay)
                params.addProperty("RepeatDOWs", viewModel.requestWeekly.RepeatDOWs)
                params.addProperty("IsNotiPopup", false)
                params.addProperty("NotiTimeType", 0)
                params.addProperty("RepeatMonth", 0)
                params.addProperty("ReservationNo", 0)
                params.addProperty("RepeatCount", viewModel.requestWeekly.RepeatCount)
                params.addProperty("RepeatWeek", 0)
                params.addProperty("RepeatDay", 0)
                params.addProperty("IsNotiMail", false)
                params.addProperty("IsNotiSMS", false)
                params.addProperty("IsNotiNote", false)
            }

            3 -> {
                params.addProperty("RepeatType", 3)
                params.addProperty("StartTime", viewModel.requestMonthly.StartTime)
                params.addProperty("EndTime", viewModel.requestMonthly.EndTime)
                params.addProperty("StartDate", viewModel.requestMonthly.StartDate)
                params.addProperty("EndDate", viewModel.requestMonthly.EndDate)
                params.addProperty("IsAllDay", viewModel.requestMonthly.IsAllDay)
                params.addProperty("RepeatDOWs", 0)
                params.addProperty("IsNotiPopup", false)
                params.addProperty("NotiTimeType", 0)
                params.addProperty("RepeatMonth", 0)
                params.addProperty("ReservationNo", 0)
                params.addProperty("RepeatCount", viewModel.requestMonthly.RepeatCount)
                params.addProperty("RepeatWeek", 0)
                params.addProperty("IsNotiMail", false)
                params.addProperty("IsNotiSMS", false)
                params.addProperty("IsNotiNote", false)
                params.addProperty("IsLastDay", viewModel.requestMonthly.IsLastDay)
                params.addProperty("IsFiveWeek", viewModel.requestMonthly.IsFiveWeek)
                params.addProperty("RepeatDay", viewModel.requestMonthly.RepeatDay)
            }

            else -> {
                params.addProperty("RepeatType", 4)
                params.addProperty("StartTime", viewModel.requestAnnualy.StartTime)
                params.addProperty("EndTime", viewModel.requestAnnualy.EndTime)
                params.addProperty("StartDate", viewModel.requestAnnualy.StartDate)
                params.addProperty("EndDate", viewModel.requestAnnualy.EndDate)
                params.addProperty("IsAllDay", viewModel.requestAnnualy.IsAllDay)
                params.addProperty("RepeatDOWs", 0)
                params.addProperty("IsNotiPopup", false)
                params.addProperty("NotiTimeType", 0)
                params.addProperty("RepeatMonth", 0)
                params.addProperty("ReservationNo", 0)
                params.addProperty("RepeatCount", 0)
                params.addProperty("RepeatWeek", 0)
                params.addProperty("IsNotiMail", false)
                params.addProperty("IsNotiSMS", false)
                params.addProperty("IsNotiNote", false)
                params.addProperty("IsLastDay", viewModel.requestMonthly.IsLastDay)
                params.addProperty("IsFiveWeek", viewModel.requestMonthly.IsFiveWeek)
                params.addProperty("RepeatDay", viewModel.requestMonthly.RepeatDay)
            }
        }

        viewModel.insertResource(params)
    }

    private fun changeLayoutParams(position: Int) {
        val height = when(position) {
            0 -> 550
            1 -> 650
            2 -> 800
            3 -> 800
            else -> 850
        }
        val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height)
        binding?.vpTab?.layoutParams = params
    }

    override fun initView() {

    }

    override fun initViewModel() {
        organizationViewModel.organization.onEach {
            if(!it.isNullOrEmpty()) {
                dismissProgressDialog()
            }
        }.launchIn(lifecycleScope)

        organizationViewModel.nonChanged.onEach {
            if(it) {
                dismissProgressDialog()
            }
        }.launchIn(lifecycleScope)

        viewModel.errorMessage.observe(this) {
            it?.let {
                dismissProgressDialog()
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.insertStateShareFlow.onEach {
            if(it) {
                Toast.makeText(this, "Insert Resource Successfully!", Toast.LENGTH_LONG).show()
                finish()
            }
        }.launchIn(lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        lifecycle.addObserver(notificationSetting)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_RESOURCE && resultCode == RESULT_OK) {
            resourceTree = data?.getSerializableExtra(Constants.ORGANIZATION_TREE) as ResourceTree?
            binding?.tvResource?.text = resourceTree?.title?: ""
        } else if(requestCode == REQUEST_CODE_ORGANIZATION && resultCode == RESULT_OK) {
            val userListType = object : TypeToken<ArrayList<User?>?>() {}.type
            val list =  Gson().fromJson<ArrayList<User>>(
                data?.getStringExtra(Constants.SELECTED_CHOSEN),
                userListType
            )

            selected.clear()
            list.forEach {
                if(selected.find { user -> user.userNo == it.userNo } == null) {
                    selected.add(it)
                }
            }

            var result = ""
            selected.forEach {
                result += "; ${it.name}"
            }

            binding?.tvShareValue?.text = result.substring(2, result.length)
        }
    }
}