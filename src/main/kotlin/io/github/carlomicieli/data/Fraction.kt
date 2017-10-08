/*
 * Copyright 2017 Carlo Micieli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.carlomicieli.data

/**
 * A number that can be represented in the form a/b, where a and b are
 * both Int and b is not zero.
 *
 * @author Carlo Micieli
 * @since 1.0.0
 */
class Fraction(n: Int, d: Int) {
    /**
     * The numerator value
     */
    val numerator: Int

    /**
     * The denominator value
     */
    val denominator: Int

    init {
        require (d != 0) { "Denominator must be != 0" }
        val g = gcd(n, d)

        this.numerator = Math.abs(n / g) * sign(n, d)
        this.denominator = Math.abs(d / g)
    }

    private fun sign(n: Int, d: Int) = if (n < 0 || d < 0) -1 else 1

    /**
     * A new [Fraction] for a whole number.
     * @param n the numerator value
     */
    constructor(n: Int) : this(n, 1)

    /**
     * Sum two [Fraction] values
     * @param that the second fraction value
     * @return their sum
     */
    operator fun plus(that: Fraction): Fraction {
        val (a, b) = this
        val (c, d) = that
        return Fraction(a * d + c * b, b * d)
    }

    operator fun unaryMinus(): Fraction = Fraction(-numerator, denominator)

    /**
     * Difference between two fractions
     */
    operator fun minus(that: Fraction): Fraction = this + (-that)

    operator fun times(that: Fraction): Fraction {
        val (a, b) = this
        val (c, d) = that
        return Fraction(a * c, b * d)
    }

    operator fun div(that: Fraction): Fraction {
        require (that.numerator != 0) { "Numerator for the second fraction must be != 0" }

        val (a, b) = this
        val (c, d) = that
        return Fraction(a * d, b * c)
    }

    infix fun max(other: Fraction) = if (this > other) this else other

    infix fun min(other: Fraction) = if (this > other) other else this

    operator fun component1(): Int = numerator
    operator fun component2(): Int = denominator

    operator fun compareTo(that: Fraction): Int {
        if (this == that)
            return 0

        val (a, b) = this
        val (c, d) = that
        if (a * d > c * b)
            return 1
        else
            return -1
    }

    override fun equals(other: Any?): Boolean = when (other) {
        is Fraction -> areEquals(this, other)
        else -> false
    }

    override fun toString(): String {
        return "$numerator/$denominator"
    }

    private fun areEquals(f1: Fraction, f2: Fraction) =
            f1.numerator == f2.numerator && f1.denominator == f2.denominator
}

// Greatest common divisor (https://en.wikipedia.org/wiki/Euclidean_algorithm)
tailrec fun gcd(x: Int, y: Int): Int {
    if (y == 0)
        return x
    else {
        return gcd(y, Math.floorMod(x, y))
    }
}

val Int.fr: Fraction
    get() = Fraction(this, 1)
