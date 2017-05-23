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

/**
 * The Maybe type encapsulates an optional value. A value of type Maybe a either
 * contains a value of type a (represented as Just a), or it is empty (represented as
 * None). Using Maybe is a good way to deal with errors or exceptional cases without
 * resorting to drastic measures such as error.
 *
 * @param T the type wrapped in the Maybe
 * @author Carlo Micieli
 * @since 1.0.0
 */
sealed class Maybe<out T> {
    fun <U> map(f: (T) -> U): Maybe<U> {
        return when(this) {
            is Just -> Just(f(this.value))
            else    -> None
        }
    }

    fun forEach(f: (T) -> Unit): Unit {
        this.map(f)
    }

    fun <U> flatMap(f: (T) -> Maybe<U>): Maybe<U> {
        return when(this) {
            is Just -> f(this.value)
            else    -> None
        }
    }

    fun get(): T? {
        return when(this) {
            is Just -> this.value
            else    -> null
        }
    }

    /**
     * Return true when this value is a None, false otherwise
     */
    abstract fun isEmpty(): Boolean

    companion object {
        fun <T> none(): Maybe<T> = None
        fun <T> just(v: T): Maybe<T> = Just(v)
        fun <T> maybe(v: T?): Maybe<T> = if (v == null) None else Just(v)
    }
}

private data class Just<out T>(val value: T) : Maybe<T>() {
    override fun isEmpty(): Boolean = true
    override fun toString(): String {
        return "Just($value)"
    }
}

private object None : Maybe<Nothing>() {
    override fun isEmpty(): Boolean = true
    override fun toString(): String {
        return "None"
    }
}