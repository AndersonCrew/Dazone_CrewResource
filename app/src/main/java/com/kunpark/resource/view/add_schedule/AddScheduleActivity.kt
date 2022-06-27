package com.kunpark.resource.view.add_schedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import androidx.activity.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.model.Organization
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_add_schedule.*
import java.util.*

class AddScheduleActivity : BaseActivity() {
    private val viewModel: AddScheduleViewModel by viewModels()
    private var organization: Organization?= null
    override fun initEvent() {
        imgShowContent?.setOnClickListener {
            if(etContent?.visibility == View.GONE) {
                etContent?.visibility = View.VISIBLE
                imgShowContent?.setImageResource(R.drawable.ic_drop_up)
            } else if(etContent?.visibility == View.VISIBLE) {
                etContent?.visibility = View.GONE
                imgShowContent?.setImageResource(R.drawable.ic_drop_down)
            }
        }

        imgBack?.setOnClickListener { onBackPressed() }
        vpTab?.adapter = TabAddResource(supportFragmentManager)
        vpTab?.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tabLayout?.addTab(tabLayout.newTab())
        tabLayout?.setupWithViewPager(vpTab)

        tvResource?.setOnClickListener {
            val intent = Intent(this, OrganizationalChartActivity::class.java)
            startActivityForResult(intent, Constants.REQUEST_CODE_ORGANIZATION)
        }


    }

    override fun initView() {

    }

    override fun initViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)
        lifecycle.addObserver(notificationSetting)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Constants.REQUEST_CODE_ORGANIZATION) {
            organization = data?.getSerializableExtra(Constants.ORGANIZATION) as Organization?
            tvResource?.text = organization?.title?: ""
        }
    }
}