package com.kunpark.resource.view.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.event.Event
import com.kunpark.resource.model.CalendarType
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.utils.TimeUtils
import com.kunpark.resource.view.add_schedule.AddScheduleActivity
import com.kunpark.resource.view.settings.SettingActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_footer_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModels ()
    private var imgSetting: ImageView? = null
    private var imgAvatar: CircleImageView? = null
    private var tvFullName: TextView? = null
    private var tvKoreanName: TextView? = null
    private var csMyResrc: ConstraintLayout? = null
    private var rvMyResource: RecyclerView? = null
    private var icDropDown: ImageView? = null
    private var currentConditionType = CalendarType.AGENDA
    private var currentCal = Calendar.getInstance()
    private var adapter: ConditionSearchAdapter?= null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initEvent() {
        imgSetting?.setOnClickListener {
            callActivity(SettingActivity::class.java)
        }

        fab?.setOnClickListener {
            callActivity(AddScheduleActivity::class.java)
        }

        csMyResrc?.setOnClickListener {
            if(rvMyResource?.visibility == View.GONE) {
                rvMyResource?.visibility = View.VISIBLE
                icDropDown?.setImageResource(R.drawable.ic_drop_up)
            } else {
                rvMyResource?.visibility = View.GONE
                icDropDown?.setImageResource(R.drawable.ic_drop_down)
            }
        }


        csAgenda?.setOnClickListener { openCalendar(CalendarType.AGENDA, currentCal) }
        csCalendarDay?.setOnClickListener { openCalendar(CalendarType.DAY, currentCal) }
        csCalendarWeek?.setOnClickListener { openCalendar(CalendarType.WEEK, currentCal) }
        csCalendarMonth?.setOnClickListener { openCalendar(CalendarType.MONTH, currentCal) }
        icSlideMenu?.setOnClickListener { drawerLayout?.openDrawer(GravityCompat.START) }
        tvToday?.setOnClickListener { Event.onMoveToday() }
    }

    @SuppressLint("SimpleDateFormat")
    override fun initView() {
        imgAvatar = findViewById(R.id.imgAvatar)
        tvFullName = findViewById(R.id.tvName)
        tvKoreanName = findViewById(R.id.tvKoreanName)
        imgSetting = findViewById(R.id.imgSetting)
        csMyResrc = findViewById(R.id.csMyResrc)
        rvMyResource = findViewById(R.id.rvMyResource)
        icDropDown = findViewById(R.id.icDropDown)

        tvTitle?.text = SimpleDateFormat(Constants.MM_YYYY).format(Calendar.getInstance().time)
        vpMain?.adapter = MainAdapter(supportFragmentManager)
        vpMain?.offscreenPageLimit = 4
    }

    override fun initViewModel() {
        homeViewModel.getUser()?.observe(this, Observer {
            it?.let {
                bindData(it)
            }
        })

        homeViewModel.getConditionSearchDB()?.observe(this, Observer {
            if(it.isNullOrEmpty()) {
                getConditionList()
            } else {
                bindingListCondition(it)
            }
        })
    }

    private fun bindData(userInfo: User) {
        val urlAvatar = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "") + userInfo.avatar
        imgAvatar?.let {
            Glide.with(this).load(urlAvatar).into(it)
        }

        tvFullName?.text = userInfo.name?: "-"
        tvKoreanName?.text = userInfo.companyName?: "-"
    }

    @SuppressLint("SimpleDateFormat")
    private fun openCalendar(type: CalendarType, cal : Calendar?) {
        currentConditionType = type
        vpMain?.currentItem = CalendarType.getType(type)?: 0
        Event.onMoveToday()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun getConditionList() {
        val params = JsonObject()
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("sessionId", DazoneApplication.getInstance().mPref?.getString(Constants.ACCESS_TOKEN, ""))

        homeViewModel.getConditionSearch(params)
    }

    private fun bindingListCondition(conditionList: List<ConditionSearch>) {
        for(condition in conditionList) {
            condition.isCheck = conditionList.indexOf(condition) == conditionList.size - 1
        }

        rvMyResource?.layoutManager = LinearLayoutManager(this)
        adapter = ConditionSearchAdapter(conditionList) { conditionSearch ->
            Event.updateConditionSearch(conditionSearch)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        rvMyResource?.adapter = adapter
    }

    @SuppressLint("SimpleDateFormat")
    override fun onEventReceive(it: Map<String, Any?>) {
        super.onEventReceive(it)

        it[Event.PAGE_MONTH_CHANGE]?.let {
            val strDay = (it as String).split("-")
            if(strDay.size == 3) {
                val day = Integer.parseInt(strDay[0])
                val month = Integer.parseInt(strDay[1])
                val year = Integer.parseInt(strDay[2])
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                tvTitle?.text = CalendarType.getStrDate(currentConditionType, cal?: Calendar.getInstance())
            }
        }

        it[Event.PAGE_DAY_CHANGE]?.let {
            val strDay = (it as String)
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat("dd/MM/yyyy").parse(strDay)
            tvTitle?.text = CalendarType.getStrDate(currentConditionType, cal?: Calendar.getInstance())
        }
    }
}