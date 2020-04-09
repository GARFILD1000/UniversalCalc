package com.example.perfectcalc.util

enum class NumberBase(val base: Int){
    TWO(2),
    THREE(3),
    FOUR(4),
    FIFE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    ELEVEN(11),
    TWELVE(12),
    THIRTEEN(13),
    FOURTEEN(14),
    FIFTEEN(15),
    SIXTEEN(16)
}

object NumberBaseHelper {
    val ALL = arrayOf(
        NumberBase.TWO, NumberBase.THREE, NumberBase.FOUR,
        NumberBase.FIFE, NumberBase.SIX, NumberBase.SEVEN, NumberBase.EIGHT,
        NumberBase.NINE, NumberBase.TEN, NumberBase.ELEVEN, NumberBase.TWELVE,
        NumberBase.THIRTEEN, NumberBase.FOURTEEN, NumberBase.FIFTEEN, NumberBase.SIXTEEN)
}

