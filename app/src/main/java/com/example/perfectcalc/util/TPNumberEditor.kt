package com.example.perfectcalc.util


class TPNumberEditor {
    companion object {
        const val ZERO = "0"
        const val SIGN = "-"
        const val DOT = "."
    }

    var number: String = ZERO

    fun isZero(): Boolean = number == ZERO

    fun changeSign(): TPNumberEditor {
        if (number.contains(SIGN)) {
            number = number.replace(SIGN, "")
        } else {
            number = SIGN + number
        }
        return this
    }

    fun addDigit(digit: String): TPNumberEditor {
        if (number == SIGN + ZERO) {
            number = SIGN
        } else if (number == ZERO) {
            number = ""
        }
        number += digit
        return this
    }

    fun addZero(): TPNumberEditor {
        return addDigit(ZERO)
    }

    fun removeLastDigit(): TPNumberEditor {
        val lastDigitIdx = number.length - 1
        if (lastDigitIdx > 0) {
            number = number.substring(0, lastDigitIdx)
        } else if (lastDigitIdx == 0) {
            number = ""
        }
        if (number == SIGN || number.isEmpty()) {
            addZero()
        }
        return this
    }

    fun addDot(): TPNumberEditor {
        if (!number.contains(DOT)) {
            number += DOT
        }
        return this
    }

    fun editNumber(command: EditorCommands, value: String = ZERO): TPNumberEditor {
        when (command) {
            EditorCommands.ADD_DOT -> addDot()
            EditorCommands.ADD_DIGIT -> addDigit(value)
            EditorCommands.CHANGE_SIGN -> changeSign()
            EditorCommands.BACKSPACE -> removeLastDigit()
            EditorCommands.CLEAR -> clear()
        }
        return this
    }

    fun clear(): TPNumberEditor {
        number = ZERO
        return this
    }
}
