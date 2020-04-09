package com.example.perfectcalc.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
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
import com.example.perfectcalc.util.*
import com.example.perfectcalc.vm.CalculatorComplexViewModel


import com.example.perfectcalc.vm.ResultsViewModel
import kotlinx.android.synthetic.main.fragment_buttons_complex.*

class ComplexButtonsFragment : Fragment() {
    lateinit var calculatorViewModel: CalculatorComplexViewModel
    lateinit var resultsViewModel: ResultsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buttons_complex, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        calculatorViewModel = ViewModelProvider(activity!!).get(CalculatorComplexViewModel::class.java)
        resultsViewModel = ViewModelProvider(activity!!).get(ResultsViewModel::class.java)
        calculatorViewModel.resultLiveData.observe(viewLifecycleOwner, Observer{
            resultsViewModel.showResult(it)
        })
        calculatorViewModel.lastResultLiveData.observe(viewLifecycleOwner, Observer{
            resultsViewModel.addLastResult(it)
        })
        calculatorViewModel.needClearLastResults.observe(viewLifecycleOwner, Observer {
            resultsViewModel.clearLastResults()
        })
        calculatorViewModel.memoryEnabled.observe(viewLifecycleOwner, Observer {
            setMemoryEnabled(it)
        })
        editTextRootNumber.setText(calculatorViewModel.rootNumber.toString())
        editTextRootDegree.setText(calculatorViewModel.rootDegree.toString())
        editTextRootNumber.doOnTextChanged { text, start, count, after ->
            calculatorViewModel.setRootNumber(text?.toString()?.toIntOrNull() ?: calculatorViewModel.rootNumber)
        }
        editTextRootDegree.doOnTextChanged { text, start, count, after ->
            calculatorViewModel.setRootDegree(text?.toString()?.toIntOrNull() ?: calculatorViewModel.rootNumber)
        }
        setButtonProcessors()
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
        buttonRoot.setOnClickListener {
            calculatorViewModel.pressButton(OperatorButton(Operator.ROOT))
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
        buttonImage.setOnClickListener {
            calculatorViewModel.pressButton(
                CommandButton(
                    EditorCommand(
                        EditorCommands.ADD_DIVIDER
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


    private fun setMemoryEnabled(isEnabled: Boolean = false) {
        buttonMADD.isEnabled = isEnabled
        buttonMR.isEnabled = isEnabled
        buttonMC.isEnabled = isEnabled
    }

}