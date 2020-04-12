package com.example.perfectcalc.util

import java.util.*

class THistory <T>{
    private val items = LinkedList<T>()

    fun addItem(newItem: T) {
        items.addLast(newItem)
    }

    fun clear() {
        items.clear()
    }

    fun getAll(): List<T> {
        return items.toList()
    }

    fun getLast() : T? {
        return items.lastOrNull()
    }

    override fun toString(): String {
        return items.joinToString("\n")
    }
}