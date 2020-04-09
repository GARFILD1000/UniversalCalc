package com.example.perfectcalc.util

abstract class TNumber {
    abstract fun add(number: TNumber): TNumber
    abstract fun sub(number: TNumber): TNumber
    abstract fun mul(number: TNumber): TNumber
    abstract fun div(number: TNumber): TNumber
    abstract fun pow(number: TNumber): TNumber
    abstract fun sqrt(): TNumber
    abstract fun root(): TNumber
    abstract fun reverse(): TNumber
}