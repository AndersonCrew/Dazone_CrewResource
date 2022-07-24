package com.kunpark.resource.view.add_schedule

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.google.gson.JsonObject
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.databinding.ActivityOrganizationBinding
import com.kunpark.resource.databinding.ActivityResourceListBinding
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.ResourceTree
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.utils.UtilsViewModel
import java.util.*
import kotlin.collections.ArrayList

class ResourceChartActivity: BaseActivity() {
    private val viewModel: UtilsViewModel by viewModels()
    private lateinit var adapter: ResourceChartAdapter
    private var binding: ActivityResourceListBinding?= null
    override fun initView() {
    }

    override fun initEvent() {

    }

    override fun initViewModel() {
        viewModel.getResourceDB()?.observe(this) {
            showProgressDialog()
            if (it != null) {
                setUpRecyclerView(it)
                Handler(Looper.getMainLooper()).postDelayed({
                    dismissProgressDialog()
                }, 1000)

            } else {
                val params = JsonObject()
                params.addProperty("languageCode", Locale.getDefault().language)
                params.addProperty(
                    "timeZoneOffset",
                    TimeUtils.getTimezoneOffsetInMinutes().toString()
                )
                params.addProperty(
                    "sessionId", DazoneApplication.getInstance().mPref?.getString(
                        Constants.ACCESS_TOKEN, ""
                    )
                )

                viewModel.getResourceChartFromServer(params)
            }
        }
    }

    private fun setUpRecyclerView(resourceTree: ResourceTree) {
        val list: ArrayList<ResourceTree> = arrayListOf()
        list.add(resourceTree)
        adapter = ResourceChartAdapter() {
                val intent = Intent()
                intent.putExtra(Constants.ORGANIZATION_TREE, it)
                setResult(RESULT_OK, intent)
                finish()
            }

        binding?.rvResource?.adapter = adapter
        adapter.submitList(list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceListBinding.inflate(layoutInflater)

        binding?.icBackResource?.setOnClickListener {
            onBackPressed()
        }

        setContentView(binding?.root)
    }
}