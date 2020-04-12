package com.example.perfectcalc.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.perfectcalc.R
import com.example.perfectcalc.adapter.ButtonsPagerAdapter
import kotlinx.android.synthetic.main.fragment_calculator.*

class CalculatorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPagerButtons.adapter = ButtonsPagerAdapter(activity!!.supportFragmentManager)
    }
}