package com.example.perfectcalc.util

import java.text.NumberFormat
import kotlin.math.*

class TComplex : TNumber {
    companion object {
        const val DELTA = 0.0000000000001
        var ROOT_NUMBER = 0
        var ROOT_DEGREE = 2
        fun parse(complexString: String): TComplex = TComplex(complexString)
        var precision = 4
    }

    var real = 0.0
    var image = 0.0

    constructor(real: Double, image: Double) {
        this.real = real
        this.image = image
    }

    constructor(complexString: String) {
        parse(complexString)
    }

    private fun init(real: Double, image: Double) {
        this.real = real
        this.image = image
    }


    fun copy(): TComplex = TComplex(this.real, this.image)

    operator fun plus(addition: TComplex): TComplex =
        TComplex(
            this.real + addition.real,
            this.image + addition.image
        )

    operator fun minus(subtrahend: TComplex): TComplex =
        TComplex(
            this.real - subtrahend.real,
            this.image - subtrahend.image
        )

    operator fun times(multiplier: TComplex): TComplex =
        TComplex(
            this.real * multiplier.real - this.image * multiplier.image,
            this.real * multiplier.image + this.image * multiplier.real
        )

    operator fun div(divider: TComplex): TComplex {
        return if (!TMath.isZero(divider.real) && !TMath.isZero(divider.image)) {
            TComplex(
                (this.real * divider.real + this.image * divider.image) / (divider.real * divider.real + divider.image * divider.image),
                (this.image * divider.real - this.real * divider.image) / (divider.real * divider.real + divider.image * divider.image)
            )
        } else {
            throw CalculationException(ERROR_DIVIDE_BY_ZERO)
        }
    }

    fun square(): TComplex =
        TComplex(this.real * this.real - this.image * this.image, 2.0 * this.real * this.image)

    fun pow(degree: Int): TComplex {
        val module = this.module().pow(degree)
        val argument = this.argument()
        return TComplex(
            module * cos(degree * argument),
            module * sin(degree * argument)
        )
    }

    override fun reverse(): TComplex {
        val a = real
        val b = image
        val newReal = a / ((a * a) + (b * b))
        val newImage = -b / ((a * a) + (b * b))
        return TComplex(newReal, newImage)
    }

    fun argument(): Double {
        return when {
            this.real > DELTA / 2.0 -> atan(this.image / this.real)
            this.real < - DELTA / 2.0 -> atan(this.image / this.real) + Math.PI
            this.image > DELTA / 2.0 -> Math.PI / 2
            this.image < - DELTA / 2.0 -> -Math.PI / 2
            else -> -1.0
        }
    }

    fun angle(): Double {
        return argument() * 180.0 / Math.PI
    }

    fun module(): Double {
        return sqrt(this.real * this.real + this.image * this.image)
    }

    override fun root(): TNumber {
        return root(ROOT_DEGREE, ROOT_NUMBER)
    }

    fun root(degree: Int = ROOT_DEGREE, rootNumber: Int = ROOT_NUMBER): TComplex {
        val k = rootNumber.rem(degree)
        val module = this.module().pow(1.0 / degree)
        val argument = this.argument()
        return TComplex(
            module * cos((argument + 2.0 * PI * k) / degree),
            module * sin((argument + 2.0 * PI * k) / degree)
        )
    }

    override fun equals(other: Any?): Boolean {
        return (other is TComplex) && (this.image == other.image && this.real == other.real)
    }

    fun getRealString(): String {
        val nf = getNumberFormat()
        return nf.format(this.real)
    }

    fun getImageString(): String {
        val nf = getNumberFormat()
        return nf.format(this.image)
    }

    private fun getNumberFormat() : NumberFormat{
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 0
        numberFormat.maximumFractionDigits = precision
        return numberFormat
    }

    override fun toString(): String {
        return (String.format(
            "%s%s%s%s",
            getRealString(),
            TComplexEditor.DIVIDER,
            getImageString(),
            TComplexEditor.IMAGE
            ).replace(",", TComplexEditor.DOT))
    }

    private fun parse(complexString: String) {
        this.real = 0.0
        this.image = 0.0
        val parts = complexString.apply { replace(" ", "") }.split(TComplexEditor.DIVIDER)
        try {
            if (parts.size > 2) {
                throw WrongNumberException("Wrong complex number notation")
            } else if (parts.size == 2) {
                var imageIndex = 1
                var realIndex = 0
                if (!parts[0].contains("i") && !parts[1].contains("i")) {
                    throw WrongNumberException("Wrong complex number notation")
                } else if (parts[0].contains("i")) {
                    imageIndex = 0
                    realIndex = 1
                }
                parts[imageIndex].let {
                    this.image = it
                        .replace("*", "")
                        .replace("i", "")
                        .replace(",", TComplexEditor.DOT)
                        .toDouble()
                }
                this.real = parts[realIndex]
                    .replace(",", TComplexEditor.DOT)
                    .toDouble()
            } else if (parts.size == 1) {
                if (parts.contains("i")) {
                    parts[0].let {
                        this.image = it
                            .replace("*", "")
                            .replace("i", "")
                            .replace(",", TComplexEditor.DOT)
                            .toDouble()
                    }
                } else {
                    this.real = parts[0]
                        .replace(",", TComplexEditor.DOT)
                        .toDouble()
                }
            }
        } catch (ex: Exception) {
            throw WrongNumberException(ex.toString())
        }
    }

    override fun add(number: TNumber): TNumber {
        return if (number is TComplex) this + number else this
    }

    override fun sub(number: TNumber): TNumber {
        return if (number is TComplex) this - number else this
    }

    override fun mul(number: TNumber): TNumber {
        return if (number is TComplex) this * number else this
    }

    override fun div(number: TNumber): TNumber {
        return if (number is TComplex) this / number else this
    }

    override fun pow(number: TNumber): TNumber {
        return if (number is TComplex) {
            val n = 0
            val x1 = this.real
            val y1 = this.image
            val x2 = number.real
            val y2 = number.image

            val tempReal1 = exp(x2 * ln( sqrt(x1 * x1 + y1 * y1) ) )
            val tempReal2 = exp(-y2 * (atan(y1 / x1) + 2 * PI * n) )
            val tempReal3 = cos(x2 * (atan(y1 / x1) + 2 * PI * n) )
            val tempReal4 = cos( y2 * ln( sqrt(x1 * x1 + y1 * y1) ) )

            val tempImage1 = sin(x2 * (atan(y1 / x1) + 2 * PI * n) )
            val tempImage2 = sin( y2 * ln( sqrt(x1 * x1 + y1 * y1) ) )

            val real = tempReal1 * tempReal2 * (tempReal3 * tempReal4 - tempImage1 * tempImage2)
            val image = tempReal1 * tempReal2 * (tempImage1 * tempReal4 + tempImage2 * tempReal3)

            TComplex(real,image)
        } else {
            this
        }
    }

    override fun sqrt(): TNumber {
        return root(2, ROOT_NUMBER)
    }
}
