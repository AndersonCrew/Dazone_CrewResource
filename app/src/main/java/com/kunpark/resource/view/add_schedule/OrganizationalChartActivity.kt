package com.kunpark.resource.view.add_schedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.model.Organization
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.utils.UtilsViewModel
import kotlinx.android.synthetic.main.activity_organizaion.*
import java.util.*
import kotlin.collections.ArrayList

class OrganizationalChartActivity: BaseActivity() {
    private val viewModel: UtilsViewModel by viewModels()
    override fun initView() {

    }

    override fun initEvent() {
        icBack?.setOnClickListener { onBackPressed() }
    }

    override fun initViewModel() {
        viewModel.getOrganizations()?.observe(this, androidx.lifecycle.Observer {
            if(!it.isNullOrEmpty()) {
                setUpRecyclerView(it)
            } else {
                val params = JsonObject()
                params.addProperty("languageCode", Locale.getDefault().language)
                params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
                params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(
                    Constants.ACCESS_TOKEN, ""))

                viewModel.getOrganizationFromServer(params)
            }
        })
    }

    private fun setUpRecyclerView(list: List<Organization>) {
        rvOrganization?.adapter = OrganizationAdapter(list, itemClick = {organization ->
            val intent = Intent()
            intent.putExtra(Constants.ORGANIZATION, organization)
            setResult(Constants.REQUEST_CODE_ORGANIZATION, intent)
            finish()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizaion)
    }
}