package com.example.perfectcalc.useless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.perfectcalc.R
import com.example.perfectcalc.vm.CalculatorViewModel
import kotlinx.android.synthetic.main.fragment_calculator_buttons.*

class CalculatorButtonsFragment : Fragment() {
    lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculator_buttons, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        calculatorViewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)
        calculatorViewModel.moreControlsEnabled.observe(viewLifecycleOwner, Observer {
            showAdditionControls(
                if (it == true) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            )
        })
    }

    private fun showAdditionControls(visibility: Int = View.VISIBLE) {
        buttonExpNumber.visibility = visibility
        buttonPiNumber.visibility = visibility
        buttonCos.visibility = visibility
        buttonSin.visibility = visibility
        buttonTan.visibility = visibility
        buttonRad.visibility = visibility
        buttonLn.visibility = visibility
        buttonLog.visibility = visibility
        buttonFact.visibility = visibility
        buttonScopeLeft.visibility = visibility
        buttonScopeRight.visibility = visibility
        buttonRoot.visibility = visibility
        buttonPow.visibility = visibility
        buttonReverse.visibility = visibility
    }

}