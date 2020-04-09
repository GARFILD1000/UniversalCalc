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
import com.example.perfectcalc.vm.ResultsViewModel
import kotlinx.android.synthetic.main.fragment_buttons_pnumber.*

class PNumberButtonsFragment : Fragment() {
    lateinit var calculatorViewModel: CalculatorPNumberViewModel
    lateinit var resultsViewModel: ResultsViewModel

    private lateinit var digitButtons: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buttons_pnumber, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createDigitButtonsList()
        calculatorViewModel = ViewModelProvider(activity!!).get(CalculatorPNumberViewModel::class.java)
        resultsViewModel = ViewModelProvider(activity!!).get(ResultsViewModel::class.java)
        calculatorViewModel.currentNumberBase.observe(viewLifecycleOwner, Observer {

        })
        calculatorViewModel.resultLiveData.observe(viewLifecycleOwner, Observer{
            resultsViewModel.showResult(it)
        })
        calculatorViewModel.lastResultLiveData.observe(viewLifecycleOwner, Observer{
            resultsViewModel.addLastResult(it)
        })
        calculatorViewModel.currentNumberBase.observe(viewLifecycleOwner, Observer {
            setDigitsEnabled(it)
        })
        calculatorViewModel.needClearLastResults.observe(viewLifecycleOwner, Observer {
            resultsViewModel.clearLastResults()
        })
        calculatorViewModel.memoryEnabled.observe(viewLifecycleOwner, Observer {
            setMemoryEnabled(it)
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
        val baseAdapter = ArrayAdapter<NumberBase>(context!!, R.layout.support_simple_spinner_dropdown_item, NumberBaseHelper.ALL)
        spinnerBase.adapter = baseAdapter
        spinnerBase.setSelection(NumberBaseHelper.ALL.indexOf(NumberBase.TEN))
        spinnerBase.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                baseAdapter.getItem(position)?.let{
                    calculatorViewModel.setNumberBase(it)
                }
            }
        }

        val precisionAdapter = ArrayAdapter<Int>(context!!, R.layout.support_simple_spinner_dropdown_item, CalculatorPNumberViewModel.precisions)
        spinnerPrecision.adapter = precisionAdapter
        spinnerPrecision.setSelection(CalculatorPNumberViewModel.defaultPrecisionIdx)
        spinnerPrecision.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                precisionAdapter.getItem(position)?.let{
                    calculatorViewModel.setPrecision(it)
                }
            }

        }
    }

    private fun setButtonProcessors() {
        buttonEqual.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.EQUALS))
        }
        buttonChangeSign.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.CHANGE_SIGN
                    )
                )
            )
        }
        buttonReverse.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.REVERSE))
        }
        buttonRoot.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.SQRT))
        }
        buttonPow.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.POW))
        }
        buttonAdd.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.ADD))
        }
        buttonSub.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.SUB))
        }
        buttonMul.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.MUL))
        }
        buttonDiv.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.DIV))
        }
        buttonSub.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.SUB))
        }

        buttonBackspace.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.BACKSPACE
                    )
                )
            )
        }
        buttonClear.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.CLEAR
                    )
                )
            )
        }
        buttonDot.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.ADD_DOT
                    )
                )
            )
        }

        buttonDgt0.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT0))
        }
        buttonDgt1.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT1))
        }
        buttonDgt2.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT2))
        }
        buttonDgt3.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT3))
        }
        buttonDgt4.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT4))
        }
        buttonDgt5.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT5))
        }
        buttonDgt6.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT6))
        }
        buttonDgt7.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT7))
        }
        buttonDgt8.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT8))
        }
        buttonDgt9.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGIT9))
        }
        buttonDgtA.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGITA))
        }
        buttonDgtB.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGITB))
        }
        buttonDgtC.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGITC))
        }
        buttonDgtD.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGITD))
        }
        buttonDgtE.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGITE))
        }
        buttonDgtF.setOnClickListener {
            calculatorViewModel.pressButton(DigitButton(DIGITF))
        }

        buttonMADD.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    MemoryCommand(
                        MemoryCommands.MADD
                    )
                )
            )
        }
        buttonMS.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    MemoryCommand(
                        MemoryCommands.MSAVE
                    )
                )
            )
        }
        buttonMR.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    MemoryCommand(
                        MemoryCommands.MRESTORE
                    )
                )
            )
        }
        buttonMC.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    MemoryCommand(
                        MemoryCommands.MCLEAR
                    )
                )
            )
        }
    }

    private fun setDigitsEnabled(numberBase: NumberBase) {
        for (i in digitButtons.indices) {
            digitButtons.get(i).isEnabled = i < numberBase.base
        }
    }

    private fun setMemoryEnabled(isEnabled: Boolean = false) {
        buttonMADD.isEnabled = isEnabled
        buttonMR.isEnabled = isEnabled
        buttonMC.isEnabled = isEnabled
    }

}