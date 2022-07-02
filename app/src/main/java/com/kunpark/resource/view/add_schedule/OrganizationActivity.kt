package com.kunpark.resource.view.add_schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.databinding.ActivityOrganizaionBinding
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

class OrganizationActivity : BaseActivity() {
    private var binding: ActivityOrganizaionBinding?= null
    private val viewModel: OrganizationViewModel by viewModels()
    private lateinit var adapter: OrganizationAdapter
    private var hasCallAPI = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrganizaionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        adapter = OrganizationAdapter(arrayListOf())
        binding?.rvOrganization?.adapter = adapter
    }

    override fun initEvent() {

    }

    override fun initViewModel() {
        viewModel.getOrganization()?.observe(this) {
            if(it != null && !it.list.isNullOrEmpty()) {
                adapter.updateList(it.list!!)
            }

            if(!hasCallAPI) {
                hasCallAPI = true
                val params = JsonObject()

                params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(
                    Constants.ACCESS_TOKEN, ""))
                params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
                params.addProperty("languageCode", Locale.getDefault().language)
                viewModel.getDepartments(params)
            }
        }
    }
}