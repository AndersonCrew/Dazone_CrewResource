package com.kunpark.resource.view.add_schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kunpark.resource.base.BaseFragment

class TabAddResource(fr: FragmentManager): FragmentStatePagerAdapter(fr, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val fragment: BaseFragment
        when(position) {
            0 -> {
                fragment = SpecialDayFragment()
            }

            1-> {
                fragment = DailyFragment()
            }

            2-> {
                fragment = WeeklyFragment()
            }

            3-> {
                fragment = MonthlyFragment()
            }

            else -> {
                fragment = AnnuallyFragment()
            }
        }

        return fragment
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Special Day"
            1 -> "Daily"
            2 -> "Weekly"
            3 -> "Monthly"
            else -> "Annually"
        }
        return null
    }
}