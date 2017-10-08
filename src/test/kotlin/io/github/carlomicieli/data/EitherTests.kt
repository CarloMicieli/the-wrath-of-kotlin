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

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class EitherTests : StringSpec() {
    val leftValue: Either<String, Int> = Either.left("error")
    val rightValue: Either<String, Int> = Either.right(42)

    init {
        "should create Left values" {
            val left = Either.left("error")
            left.isLeft() shouldBe true
            left.get() ?: -1 shouldBe -1
        }

        "should create a string representation for left values" {
            leftValue.toString() shouldBe "Left(error)"
        }

        "should create right values" {
            val right = Either.right(42)
            right.isLeft() shouldBe false
            right.isRight() shouldBe true
            right.get() ?: -1 shouldBe 42
        }

        "should create a string representation for right values" {
            rightValue.toString() shouldBe "Right(42)"
        }

        "should convert an Either to a Maybe value" {
            leftValue.toMaybe() shouldBe Maybe.none<String>()
            rightValue.toMaybe() shouldBe Maybe.just(42)
        }

        "should check whether two values are equals" {
            Either.left("error") shouldEqual Either.left("error")
            Either.right(42) shouldEqual Either.right(42)
            (Either.left("error") == Either.right(42)) shouldBe false
            (Either.right(42) == Either.left("error")) shouldBe false
        }

        "should fold on Left values" {
            val res: Int = leftValue.fold({ str -> str.length }, { n -> n * 2 })
            res shouldEqual "error".length
        }

        "should fold on Right values" {
            val res: Int = rightValue.fold({ str -> str.length }, { n -> n * 2 })
            res shouldEqual (42 * 2)
        }

        "should map a function over right values" {
            rightValue.map { n -> n * 2 } shouldEqual Either.right(84)
        }

        "should map a function over left values" {
            leftValue.map { n -> n * 2 } shouldEqual leftValue
        }

        "should chain left values" {
            leftValue.andThen { Either.right(it * 2) } shouldEqual leftValue
        }

        "should chain right values" {
            rightValue.andThen { Either.right(it * 2) }.andThen { Either.right(it + 1) } shouldEqual Either.right(85)
        }

        "should create right value when condition is true" {
            val e: Either<String, Int> = Either.either(true, { 42 }, { "error" })
            e shouldEqual Either.right(42)
        }

        "should create left value when condition is false" {
            val e: Either<String, Int> = Either.either(false, { 42 }, { "error" })
            e shouldEqual Either.left("error")
        }

        "elim should collapse either values" {
            val f: (String) -> Boolean = { it.isBlank() }
            val g: (Int) -> Boolean = { it % 2 == 0 }

            Either.elim(f, g, leftValue) shouldBe false
            Either.elim(f, g, rightValue) shouldBe true
        }
    }
}
