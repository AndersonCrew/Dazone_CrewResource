package com.kunpark.resource.view.add_schedule

import android.os.Bundle
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import kotlinx.android.synthetic.main.activity_organizaion.*

class ResourceListActivity: BaseActivity() {
    override fun initView() {

    }

    override fun initEvent() {
        icBack?.setOnClickListener { onBackPressed() }
    }

    override fun initViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizaion)
    }
}