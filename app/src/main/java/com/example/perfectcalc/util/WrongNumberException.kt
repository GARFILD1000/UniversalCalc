package com.example.perfectcalc.util


class WrongNumberException(var description: String) : Exception() {
    override fun toString(): String {
        return "WrongNumberException: ${description}"
    }
}