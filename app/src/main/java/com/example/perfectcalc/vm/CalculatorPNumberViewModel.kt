package com.example.perfectcalc.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perfectcalc.model.ErrorObject
import com.example.perfectcalc.util.*
import java.lang.Exception

class CalculatorPNumberViewModel : ViewModel() {
    companion object {
        val LOG_TAG = "CalculatorPNumberVM"
        val precisions = arrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)
        val defaultPrecisionIdx = 3
        val defaultPrecision = 4
        val defaultNumberBase = NumberBase.TEN
        val defaultValue = TPNumber(0.0, defaultNumberBase, defaultPrecision)
    }

    private var memory = TMemory<TPNumber>()
    private var processor = TProcessor<TPNumber>()
    private var editor = TPNumberEditor()

    private var lastOperator: Operator? = null

    private var newNumberInput = false
    private var needUpdateLastResult = true

    var result: TPNumber? = null
    private set

    val resultLiveData = MutableLiveData<String>().apply { postValue(editor.number) }
    val errorLiveData = MutableLiveData<ErrorObject?>().apply {postValue(null)}
    var error : ErrorObject? = null
    private set
    val lastResultLiveData = MutableLiveData<String>().apply { postValue("") }
    val needClearLastResults = MutableLiveData<Boolean>().apply { postValue(false) }

    val lastPressedButton = MutableLiveData<CalculatorButton>()
    val currentNumberBase = MutableLiveData<NumberBase>().apply { postValue(defaultNumberBase) }
    val currentPrecision = MutableLiveData<Int>().apply { postValue(defaultPrecision) }

    val memoryEnabled = MutableLiveData<Boolean>().apply { postValue(false) }

    init {
        initProcessor()
    }

    private fun initProcessor() {
        processor.setOperator(null)
        processor.setLeftOperandResult(null)
        processor.setRightOperand(null)
    }

    fun pressButton(button: CalculatorButton) {
        setError(null)
        lastPressedButton.postValue(button)
        if (button is DigitButton) {
            processDigitButton(button)
        } else if (button is CommandButton) {
            processCommandButton(button)
        } else if (button is OperatorButton) {
            processOperatorButton(button)
        }
    }

    private fun processDigitButton(button: DigitButton) {
        if (newNumberInput) {
            editor.clear()
            newNumberInput = false
        }
        editor.editNumber(EditorCommands.ADD_DIGIT, button.digit)
        resultLiveData.postValue(editor.number)
    }

    private fun processCommandButton(button: CommandButton) {
        val command = button.command
        if (command is EditorCommand) {
            processEditorCommand(command)
        } else if (command is MemoryCommand) {
            processMemoryCommand(command)
        }
    }

    private fun processEditorCommand(command: EditorCommand) {
        when(command.command) {
            EditorCommands.CLEAR -> {
                clearAll()
            }
        }
        editor.editNumber(command.command)
        resultLiveData.postValue(editor.number)
    }

    private fun processMemoryCommand(command: MemoryCommand) {
        when(command.command) {
            MemoryCommands.MSAVE -> {
                memory.setValue(getCurrentNumber())
            }
            MemoryCommands.MCLEAR -> {
                memory.clear()
            }
            MemoryCommands.MADD -> {
                memory.add(getCurrentNumber())
            }
            MemoryCommands.MRESTORE -> {
                editor.number = memory.getValue()?.toString(currentNumberBase.value!!) ?: TPNumberEditor.ZERO
                newNumberInput = false
                resultLiveData.postValue(editor.number)
            }
        }
        memoryEnabled.postValue(memory.fstate)
    }

    private fun processOperatorButton(button: OperatorButton) {
        val currentOperator = button.operator
        val currentOperand = getCurrentNumber()

        if (newNumberInput) {
            lastOperator = button.operator
            return
        }

        if (button.operator != Operator.EQUALS) {
            if (lastOperator == Operator.EQUALS) {
                processor.setOperator(null)
                processor.setLeftOperandResult(currentOperand)
                needUpdateLastResult = false
            } else {
                needUpdateLastResult = true
            }

            if (processor.getLeftOperandResult() == null || processor.getOperator()?.isBinary == false) {
                processor.setLeftOperandResult(currentOperand)
            } else {
                processor.setRightOperand(currentOperand)
                launchProcessor()
            }

            processor.setOperator(currentOperator)

            newNumberInput = currentOperator.isBinary
            if (!currentOperator.isBinary) {
                launchProcessor()
            }

            result = processor.getLeftOperandResult()
            updateResult()
        } else {
            if (lastOperator != Operator.EQUALS) {
                processor.setRightOperand(currentOperand)
                lastOperator = Operator.EQUALS
            }

            launchProcessor()

            result = processor.getLeftOperandResult()
            needUpdateLastResult = true
            updateResult()
        }
        lastOperator = currentOperator
    }

    private fun updateResult() {
        if (error == null) {
            result?.toString()?.let {
                resultLiveData.postValue(it)
                if (needUpdateLastResult) lastResultLiveData.postValue(it)
                editor.number = it
            }
        } else {
            resultLiveData.postValue(error!!.name)
        }
    }

    private fun getCurrentNumber(): TPNumber {
        return TPNumber(editor.number, currentNumberBase.value!!, currentPrecision.value!!)
    }

    private fun clearAll() {
        lastOperator = null
        initProcessor()
        editor.clear()
        lastResultLiveData.postValue("")
        resultLiveData.postValue(editor.number)
        needClearLastResults.postValue(true)
    }

    fun setNumberBase(base: NumberBase) {
        currentNumberBase.postValue(base)
        clearAll()
    }

    fun setPrecision(precision: Int) {
        currentPrecision.postValue(precision)
    }

    private fun launchProcessor() {
        try {
            processor.performOperation()
        } catch (ex: Exception) {
            setError(ErrorObject(ERROR_TITLE))
        }
    }

    fun setError(errorObject: ErrorObject?) {
        error = errorObject
        errorLiveData.postValue(errorObject)
    }

}