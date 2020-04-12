package com.example.perfectcalc.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.perfectcalc.R
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT0
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT1
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT2
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT3
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT4
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT5
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT6
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT7
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT8
import com.example.perfectcalc.useless.ExpressionConstants.DIGIT9
import com.example.perfectcalc.useless.ExpressionConstants.DIGITA
import com.example.perfectcalc.useless.ExpressionConstants.DIGITB
import com.example.perfectcalc.useless.ExpressionConstants.DIGITC
import com.example.perfectcalc.useless.ExpressionConstants.DIGITD
import com.example.perfectcalc.useless.ExpressionConstants.DIGITE
import com.example.perfectcalc.useless.ExpressionConstants.DIGITF
import com.example.perfectcalc.util.*
import com.example.perfectcalc.vm.CalculatorPNumberViewModel


import com.example.perfectcalc.vm.ConverterViewModel
import kotlinx.android.synthetic.main.fragment_buttons_converter.*
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonBackspace
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonClear
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt0
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt1
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt2
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt3
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt4
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt5
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt6
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt7
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt8
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgt9
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgtA
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgtB
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgtC
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgtD
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgtE
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDgtF
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonDot
import kotlinx.android.synthetic.main.fragment_buttons_converter.buttonEqual
import kotlinx.android.synthetic.main.fragment_buttons_converter.spinnerPrecision
import kotlinx.android.synthetic.main.fragment_buttons_pnumber.*

class ConverterButtonsFragment : Fragment() {
    lateinit var converterViewModel: ConverterViewModel

    private lateinit var digitButtons: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buttons_converter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createDigitButtonsList()
        converterViewModel = ViewModelProvider(activity!!).get(ConverterViewModel::class.java)
        converterViewModel.currentNumberBase.observe(viewLifecycleOwner, Observer {
            setDigitsEnabled(it)
        })
        setButtonProcessors()
        prepareSpinners()
    }

    private fun createDigitButtonsList() {
        digitButtons = listOf(
            buttonDgt0, buttonDgt1, buttonDgt2, buttonDgt3,
            buttonDgt4, buttonDgt5, buttonDgt6, buttonDgt7,
            buttonDgt8, buttonDgt9, buttonDgtA, buttonDgtB,
            buttonDgtC, buttonDgtD, buttonDgtE, buttonDgtF
        )
    }

    private fun prepareSpinners() {
        val baseFromAdapter = ArrayAdapter<NumberBase>(context!!, R.layout.support_simple_spinner_dropdown_item, NumberBaseHelper.ALL)
        spinnerBaseFrom.adapter = baseFromAdapter
        spinnerBaseFrom.setSelection(NumberBaseHelper.ALL.indexOf(ConverterViewModel.defaultFromNumberBase))
        spinnerBaseFrom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                baseFromAdapter.getItem(position)?.let{
                    converterViewModel.setCurrentNumberBase(it)
                }
            }
        }

        val baseToAdapter = ArrayAdapter<NumberBase>(context!!, R.layout.support_simple_spinner_dropdown_item, NumberBaseHelper.ALL)
        spinnerBaseTo.adapter = baseToAdapter
        spinnerBaseTo.setSelection(NumberBaseHelper.ALL.indexOf(ConverterViewModel.defaultToNumberBase))
        spinnerBaseTo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                baseToAdapter.getItem(position)?.let{
                    converterViewModel.setDestinationNumberBase(it)
                }
            }
        }

        val precisionAdapter = ArrayAdapter<Int>(context!!, R.layout.support_simple_spinner_dropdown_item, ConverterViewModel.precisions)
        spinnerPrecision.adapter = precisionAdapter
        spinnerPrecision.setSelection(ConverterViewModel.defaultPrecisionIdx)
        spinnerPrecision.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                precisionAdapter.getItem(position)?.let{
                    converterViewModel.setPrecision(it)
                }
            }
        }
    }

    private fun setButtonProcessors() {
        buttonEqual.setOnClickListener {
            converterViewModel.pressButton(OperatorButton(Operator.EQUALS))
        }

        buttonBackspace.setOnClickListener {
            converterViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.BACKSPACE
                    )
                )
            )
        }
        buttonClear.setOnClickListener {
            converterViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.CLEAR
                    )
                )
            )
        }
        buttonDot.setOnClickListener {
            converterViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.ADD_DOT
                    )
                )
            )
        }

        buttonDgt0.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT0))
        }
        buttonDgt1.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT1))
        }
        buttonDgt2.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT2))
        }
        buttonDgt3.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT3))
        }
        buttonDgt4.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT4))
        }
        buttonDgt5.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT5))
        }
        buttonDgt6.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT6))
        }
        buttonDgt7.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT7))
        }
        buttonDgt8.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT8))
        }
        buttonDgt9.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGIT9))
        }
        buttonDgtA.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGITA))
        }
        buttonDgtB.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGITB))
        }
        buttonDgtC.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGITC))
        }
        buttonDgtD.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGITD))
        }
        buttonDgtE.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGITE))
        }
        buttonDgtF.setOnClickListener {
            converterViewModel.pressButton(DigitButton(DIGITF))
        }
    }

    private fun setDigitsEnabled(numberBase: NumberBase) {
        for (i in digitButtons.indices) {
            digitButtons.get(i).isEnabled = i < numberBase.base
        }
    }

}