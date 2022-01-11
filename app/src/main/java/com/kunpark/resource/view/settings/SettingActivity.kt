package com.kunpark.resource.view.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    private val settingViewModel: SettingViewModel by viewModels ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }

    override fun initEvent() {
        imgBack?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initView() {

    }

    override fun initViewModel() {
        settingViewModel.getUser()?.observe(this, Observer {
            it?.let {
                bindData(it)
            }
        })
    }

    private fun bindData(userInfo: User) {
        val urlAvatar = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "") + userInfo.avatar
        imgAvatar?.let {
            Glide.with(this).load(urlAvatar).into(it)
        }
    }
}