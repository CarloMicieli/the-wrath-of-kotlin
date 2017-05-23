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

import io.kotlintest.specs.StringSpec
import io.kotlintest.matchers.shouldBe

class MaybeTests : StringSpec() {
    val just42 = Maybe.just(42)
    val none: Maybe<Int> = Maybe.none()

    init {
        "should create a Just value" {
            val just = Maybe.just(42)
            just.get() ?: -1 shouldBe 42
        }

        "should create a None value" {
            val none: Maybe<Int> = Maybe.none()
            none.get() ?: -1 shouldBe -1
        }

        "should create Maybe values" {
            Maybe.maybe(null) shouldBe none
            Maybe.maybe(42) shouldBe just42
        }

        "should produce string representations for Maybe values" {
            just42.toString() shouldBe "Just(42)"
            none.toString() shouldBe "None"
        }

        "should apply a function to the value" {
            var n = 0
            none.forEach { _ -> n += 1 }
            n shouldBe 0
        }

        "should map Maybe values" {
            val times2: (Int) -> Int = { n -> n * 2 }
            just42.map(times2) shouldBe Maybe.just(84)
            none.map(times2) shouldBe none

            just42.map {
                it * 2
            } shouldBe Maybe.just(84)
        }

        "should andThen Maybe values" {
            val times2: (Int) -> Maybe<Int> = { n -> Maybe.just(n * 2) }
            just42.flatMap(times2) shouldBe Maybe.just(84)
            none.flatMap(times2) shouldBe none
        }
    }
}
