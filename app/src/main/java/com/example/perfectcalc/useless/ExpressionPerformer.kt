package com.example.perfectcalc.useless


import com.example.perfectcalc.useless.ExpressionConstants.ADD
import com.example.perfectcalc.useless.ExpressionConstants.BRACKET_LEFT
import com.example.perfectcalc.useless.ExpressionConstants.BRACKET_RIGHT
import com.example.perfectcalc.useless.ExpressionConstants.DIV
import com.example.perfectcalc.useless.ExpressionConstants.ERROR
import com.example.perfectcalc.useless.ExpressionConstants.INFINITY
import com.example.perfectcalc.useless.ExpressionConstants.MINUS
import com.example.perfectcalc.useless.ExpressionConstants.MOD
import com.example.perfectcalc.useless.ExpressionConstants.MUL
import com.example.perfectcalc.useless.ExpressionConstants.POW
import com.example.perfectcalc.useless.ExpressionConstants.SPACE
import com.example.perfectcalc.useless.ExpressionConstants.SUB
import com.example.perfectcalc.useless.ExpressionConstants.ZERO
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


object ExpressionPerformer {

    private class ReversePolishNotation{
        var postfix: LinkedList<String> = LinkedList()
        var successfulParse = true
        var successfulCalculate = true
        val operators = ExpressionConstants.operators.keys


        private fun checkOperator(str: String): Boolean{
            return operators.contains(str)
        }

//        @Throws(NumberFormatException::class)
        private fun checkNumber(str: String): Boolean {
            try {
                BigDecimal(str)
                return true
            } catch (ex: NumberFormatException) {
                return false
            }
        }

        private fun checkBracket(str: String): Boolean{
            return (str.equals("(") || str.equals(")"))
        }

        private fun getOperatorPriority(str: String): Int{
            return ExpressionConstants.operators.get(str) ?: 0
        }

        fun parseExpression(infix: String): List<String>{
            successfulParse = true
            postfix.clear()
            val stack = Stack<String>()
            val delims = SPACE + operators.joinToString("") + ExpressionConstants.brackets
            val tokenizer = StringTokenizer(infix, delims, true)
            var current = ""
            var previous = ""
            while (tokenizer.hasMoreTokens()) {
                current = tokenizer.nextToken()
                if (!tokenizer.hasMoreTokens() && checkOperator(current)) {
                    successfulParse = false
                    return postfix
                }
                if (current == SPACE) continue
                if (checkBracket(current)) {
                    if (current == BRACKET_LEFT) {
                        stack.push(current)
                    } else if (current == BRACKET_RIGHT) {
                        while (stack.peek() != BRACKET_LEFT) {
                            postfix.add(stack.pop())
                            if (stack.isEmpty()) {
                                successfulParse = false
                                return postfix
                            }
                        }
                        stack.pop()
                    }
                } else if (checkOperator(current)){
                    if (current == SUB && (checkOperator(previous) || (previous == BRACKET_LEFT))) {
                        current = MINUS
                    } else {
                        while (!stack.isEmpty() && (getOperatorPriority(current) <= getOperatorPriority(stack.peek()))) {
                            postfix.add(stack.pop())
                        }
                    }
                    stack.push(current)
                }
                else if(checkNumber(current)){
                    if (checkNumber(previous)){
                        successfulParse = false
                        return postfix
                    }
                    else{
                        postfix.add(current)
                    }
                }
                else{
                    successfulParse = false
                    return postfix;
                }
                previous = current;
            }
            while(!stack.isEmpty()){
                if(checkOperator(stack.peek())){
                    postfix.add(stack.pop())
                }
                else{
                    successfulParse = false
                    return postfix
                }
            }
            return postfix;
        }

        fun calculateExpression(): String {
            if (!successfulParse) return ZERO
            successfulCalculate = true
            val stack = Stack<BigDecimal>()
            var operand1 = BigDecimal.ZERO
            var operand2 = BigDecimal.ZERO
            var temp = BigDecimal.ZERO

            for(x in postfix) {
                if (!checkOperator(x)) {
                    operand1 = BigDecimal(x)
                    stack.push(operand1)
                } else {
                    when (x) {
                        ADD -> {
                            operand2 = stack.pop()
                            operand1 = stack.pop()
                            stack.push(operand1.add(operand2))
                        }
                        SUB -> {
                            operand2 = stack.pop();
                            operand1 = stack.pop();
                            stack.push(operand1.subtract(operand2));
                        }
                        MUL -> {
                            operand2 = stack.pop();
                            operand1 = stack.pop();
                            stack.push(operand1.multiply(operand2));
                        }
                        DIV -> {
                            operand2 = stack.pop();
                            operand1 = stack.pop();
                            if (operand2.equals(BigDecimal.ZERO)) {
                                successfulCalculate = false
                                return INFINITY
                            }
                            temp = BigDecimal(0)
                            try {
                                temp = operand1.divide(operand2, 10, RoundingMode.HALF_DOWN)
                            } catch (ex: ArithmeticException) {
                                successfulCalculate = false
                                return ZERO
                            }
                            stack.push(temp)
                        }
                        MINUS -> {
                            stack.push(stack.pop().negate())
                        }
                        POW -> {
                            operand2 = stack.pop()
                            operand1 = stack.pop()
                            if (operand2.compareTo(BigDecimal.ZERO) >= 0) {
                                stack.push(operand1.pow(operand2.intValueExact()))
                            } else {
                                stack.push(BigDecimal.ONE.divide(operand1.pow(operand2.abs().intValueExact())))
                            }
                        }
                        MOD -> {
                            operand2 = stack.pop()
                            operand1 = stack.pop()
                            operand1 = BigDecimal ((operand1.toBigInteger().mod(operand2.toBigInteger())).toString())
                            stack.push(operand1)
                        }
                    }
                }
            }
            return stack.pop().toString()
        }
    }

    fun calculateExpression(expression: String): String{
        val rpn = ReversePolishNotation()
        rpn.parseExpression(expression)
        if (rpn.successfulParse){
            val result = rpn.calculateExpression()
            if (rpn.successfulCalculate){
                return result
            }
            else{
                return ERROR
            }
        }
        else{
            return ERROR
        }
    }

}

fun main() {
    val expression = "1+(2^2+3)*2"
    println(ExpressionPerformer.calculateExpression(expression))
}
