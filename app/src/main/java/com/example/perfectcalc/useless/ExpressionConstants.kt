package com.example.perfectcalc.useless

object ExpressionConstants {

    const val DIGIT0 = "0"
    const val DIGIT1 = "1"
    const val DIGIT2 = "2"
    const val DIGIT3 = "3"
    const val DIGIT4 = "4"
    const val DIGIT5 = "5"
    const val DIGIT6 = "6"
    const val DIGIT7 = "7"
    const val DIGIT8 = "8"
    const val DIGIT9 = "9"
    const val DIGITA = "A"
    const val DIGITB = "B"
    const val DIGITC = "C"
    const val DIGITD = "D"
    const val DIGITE = "E"
    const val DIGITF = "F"
    const val DIGITDOT = "."

    const val ADD = "+"
    const val SUB = "-"
    const val MUL = "*"
    const val DIV = "/"

    const val REVERSE = "^(-1)"
    const val POW = "^"
    const val SQRT = "^(1/2)"
    const val ROOT = "√"
    const val FACTORIAL = "!"
    const val SIN = "sin"
    const val COS = "cos"
    const val TAN = "tan"
    const val BRACKET_LEFT = "("
    const val BRACKET_RIGHT = ")"
    const val LG = "lg"
    const val LN = "ln"
    const val MOD = "mod"

    const val EQUALS = "="
    const val SPACE = " "
    const val MINUS = "minus"
    const val ZERO = "0"
    const val INFINITY = "∞"

    const val NUMBER_PI = "π"
    const val NUMBER_E = "e"

    const val ERROR = "error"

    val operators = mapOf<String, Int>(
        ADD to 1,
        SUB to 1,
        MUL to 2,
        DIV to 2,
        REVERSE to 2,
        POW to 3,
        ROOT to 3,
        FACTORIAL to 3,
        SIN to 3,
        COS to 3,
        TAN to 3,
        LG to 3,
        LN to 3
    )

    val brackets = listOf(
        BRACKET_LEFT,
        BRACKET_RIGHT
    )

    val numbers = mapOf<String, Double>(
        NUMBER_E to Math.E,
        NUMBER_PI to Math.PI
    )

}