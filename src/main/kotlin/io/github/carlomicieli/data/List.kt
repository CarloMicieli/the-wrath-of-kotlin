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
 * An immutable linked list.
 *
 * @author Carlo Micieli
 * @since 1.0.0
 */
sealed class List<out T> {
    /**
     * The first element of this list.
     */
    abstract val head: Maybe<T>

    /**
     * The rest of this list.
     */
    abstract val tail: List<T>

    /**
     * true if this list is empty; false otherwise
     */
    abstract val isEmpty: Boolean

    /**
     * Apply a function to every element of a list.
     * @param f the function
     * @return a List
     */
    fun <T1> map(f: (T) -> T1): List<T1> = foldRight(List.empty()) { x, xs -> List.cons(f(x), xs) }

    /**
     * Keep only elements that satisfy the predicate.
     * @param p a predicate
     * @return the list containing the elements that satisfy the predicate
     */
    fun filter(p: (T) -> Boolean): List<T> = TODO("")

    /**
     * Take the first n members of this list.
     */
    fun take(n: Int): List<T> = TODO("")

    /**
     * Drop the first n members of this list.
     */
    fun drop(n: Int): List<T> = TODO("")

    /**
     * Reverse a list
     * @return the list reversed
     */
    fun reverse(): List<T> = foldLeft(List.empty()) { xs, x -> List.cons(x, xs) }

    /**
     * Returns the length of this list
     * @return the length
     */
    fun length(): Int = foldLeft(0) { len, _ -> len + 1 }

    fun mkString(): String {
        return when (this) {
            is Nil -> "[]"
            is Cons -> {
                val h = this.head.map { it.toString() }.get()!!
                val elements = this.tail.foldLeft(h) { str, x -> "$str, $x" }
                "[$elements]"
            }
        }
    }

    /**
     * Reduce a list from the right. NON STACK-SAFE!
     * @param v the initial value
     * @param f the reduction function
     * @return a value
     */
    fun <R> foldRight(v: R, f: (T, R) -> R): R {
        return when (this) {
            is Cons -> f(this.head.get()!!, this.tail.foldRight(v, f))
            is Nil -> v
        }
    }

    /**
     * Reduce a list from the left.
     * @param v the initial value
     * @param f the reduction function
     * @return a value
     */
    fun <R> foldLeft(v: R, f: (R, T) -> R): R {
        tailrec fun go(xs: List<T>, acc: R): R {
            return when (xs) {
                is Cons -> go(xs.tail, f(acc, xs.head.get()!!))
                is Nil -> acc
            }
        }

        return go(this, v)
    }

    companion object {
        /**
         * Prepends the given head element to the given tail element to produce a new list.
         * @param x the new element
         * @param xs a List
         * @return a new List, with x prepended
         */
        fun <T> cons(x: T, xs: List<T>): List<T> = Cons(Maybe.just(x), xs)

        /**
         * Creates an immutable, empty list
         * @return the empty list
         */
        fun <T> empty(): List<T> = Nil

        /**
         * Creates an immutable list
         * @param elements the list elements
         * @return a list
         */
        fun <T> listOf(vararg elements: T): List<T> {
            if (elements.isEmpty()) {
                return Nil
            }
            else {
                var list = List.empty<T>()
                elements.reverse()
                for (el in elements) {
                    list = List.cons(el, list)
                }
                return list
            }
        }
    }
}

private data class Cons<out T>(override val head: Maybe<T>, override val tail: List<T>) : List<T>() {
    override val isEmpty: Boolean = false
}

private object Nil : List<Nothing>() {
    override val head: Maybe<Nothing> = Maybe.none()
    override val tail: List<Nothing> = Nil
    override val isEmpty: Boolean = true
}
