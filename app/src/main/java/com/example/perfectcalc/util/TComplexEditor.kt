package com.example.perfectcalc.util

import java.lang.Exception

class TComplexEditor {
    companion object {
        const val ZERO = "0"
        const val SIGN = "-"
        const val DOT = "."
        const val DIVIDER = " + "
        const val IMAGE = "i"
    }

    var complex: String
        get() {
            if (real == SIGN) real += ZERO
            if (image == SIGN) image += ZERO
            var string = if (real.isEmpty()) ZERO else real
            if (dividerEnabled) {
                string += DIVIDER
                string += if (image.isEmpty()) ZERO else image
                string += if (!image.contains(IMAGE)) IMAGE else ""
            }
            return string
        }
        set(value) {
            val parts = value.split(DIVIDER)
            try {
                if (parts.size > 1) dividerEnabled = true
                real = parts.getOrNull(0) ?: ""
                image = parts.getOrNull(1) ?.replace(IMAGE, "") ?: ""
            } catch(ex: Exception) {

            }
        }
    private var real: String = ""
    private var image: String = ""
    private var dividerEnabled = false

    fun isZero(): Boolean = complex == ZERO

    private fun getCurrent(): String = if (!dividerEnabled) real else image

    private fun setCurrent(value: String) {
        if (!dividerEnabled) real = value
        else image = value
    }

    fun changeSign(): TComplexEditor {
        var current = getCurrent()
        if (current.contains(SIGN)) {
            current = current.replace(SIGN, "")
        } else {
            current = SIGN + current
        }
        setCurrent(current)
        return this
    }

    fun addDigit(digit: String): TComplexEditor {
        var current = getCurrent()
        if (current == SIGN + ZERO) {
            current = SIGN
        } else if (complex == ZERO) {
            current = ""
        }
        current += digit
        setCurrent(current)
        return this
    }

    fun addZero(): TComplexEditor {
        return addDigit(ZERO)
    }

    fun removeLastDigit(): TComplexEditor {
        var current = getCurrent()
        if (current.isEmpty() && dividerEnabled) {
            dividerEnabled = false
            return this
        }
        val lastDigitIdx = current.length - 1
        if (lastDigitIdx > 0) {
            current = current.substring(0, lastDigitIdx)
        } else if (lastDigitIdx == 0) {
            current = ""
        }
        setCurrent(current)
        return this
    }

    fun addDivider(): TComplexEditor {
        dividerEnabled = true
        return this
    }

    fun addDot(): TComplexEditor {
        var current = getCurrent()
        if (!current.contains(DOT)) {
            current += DOT
        }
        setCurrent(current)
        return this
    }

    fun editComplex(command: EditorCommands, value: String = ZERO): TComplexEditor {
        when (command) {
            EditorCommands.ADD_DIVIDER -> addDivider()
            EditorCommands.ADD_DOT -> addDot()
            EditorCommands.ADD_DIGIT -> addDigit(value)
            EditorCommands.CHANGE_SIGN -> changeSign()
            EditorCommands.BACKSPACE -> removeLastDigit()
            EditorCommands.CLEAR -> clear()
        }
        return this
    }

    fun clear(): TComplexEditor {
        real = ""
        image = ""
        dividerEnabled = false
        return this
    }
}