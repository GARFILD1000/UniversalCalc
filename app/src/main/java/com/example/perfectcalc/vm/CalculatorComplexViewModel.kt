package com.example.perfectcalc.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perfectcalc.model.ErrorObject
import com.example.perfectcalc.util.*
import java.lang.Exception

class CalculatorComplexViewModel : ViewModel() {
    companion object {
        val LOG_TAG = "CalculatorComplexVM"
        val defaultValue = TComplex(0.0, 0.0)
    }

    private var memory = TMemory<TComplex>()
    private var processor = TProcessor<TComplex>()
    private var editor = TComplexEditor()

    private var lastOperator: Operator? = null

    private var newNumberInput = false
    private var needUpdateLastResult = true

    var result: TComplex? = null
    private set

    val resultLiveData = MutableLiveData<String>().apply { postValue(editor.complex) }
    val errorLiveData = MutableLiveData<ErrorObject?>().apply {postValue(null)}
    var error : ErrorObject? = null
        private set
    val lastResultLiveData = MutableLiveData<String>().apply { postValue("") }
    val needClearLastResults = MutableLiveData<Boolean>().apply { postValue(false) }

    val lastPressedButton = MutableLiveData<CalculatorButton>()

    var rootDegree: Int = TComplex.ROOT_DEGREE
    private set
    var rootNumber: Int = TComplex.ROOT_NUMBER
    private set

    val memoryEnabled = MutableLiveData<Boolean>().apply { postValue(false) }

    init {
        initProcessor()
        TComplex.ROOT_NUMBER = rootNumber
        TComplex.ROOT_DEGREE = rootDegree
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
        editor.editComplex(EditorCommands.ADD_DIGIT, button.digit)
        resultLiveData.postValue(editor.complex)
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
        editor.editComplex(command.command)
        resultLiveData.postValue(editor.complex)
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
                editor.complex = memory.getValue().toString()
                newNumberInput = false
                resultLiveData.postValue(editor.complex)
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
                editor.complex = it
            }
        } else {
            resultLiveData.postValue(error!!.name)
        }
    }

    private fun getCurrentNumber(): TComplex {
        return TComplex(editor.complex)
    }

    private fun clearAll() {
        lastOperator = null
        initProcessor()
        editor.clear()
        lastResultLiveData.postValue("")
        resultLiveData.postValue(editor.complex)
        needClearLastResults.postValue(true)
    }

    fun setRootDegree(newRootDegree: Int) {
        rootDegree = newRootDegree
        TComplex.ROOT_DEGREE = rootDegree
    }

    fun setRootNumber(newRootNumber: Int) {
        rootNumber = newRootNumber
        TComplex.ROOT_NUMBER = rootNumber
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