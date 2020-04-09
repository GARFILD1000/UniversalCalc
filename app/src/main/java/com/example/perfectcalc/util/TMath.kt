package com.example.perfectcalc.util

import kotlin.math.absoluteValue

object TMath {

    fun isZero(value: Int): Boolean {
        return value == 0
    }

    fun isZero(value: Long): Boolean {
        return value == 0L
    }

    fun isZero(value: Double): Boolean {
        return value.absoluteValue < DOUBLE_DELTA
    }
}