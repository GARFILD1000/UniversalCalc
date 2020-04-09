package com.example.perfectcalc.util

class TProcessor<T : TNumber> {
    private var leftOperandResult: T? = null
    private var rightOperand: T? = null
    private var operator: Operator? = null
    private var lastOperator: Operator? = null

    fun setOperator(newOperator: Operator?) {
        lastOperator = operator
        operator = newOperator
    }

    fun getOperator() = operator

    fun setLeftOperandResult(newOperand: T?) {
        leftOperandResult = newOperand
    }

    fun getLeftOperandResult(): T? = leftOperandResult

    fun setRightOperand(newOperand: T?) {
        rightOperand = newOperand
    }

    fun getRightOperand(): T? = rightOperand


    fun performOperation() {
        operator ?: return
        if (operator?.isBinary == true) {
            rightOperand?.let {
                when (operator) {
                    Operator.ADD -> {
                        leftOperandResult = leftOperandResult?.add(it) as T
                    }
                    Operator.SUB -> {
                        leftOperandResult = leftOperandResult?.sub(it) as T
                    }
                    Operator.DIV -> {
                        leftOperandResult = leftOperandResult?.div(it) as T
                    }
                    Operator.MUL -> {
                        leftOperandResult = leftOperandResult?.mul(it) as T
                    }
                    Operator.POW -> {
                        leftOperandResult = leftOperandResult?.pow(it) as T
                    }
                }
            }
        } else if (operator?.isBinary == false) {
            when (operator) {
                Operator.SQRT -> {
                    leftOperandResult = leftOperandResult?.sqrt() as T
                }
                Operator.ROOT -> {
                    leftOperandResult = leftOperandResult?.root() as T
                }
                Operator.REVERSE -> {
                    leftOperandResult = leftOperandResult?.reverse() as T
                }
            }
        }
    }
}