package com.example.perfectcalc.util

abstract class CalculatorButton

class CommandButton(var command: Command) : CalculatorButton() {

}

class OperatorButton(var operator: Operator) : CalculatorButton() {

}

class DigitButton() : CalculatorButton() {
    var digit: String = ""

    constructor(newDigit: String) : this() {
        digit = newDigit
    }
}