package com.example.perfectcalc.util

import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow

class TPNumber : TNumber {

    companion object {
        const val separators = "."
        private val digitsParsing: Map<String, Short> = mapOf<String, Short>(
            "0" to 0,
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
            "A" to 10,
            "B" to 11,
            "C" to 12,
            "D" to 13,
            "E" to 14,
            "F" to 15
        )
        private val digitsConverting: Map<Int, String> = mapOf<Int, String>(
            0 to "0",
            1 to "1",
            2 to "2",
            3 to "3",
            4 to "4",
            5 to "5",
            6 to "6",
            7 to "7",
            8 to "8",
            9 to "9",
            10 to "A",
            11 to "B",
            12 to "C",
            13 to "D",
            14 to "E",
            15 to "F"
        )
        private val signParsing: Map<String, Int> = mapOf<String, Int>(
            "+" to 1,
            "-" to -1
        )
    }

    var number = 0.0
        private set
    var numberBase: NumberBase = NumberBase.TEN
    var precision = 10

//    constructor(numberBase: NumberBase, precision: Int) {}

    constructor(number: Double, numberBase: NumberBase, precision: Int) {
        this.number = number
        this.numberBase = numberBase
        this.precision = precision
    }

    constructor(number: String, numberBase: NumberBase, precision: Int) {
        this.number = parseString(number, numberBase, precision)
        this.numberBase = numberBase
        this.precision = precision
    }

    @Throws(WrongNumberException::class)
    fun parseString(number: String, numberBase: NumberBase, precision: Int): Double {
        var newNumber = 0.0

        var numberString = number
        var sign = 1
        number.getOrNull(0)?.let { firstChar ->
            signParsing.get(firstChar.toString())?.let { parsedSign ->
                sign = parsedSign
                numberString = number.removeRange(0, 1)
            }
        }

        val splittedNumber = numberString.replace(",", ".").split(separators)
        if (splittedNumber.size > 2) {
            throw WrongNumberException("There are more than one decimal separators in number")
        }
        val integerPart = splittedNumber.getOrNull(0) ?: ""
        val fractionalPart = splittedNumber.getOrNull(1) ?: ""

        var basePowed = 1L
        for (symbol in integerPart.reversed()) {
            val isValidDigit = isValidDigit(symbol.toString(), numberBase)
            if (isValidDigit) {
                val digit = digitsParsing.get(symbol.toString())
                val additional = basePowed * digit!!
                newNumber += additional
            } else {
                throw WrongNumberException("There is wrong digit '$symbol' in number with the given base")
            }
            basePowed *= numberBase.base
        }
        basePowed = numberBase.base.toLong()
        var counter = 0
        for (symbol in fractionalPart) {
            counter++
            if (counter > precision) break
            val isValidDigit = isValidDigit(symbol.toString(), numberBase)
            if (isValidDigit) {
                val digit = digitsParsing.get(symbol.toString())
                val additional = (digit!!.toDouble() / basePowed.toDouble())
                if (additional.isNaN()) break
                newNumber += additional
            } else {
                throw WrongNumberException("There is wrong digit '$symbol' in number with the given base")
            }
            basePowed *= numberBase.base
        }
        newNumber *= sign
        return newNumber
    }

    private fun isValidDigit(digit: String, numberBase: NumberBase): Boolean {
        val parsed = digitsParsing.get(digit)
        return parsed != null && parsed < numberBase.base
    }

    override fun toString(): String {
        return toString(numberBase)
    }

    fun toString(base: NumberBase): String {
        var result = StringBuilder()
        var remains = 0
        var quotient = number.toInt().absoluteValue
        while (quotient >= base.base) {
            remains = quotient % base.base
            quotient = quotient / base.base
            result.append(digitsConverting.get(remains))
        }

        result.append(digitsConverting.get(quotient))
        if (number < 0) result.append("-")
        result.reverse()

        var fractional = (number - (number.toInt())).absoluteValue

        if (fractional > 0.0) {
            result.append(separators)
            for (i in 0 until precision) {
                fractional = fractional * base.base
                result.append(digitsConverting.get(fractional.toInt()))
                fractional = (fractional - (fractional.toInt()))
            }
        }
        return result.toString()
    }

    override fun add(number: TNumber): TNumber {
        return if (number is TPNumber) this + number else this
    }

    override fun div(number: TNumber): TNumber {
        return if (number is TPNumber) this / number else this
    }

    override fun mul(number: TNumber): TNumber {
        return if (number is TPNumber) this * number else this
    }

    override fun sub(number: TNumber): TNumber {
        return if (number is TPNumber) this - number else this
    }

    override fun pow(number: TNumber): TNumber {
        return if (number is TPNumber) this.pow(number) else this
    }

    operator fun plus(otherNumber: TPNumber): TPNumber {
        return TPNumber(this.number + otherNumber.number, this.numberBase, this.precision)
    }

    operator fun minus(otherNumber: TPNumber): TPNumber {
        return TPNumber(this.number - otherNumber.number, this.numberBase, this.precision)
    }

    operator fun times(otherNumber: TPNumber): TPNumber {
        return TPNumber(this.number * otherNumber.number, this.numberBase, this.precision)
    }

    operator fun div(otherNumber: TPNumber): TPNumber {
        var result = 0.0
        if (!TMath.isZero(otherNumber.number)) {
            result = this.number / otherNumber.number
        } else {
            throw CalculationException(ERROR_DIVIDE_BY_ZERO)
        }
        return TPNumber(result, this.numberBase, this.precision)
    }

    override fun reverse(): TPNumber {
        return TPNumber(1.0 / this.number, this.numberBase, this.precision)
    }

    fun square(): TPNumber {
        return TPNumber(this.number * this.number, this.numberBase, this.precision)
    }

    fun pow(degree: TPNumber): TPNumber {
        return TPNumber(number.pow(degree.number), this.numberBase, this.precision)
    }

    override fun sqrt(): TPNumber {
        return TPNumber(kotlin.math.sqrt(number), this.numberBase, this.precision)
    }

    override fun root(): TNumber {
        return sqrt()
    }

    fun copy(): TPNumber {
        return TPNumber(number, numberBase, precision)
    }

}

fun main() {
    val num1 = TPNumber(1.0, NumberBase.TEN, 5)
    val num2 = TPNumber(0.0, NumberBase.TEN, 5)
    println(num1 / num2)
}