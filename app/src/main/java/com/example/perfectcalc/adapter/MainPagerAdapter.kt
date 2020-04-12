package com.example.perfectcalc.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.perfectcalc.ui.fragment.CalculatorFragment
import com.example.perfectcalc.ui.fragment.ConverterFragment


class MainPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val fragments = listOf<Fragment>(
        CalculatorFragment(),
        ConverterFragment()
    )

    val titles = listOf(
        "Calculator",
        "Converter"
    )

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles.get(position)
    }


}