package com.example.perfectcalc.util

import java.lang.Exception
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

class TFraction : TNumber{
    companion object{
        var DELTA = 0.00000001
    }

    var numerator: Long = 0L
    var denominator: Long = 0L

    constructor(numerator: Long, denominator: Long) {
        init(numerator, denominator)
        reduceFraction()
    }

    constructor(fractionString: String) {
        val parts = fractionString.split("/")
        var newNumerator = 0L
        var newDenominator = 1L
        try {
            when (parts.size) {
                2 -> {
                    newNumerator = parts[0].toLongOrNull() ?: parts[0].toDouble().roundToLong()
                    if (parts[1].isNotEmpty()) {
                        newDenominator = parts[1].toLongOrNull() ?: parts[1].toDouble().roundToLong()
                    }
                }
                1 -> {
                    newNumerator = parts[0].toLongOrNull() ?: parts[0].toDouble().roundToLong()
                }
            }
        }
        catch(ex: Exception){
            throw WrongNumberException(ex.toString())
        }
        finally {
            init(newNumerator, newDenominator)
            reduceFraction()
        }
    }

    constructor(oldFraction: TFraction){
        init(oldFraction.numerator, oldFraction.denominator)
    }

    private fun init(numerator: Long, denominator: Long) {
        this.numerator = numerator
        this.denominator = denominator
    }

    fun copy(): TFraction{
        return TFraction(this.numerator, this.denominator)
    }

    fun reduceFraction(){
        val gcd: Long = getGreatestCommonDivisor(numerator, denominator)
        if (gcd != 0L){
            numerator = numerator / gcd
            denominator = denominator / gcd
        }
        if (numerator < 0 && denominator < 0){
            numerator = numerator.absoluteValue
            denominator = denominator.absoluteValue
        }else if (denominator < 0){
            numerator = -numerator
            denominator = denominator.absoluteValue
        }
    }

    private fun setCommonDenominatorWith(fraction: TFraction){
        if (this.denominator != fraction.denominator){
            val lcd = getLowestCommonDenominator(this.denominator, fraction.denominator)
            this.numerator = this.numerator * (lcd / this.denominator)
            fraction.numerator = fraction.numerator * (lcd / fraction.denominator)
            this.denominator = lcd
            fraction.denominator = lcd
        }
    }

    private fun getLowestCommonDenominator(denominator1: Long, denominator2: Long): Long{
        return denominator1 * denominator2 / getGreatestCommonDivisor(denominator1, denominator2)
    }

    private fun getGreatestCommonDivisor(number1: Long, number2: Long): Long{
        var tempNumber1 = number1.absoluteValue
        var tempNumber2 = number2.absoluteValue
        if (tempNumber1 == 0L || tempNumber2 == 0L){
            return 0L
        }
        if (tempNumber1 == 1L || tempNumber2 == 1L){
            return 1L
        }
        while (tempNumber1 != tempNumber2){
            if (tempNumber1 > tempNumber2){
                tempNumber1 -= tempNumber2
            }
            else{
                tempNumber2 -= tempNumber1
            }
        }
        return tempNumber1
    }

    override fun reverse(): TFraction{
        return TFraction(denominator, numerator)
    }

    fun getNegative(): TFraction{
        return TFraction(numerator, denominator).apply{
            setNegative()
        }
    }

    fun setNegative(){
        numerator = -numerator
    }

    fun getAbs(): TFraction{
        return TFraction(numerator.absoluteValue, denominator.absoluteValue)
    }

    fun square(): TFraction{
        return TFraction(numerator*numerator, denominator*denominator)
    }

    operator fun plus(operator2: TFraction): TFraction{
        var result = TFraction(0,1)
        this.reduceFraction()
        operator2.reduceFraction()
        setCommonDenominatorWith(operator2)
        result.numerator = numerator + operator2.numerator
        result.denominator = denominator
        result.reduceFraction()
        reduceFraction()
        operator2.reduceFraction()
        return result
    }

