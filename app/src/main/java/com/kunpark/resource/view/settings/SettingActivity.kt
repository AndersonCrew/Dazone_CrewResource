package com.kunpark.resource.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.database.DazoneDatabase
import com.kunpark.resource.dialog.DialogUtil
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        csLogout.setOnClickListener {
            DialogUtil(this).showDialogConfirm(getString(R.string.str_logout)) {
                DazoneApplication.getInstance().mPref?.logout()
                val scope = CoroutineScope(Dispatchers.Default)
                scope.launch {
                    DazoneDatabase.getDatabase(this@SettingActivity).clearAllTables()
                }
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
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