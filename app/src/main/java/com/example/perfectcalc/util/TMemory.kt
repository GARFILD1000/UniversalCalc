package com.example.perfectcalc.util


class TMemory<T : TNumber> {
    var fnumber: T? = null
        private set
    var fstate: Boolean = false
        private set

    fun setValue(num: T) {
        fnumber = num
        fstate = true
    }

    fun getValue(): T? = fnumber

    fun add(e: T) {
        fnumber = if (fnumber != null) {
            fnumber!!.add(e) as T
        } else {
            e
        }
        fstate = true
    }

    fun clear() {
        fnumber = null
        fstate = false
    }

    fun getFState(): String {
        return if (fstate) "On" else "Off"
    }

    operator fun Number.plus(other: Number): Number {
        return when (this) {
            is Long -> this.toLong() + other.toLong()
            is Int -> this.toInt() + other.toInt()
            is Short -> this.toShort() + other.toShort()
            is Byte -> this.toByte() + other.toByte()
            is Double -> this.toDouble() + other.toDouble()
            is Float -> this.toFloat() + other.toFloat()
            else -> throw RuntimeException("Unknown numeric type")
        }
    }
}