    operator fun minus(operator2: TFraction): TFraction{
        var result = TFraction(0,0)
        this.reduceFraction()
        operator2.reduceFraction()
        setCommonDenominatorWith(operator2)
        result.numerator = numerator - operator2.numerator
        result.denominator = denominator
        result.reduceFraction()
        reduceFraction()
        operator2.reduceFraction()
        return result
    }

    operator fun times(operator2: TFraction): TFraction{
        var result = TFraction(0,0)
        if (!TMath.isZero(operator2.denominator)) {
            this.reduceFraction()
            operator2.reduceFraction()
            result.numerator = numerator * operator2.numerator
            result.denominator = denominator * operator2.denominator
            result.reduceFraction()
            reduceFraction()
            operator2.reduceFraction()
        } else {
            throw CalculationException(ERROR_DIVIDE_BY_ZERO)
        }
        return result
    }

    operator fun div(operator2: TFraction): TFraction{
        var result = TFraction(0,0)
        if (!TMath.isZero(operator2.numerator)) {
            this.reduceFraction()
            operator2.reduceFraction()
            setCommonDenominatorWith(operator2)
            result.numerator = numerator * operator2.denominator
            result.denominator = denominator * operator2.numerator
            result.reduceFraction()
            reduceFraction()
            operator2.reduceFraction()
        } else {
            throw CalculationException(ERROR_DIVIDE_BY_ZERO)
        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        return (other is TFraction) && other.numerator == numerator && other.denominator == denominator
    }

    operator fun compareTo(fraction: TFraction): Int{
        var result = 0
        this.reduceFraction()
        fraction.reduceFraction()
        if (this.denominator == fraction.denominator){
            result = if (this.numerator == fraction.numerator) 0
            else if (this.numerator > fraction.numerator) 1
            else -1
        }
        else {
            val fraction1 = this.toDouble()
            val fraction2 = fraction.toDouble()
            result = if (abs(fraction1-fraction2) < DELTA) 0
            else if (fraction1 > fraction2) 1
            else -1
        }
        return result
    }

    fun getNumeratorString(): String{
        return numerator.toString()
    }

    fun getDenominatorString(): String{
        return denominator.toString()
    }

    override fun toString(): String{
        return if (numerator == 0L){
            TFractionEditor.ZERO
        }
        else if (denominator == 1L){
            "$numerator"
        }
        else {
            "$numerator ${TFractionEditor.DIVIDER} $denominator"
        }
    }

    fun toDouble(): Double{
        return numerator.toDouble() / denominator
    }

    fun getInteger(): Long{
        return (numerator.toDouble() / denominator).roundToLong()
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun add(number: TNumber): TNumber {
        return if (number is TFraction) this + number else this
    }

    override fun sub(number: TNumber): TNumber {
        return if (number is TFraction) this - number else this
    }

    override fun mul(number: TNumber): TNumber {
        return if (number is TFraction) this * number else this
    }

    override fun div(number: TNumber): TNumber {
        return if (number is TFraction) this / number else this
    }

    override fun pow(number: TNumber): TNumber {
        return if (number is TFraction) pow(number.toDouble()) else this
    }

    fun pow(degree: Double) : TFraction {
        val newNumerator = Math.pow(numerator.toDouble(), degree).roundToLong()
        val newDenominator = Math.pow(denominator.toDouble(), degree).roundToLong()
        val newNumber = TFraction(newNumerator, newDenominator)
        newNumber.reduceFraction()
        return newNumber
    }

    override fun sqrt() : TFraction {
        val newNumerator = Math.sqrt(numerator.toDouble()).roundToLong()
        val newDenominator = Math.sqrt(denominator.toDouble()).roundToLong()
        val newNumber = TFraction(newNumerator, newDenominator)
        newNumber.reduceFraction()
        return newNumber
    }

    override fun root(): TNumber {
        return sqrt()
    }
}