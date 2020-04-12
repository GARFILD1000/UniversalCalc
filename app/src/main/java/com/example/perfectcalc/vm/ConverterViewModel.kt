package com.example.perfectcalc.vm

import android.provider.SyncStateContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perfectcalc.model.ConverterHistoryItem
import com.example.perfectcalc.model.ErrorObject
import com.example.perfectcalc.util.*
import java.lang.Exception

class ConverterViewModel : ViewModel() {
    companion object {
        val LOG_TAG = "ConverterViewModel"
        val precisions =
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        val defaultPrecisionIdx = 3
        val defaultPrecision = 4
        val defaultFromNumberBase = NumberBase.TEN
        val defaultToNumberBase = NumberBase.TWO
    }

    private var editor = TPNumberEditor()
    var history = THistory<ConverterHistoryItem>()
        private set

    val resultLiveData = MutableLiveData<String>().apply { "" }
    val editLiveData = MutableLiveData<String>().apply { postValue(editor.number) }
    val errorLiveData = MutableLiveData<ErrorObject?>().apply { postValue(null) }
    var error: ErrorObject? = null
        private set

    val needUpdateHistory = MutableLiveData<Boolean>().apply { postValue(false) }
    val needClearHistory = MutableLiveData<Boolean>().apply { postValue(false) }

    val lastPressedButton = MutableLiveData<CalculatorButton>()
    val currentNumberBase = MutableLiveData<NumberBase>().apply { postValue(defaultFromNumberBase) }
    val destinationNumberBase = MutableLiveData<NumberBase>().apply { postValue(defaultToNumberBase) }

    val currentPrecision = MutableLiveData<Int>().apply { postValue(defaultPrecision) }

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
        editor.editNumber(EditorCommands.ADD_DIGIT, button.digit)
        editLiveData.postValue(editor.number)
    }

    private fun processCommandButton(button: CommandButton) {
        val command = button.command
        if (command is EditorCommand) {
            processEditorCommand(command)
        }
    }

    private fun processEditorCommand(command: EditorCommand) {
        when (command.command) {
            EditorCommands.CLEAR -> {
                clearAll()
            }
        }
        editor.editNumber(command.command)
        editLiveData.postValue(editor.number)
    }


    private fun processOperatorButton(button: OperatorButton) {
        if (button.operator == Operator.EQUALS) {
            updateResult()
        }
    }

    private fun updateResult() {
        val currentNumber = getCurrentNumber()
        val destinationNumber = getDestinationNumber()
        if (error == null) {
            history.addItem(ConverterHistoryItem(currentNumber, destinationNumber))
            resultLiveData.postValue(destinationNumber.toString())
            needUpdateHistory.postValue(true)
        } else {
            resultLiveData.postValue(error!!.name)
        }
    }

    private fun getCurrentNumber(): TPNumber {
        return TPNumber(editor.number, currentNumberBase.value!!, currentPrecision.value!!).apply {
            if (!this.isCorrect()) {
                error = ErrorObject(ERROR_TITLE)
                errorLiveData.postValue(error)
            }
        }
    }

    private fun getDestinationNumber(): TPNumber {
        return TPNumber(editor.number, currentNumberBase.value!!, currentPrecision.value!!).apply {
            this.numberBase = destinationNumberBase.value!!
            if (!this.isCorrect()) {
                error = ErrorObject(ERROR_TITLE)
                errorLiveData.postValue(error)
            }
        }
    }

    private fun clearAll() {
        editor.clear()
        editLiveData.postValue(editor.number)
        resultLiveData.postValue("")
//        needClearLastResults.postValue(true)
    }

    fun setCurrentNumberBase(base: NumberBase) {
        currentNumberBase.postValue(base)
        clearAll()
    }

    fun setDestinationNumberBase(base: NumberBase) {
        destinationNumberBase.postValue(base)
    }

    fun setPrecision(precision: Int) {
        currentPrecision.postValue(precision)
    }

    fun setError(errorObject: ErrorObject?) {
        error = errorObject
        errorLiveData.postValue(errorObject)
    }
}