package com.kunpark.resource.view.add_schedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.databinding.ActivityAddScheduleBinding
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.OrganizationChart
import com.kunpark.resource.utils.Constants
import kotlinx.android.synthetic.main.activity_add_schedule.*


class AddScheduleActivity : BaseActivity() {
    private lateinit var viewModel : AddScheduleViewModel
    private var binding: ActivityAddScheduleBinding?= null
    private var organization: Organization?= null
    private var REQUEST_CODE_RESOURCE = 111
    private var REQUEST_CODE_ORGANIZATION = 111
    override fun initEvent() {
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
            //startActivityForResult(intent, REQUEST_CODE_RESOURCE)
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode == RESULT_OK) {
                    organization = it?.data?.getSerializableExtra(Constants.ORGANIZATION) as Organization?
                    tvResource?.text = organization?.title?: ""
                }
            }.launch(intent)
        }

        binding?.tvShareValue?.setOnClickListener {
            val intent = Intent(this, OrganizationActivity::class.java)
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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        lifecycle.addObserver(notificationSetting)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_RESOURCE) {
            organization = data?.getSerializableExtra(Constants.ORGANIZATION) as Organization?
                    tvResource?.text = organization?.title?: ""
        } else if(requestCode == REQUEST_CODE_ORGANIZATION) {
            organization = data?.getSerializableExtra(Constants.ORGANIZATION) as Organization?
            tvShareValue?.text = organization?.title?: ""
        }
    }
}