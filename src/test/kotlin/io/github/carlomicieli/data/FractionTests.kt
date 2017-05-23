/*
 * Copyright 2017 Carlo Micieli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.carlomicieli.data

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual

class FractionTests : io.kotlintest.specs.StringSpec() {
    val twoThird = io.github.carlomicieli.data.Fraction(2, 3)
    val oneHalf = io.github.carlomicieli.data.Fraction(1, 2)

    init {
        "should create Fraction values" {
            val oneHalf = io.github.carlomicieli.data.Fraction(1, 2)
            oneHalf.numerator shouldBe 1
            oneHalf.denominator shouldBe 2
        }

        "should create Fractions from whole numbers" {
            val two = io.github.carlomicieli.data.Fraction(2)
            two.numerator shouldBe 2
            two.denominator shouldBe 1
        }

        "should create Fractions from negative numbers" {
            val f1 = io.github.carlomicieli.data.Fraction(-1, 2)
            val f2 = io.github.carlomicieli.data.Fraction(1, -2)
            val f3 = io.github.carlomicieli.data.Fraction(-1, -2)

            f1 shouldEqual f2
            f3 shouldEqual f1
            f2 shouldEqual f2
        }

        "should check whether two fractions are equals" {
            (io.github.carlomicieli.data.Fraction(1, 2) == io.github.carlomicieli.data.Fraction(1, 2)) shouldBe true
            (io.github.carlomicieli.data.Fraction(1, 2) == io.github.carlomicieli.data.Fraction(5, 2)) shouldBe false
            (io.github.carlomicieli.data.Fraction(5, 2) == io.github.carlomicieli.data.Fraction(1, 2)) shouldBe false
        }

        "should produce String representations from Fractions" {
            val s = io.github.carlomicieli.data.Fraction(1, 2).toString()
            s shouldBe "1/2"
        }

        "should always produce fractions in their lowest terms" {
            val f = io.github.carlomicieli.data.Fraction(6, 63)
            f.toString() shouldBe "2/21"
        }

        "should throw an exception when denominator is 0" {
            val ex = io.kotlintest.matchers.shouldThrow<IllegalArgumentException> {
                Fraction(42, 0)
            }
            ex.message shouldBe "Denominator must be != 0"
        }

        "should return the negate fraction" {
            val neg = -twoThird
            neg.numerator shouldBe -twoThird.numerator
            neg.denominator shouldBe twoThird.denominator
        }

        "should sum two fractions" {
            (oneHalf + twoThird) shouldBe io.github.carlomicieli.data.Fraction(7, 6)
        }

        "should subtract two fractions" {
            (oneHalf - twoThird) shouldBe io.github.carlomicieli.data.Fraction(-1, 6)
        }

        "should multiply two fractions" {
            (twoThird * oneHalf) shouldBe io.github.carlomicieli.data.Fraction(1, 3)
        }

        "should divide two fractions" {
            (twoThird / oneHalf) shouldBe io.github.carlomicieli.data.Fraction(4, 3)
        }

        "should compare two fractions" {
            (twoThird > oneHalf) shouldBe true
            (oneHalf > twoThird) shouldBe false
        }

        "should find the maximum fraction" {
            (twoThird max oneHalf) shouldBe twoThird
            (oneHalf max twoThird) shouldBe twoThird
        }

        "should find the minimum fraction" {
            (twoThird min oneHalf) shouldBe oneHalf
            (oneHalf min twoThird) shouldBe oneHalf
        }

        "should sum a fraction with an Int" {
            (2.fr + oneHalf) shouldBe io.github.carlomicieli.data.Fraction(5, 2)
        }
    }
}
