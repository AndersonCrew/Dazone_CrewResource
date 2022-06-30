package com.kunpark.resource.event

import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.utils.DazoneApplication

object Event {
    const val UPDATE_CONDITION_SEARCH = "UPDATE_CONDITION_SEARCH"
    const val PAGE_TITLE_DATE_CHANGE = "PAGE_TITLE_DATE_CHANGE"
    const val MOVE_TODAY = "MOVE_TODAY"

    fun updateConditionSearch(conditionSearch: ConditionSearch?) {
        DazoneApplication.eventBus.onNext(hashMapOf(UPDATE_CONDITION_SEARCH to conditionSearch))
    }

    fun onTitleDateChange(strDate: String) {
        DazoneApplication.eventBus.onNext(hashMapOf(PAGE_TITLE_DATE_CHANGE to strDate))
    }

    fun onMoveToday() {
        DazoneApplication.eventBus.onNext(hashMapOf(MOVE_TODAY to ""))
    }
}