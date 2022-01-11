package com.kunpark.resource.view

import android.os.Bundle
import android.os.Handler
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.view.login.LoginActivity
import com.kunpark.resource.view.main.MainActivity

class IntroActivity : BaseActivity() {
    override fun initEvent() {

    }

    override fun initView() {
        Handler().postDelayed({
            if(DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, "") == "") {
                callActivity(LoginActivity::class.java)
            } else {
                callActivity(MainActivity::class.java)
            }

            finish()
        }, 1500)
    }

    override fun initViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }
}