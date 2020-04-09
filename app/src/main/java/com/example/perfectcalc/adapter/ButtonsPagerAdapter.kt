package com.example.perfectcalc.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.perfectcalc.ui.fragment.ComplexButtonsFragment
import com.example.perfectcalc.ui.fragment.FractionButtonsFragment
import com.example.perfectcalc.ui.fragment.PNumberButtonsFragment

class ButtonsPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val fragments = listOf<Fragment>(
        PNumberButtonsFragment(),
        FractionButtonsFragment(),
        ComplexButtonsFragment()
    )

    val titles = listOf(
        "PNumber",
        "Fraction",
        "Complex"
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