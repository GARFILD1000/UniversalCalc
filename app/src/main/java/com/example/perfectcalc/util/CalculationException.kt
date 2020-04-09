package com.example.perfectcalc.util

class CalculationException(var description: String) : Exception() {
    override fun toString(): String {
        return "CalculationException: ${description}"
    }
}