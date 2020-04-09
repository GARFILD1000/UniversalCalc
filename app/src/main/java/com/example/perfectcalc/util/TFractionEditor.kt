package com.example.perfectcalc.util


class TFractionEditor {
    companion object {
        const val ZERO = "0"
        const val SIGN = "-"
        const val DIVIDER = "/"
    }

    var fraction: String = ZERO

    fun isZero(): Boolean = fraction == ZERO

    fun changeSign(): TFractionEditor {
        if (fraction.contains(SIGN)) {
            fraction.replace(SIGN, "")
        } else {
            fraction = SIGN + fraction
        }
        return this
    }

    fun addDigit(digit: String): TFractionEditor {
        if (fraction == SIGN + ZERO) {
            fraction = SIGN
        } else if (fraction == ZERO) {
            fraction = ""
        }
        if (fraction.isEmpty() || !(digit == ZERO && fraction.last().toString() == DIVIDER)) {
            fraction += digit
        }
        return this
    }

    fun addZero(): TFractionEditor {
        return addDigit(ZERO)
    }

    fun removeLastDigit(): TFractionEditor {
        val lastDigitIdx = fraction.length - 1
        if (lastDigitIdx > 0) {
            fraction = fraction.substring(0, lastDigitIdx)
        } else if (lastDigitIdx == 0) {
            fraction = ""
        }
        if (fraction == SIGN || fraction.isEmpty()) {
            addZero()
        }
        return this
    }

    fun addDivider(): TFractionEditor {
        if (!fraction.contains(DIVIDER)) {
            fraction += DIVIDER
        }
        return this
    }

    fun editFraction(command: EditorCommands, value: String = ZERO): TFractionEditor {
        when (command) {
            EditorCommands.ADD_DIVIDER -> addDivider()
            EditorCommands.ADD_DIGIT -> addDigit(value)
            EditorCommands.CHANGE_SIGN -> changeSign()
            EditorCommands.BACKSPACE -> removeLastDigit()
            EditorCommands.CLEAR -> clear()
        }
        return this
    }

    fun clear(): TFractionEditor {
        fraction = ZERO
        return this
    }
}