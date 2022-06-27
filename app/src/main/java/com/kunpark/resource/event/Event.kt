package com.kunpark.resource.event

import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.DazoneApplication

object Event {
    const val UPDATE_CONDITION_SEARCH = "UPDATE_CONDITION_SEARCH"
    const val PAGE_MONTH_CHANGE = "PAGE_MONTH_CHANGE"
    const val PAGE_WEEK_CHANGE = "PAGE_WEEK_CHANGE"
    const val PAGE_DAY_CHANGE = "PAGE_DAY_CHANGE"
    const val MOVE_TODAY = "MOVE_TODAY"
    const val ON_PAGE_MAIN_CHANGED = "ON_PAGE_MAIN_CHANGED"

    fun updateConditionSearch(conditionSearch: ConditionSearch?) {
        DazoneApplication.eventBus.onNext(hashMapOf(UPDATE_CONDITION_SEARCH to conditionSearch))
    }

    fun onPageMonthChange(strDate: String) {
        DazoneApplication.eventBus.onNext(hashMapOf(PAGE_MONTH_CHANGE to strDate))
    }

    fun onPageWeekChange(strDate: String) {
        DazoneApplication.eventBus.onNext(hashMapOf(PAGE_WEEK_CHANGE to strDate))
    }

    fun onPageDayChange(strDate: String) {
        DazoneApplication.eventBus.onNext(hashMapOf(PAGE_DAY_CHANGE to strDate))
    }

    fun onMoveToday() {
        DazoneApplication.eventBus.onNext(hashMapOf(MOVE_TODAY to ""))
    }

    fun onPageMainChanged(page: Int) {
        DazoneApplication.eventBus.onNext(hashMapOf(ON_PAGE_MAIN_CHANGED to page))
    }
}