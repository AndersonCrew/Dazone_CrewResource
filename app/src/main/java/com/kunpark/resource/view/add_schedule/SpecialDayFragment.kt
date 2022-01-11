package com.kunpark.resource.view.add_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseFragment

class SpecialDayFragment: BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_special_day, container, false)
        return root
    }
}