package com.kunpark.resource.view.detail_schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.model.Resource
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_detail_schedule.*
import kotlinx.android.synthetic.main.activity_detail_schedule.imgBack
import kotlinx.android.synthetic.main.activity_detail_schedule.notificationSetting
import kotlinx.android.synthetic.main.activity_detail_schedule.tvResource
import kotlinx.android.synthetic.main.activity_detail_schedule.tvShare
import java.util.*

class DetailScheduleActivity: BaseActivity(){
    private var resource: Resource?= null
    private val viewModel: DetailScheduleViewModel by viewModels ()
    companion object {
        fun start(baseActivity: BaseActivity, src: Resource) {
            val intent = Intent(baseActivity, DetailScheduleActivity::class.java)
            intent.putExtra(Constants.RESOURCE, src)
            baseActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_schedule)
        lifecycle.addObserver(notificationSetting)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        intent?.let {
            resource = it.getSerializableExtra(Constants.RESOURCE) as Resource

            tvTitle?.text = resource?.title?: "-"
            tvStartDate?.text = resource?.startStr
            tvTimeValue?.text = "${resource?.startTime} - ${resource?.endTime}"
        }

    }

    override fun initEvent() {
        imgBack?.setOnClickListener { onBackPressed() }
    }

    override fun initViewModel() {
        viewModel.resourceLiveDate.observe(this, Observer {
            if(it != null){
                tvContent?.text = it.content?: ""
                tvWriter?.text = it.rsvnUserName?: ""
                tvResource?.text = resource?.resourceName?: ""
            } else {
                val params = JsonObject()
                params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))
                params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
                params.addProperty("languageCode", Locale.getDefault().language)
                params.addProperty("RsvnNo", resource?.id?: 0)
                viewModel.getDetailViewModel(params)
            }
        })

        viewModel.participantLiveDate.observe(this, Observer {
            if(!it.isNullOrEmpty()) {
                var participants = ""
                for(participant in it) {
                    participants += "${participant.name}, "
                }

                participants = participants.substring(0, participants.length - 2)
                tvShare?.text = participants
            }
        })

        viewModel.notificationLiveDate.observe(this, Observer {
            if(!it.isNullOrEmpty()) {
                notificationSetting?.addListSchedule(it)
            }
        })
    }
}