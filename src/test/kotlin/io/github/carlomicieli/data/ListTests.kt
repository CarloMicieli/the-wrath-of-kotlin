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

import io.github.carlomicieli.data.List.Companion.cons
import io.kotlintest.specs.FreeSpec
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual

class ListTests : FreeSpec() {
    val emptyList: List<Int> = List.empty()
    val numbers = cons(1, cons(2, cons(3, cons(4, cons(5, List.empty())))))

    init {
        "List.mkString" - {
            "should produce a string representation for the empty list" {
                emptyList.mkString() shouldEqual "[]"
            }

            "should produce a string representation for the non empty list" {
                numbers.mkString() shouldEqual "[1, 2, 3, 4, 5]"
            }
        }

        "List.listOf" - {
            "should create non empty list" {
                List.listOf(1, 2, 3, 4, 5) shouldEqual numbers
            }
        }

        "List.empty" - {
            "should create the empty list" {
                val l: List<Int> = List.empty()
                l.isEmpty shouldBe true
            }

            "should create a list with no head" {
                val l: List<Int> = List.empty()
                l.head shouldBe Maybe.none<Int>()
            }
        }

        "List.cons" - {
            "should create a non empty list" {
                val l = List.cons(42, List.empty())
                l.isEmpty shouldBe false
            }

            "should have the element as head" {
                val l = List.cons(42, List.empty())
                l.head shouldBe Maybe.just(42)
            }
        }

        "List.foldLeft" - {
            "should reduce the empty list" {
                val result = emptyList.foldLeft(42, { acc, x -> acc + x })
                result shouldEqual 42
            }

            "should reduce a list" {
                val result = numbers.foldLeft(0, { acc, x -> acc + x })
                result shouldEqual 15
            }
        }

        "List.foldRight" - {
            "should reduce the empty list" {
                val result = emptyList.foldRight(42, { acc, x -> acc + x })
                result shouldEqual 42
            }

            "should reduce a list" {
                val result = numbers.foldRight(0, { acc, x -> acc + x })
                result shouldEqual 15
            }

            "should return the same list, if function is cons" {
                val result: List<Int> = numbers.foldRight(List.empty(), ::cons)
                result shouldEqual numbers
            }
        }

        "List.length" - {
            "should return 0 for the empty list" {
                emptyList.length() shouldEqual 0
            }

            "should return the number of elements in the list" {
                numbers.length() shouldEqual 5
            }
        }

        "List.reverse" - {
            "should return the empty list unchanged" {
                emptyList.reverse() shouldEqual emptyList
            }

            "should reverse a list" {
                numbers.reverse() shouldEqual cons(5, cons(4, cons(3, cons(2, cons(1, List.empty())))))
            }
        }

        "List.map" - {
            "should return the empty list unchanged" {
                emptyList.map { it * 2 } shouldEqual emptyList
            }

            "should leave a non empty list unchanged when applied the identity function" {
                numbers.map { it } shouldEqual numbers
            }

            "should apply a function to all list elements" {
                numbers.map { it * 2 } shouldEqual cons(2, cons(4, cons(6, cons(8, cons(10, List.empty())))))
            }
        }
    }
}
