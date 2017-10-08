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
 * The Either type represents values with two possibilities: a value of type Either a b is either Left a or Right b.
 *
 * The Either type is sometimes used to represent a value which is either correct or an error; by convention, the Left
 * constructor is used to hold an error value and the Right constructor is used to hold a correct value
 * (mnemonic: "right" also means "correct").
 *
 * @author Carlo Micieli
 * @since 1.0.0
 */
sealed class Either<out L, out R> {

    /**
     * Returns the value contained, if this is a "right". null is returned otherwise
     * @return the right value, null otherwise
     */
    fun get(): R? {
        return when (this) {
            is Right -> this.value
            else -> null
        }
    }

    /**
     * If this is a left, then return the left value in right, or vice versa.
     * @return an Either value
     */
    fun swap(): Either<R, L> = fold({ l -> right(l) }, { r -> left(r) })

    /**
     * Folds either the left or the right side of this value.
     * @param leftMap maps the left value if this is a Left
     * @param rightMap maps the right value if this is a Right
     * @return a value of type U
     */
    fun <U> fold(leftMap: (L) -> U, rightMap: (R) -> U): U {
        return foldEither( { (value) -> leftMap(value) }, { (value) -> rightMap(value) })
    }

    /**
     * Maps this right-biased Either.
     * @param f a mapper function
     * @return a mapped either value
     */
    fun <R2> map(f: (R) -> R2): Either<L, R2> = foldEither( { l -> l }, { (value) -> right(f(value)) })

    /**
     * Returns true when this value is a "left"
     * @return true when this value is a "left", false otherwise
     */
    abstract fun isLeft(): Boolean

    /**
     * Returns true when this value is a "right"
     * @return true when this value is a "right", false otherwise
     */
    abstract fun isRight(): Boolean

    /**
     * Converts a right value to a Just and a left to a none
     * @return a Maybe value
     */
    fun toMaybe(): Maybe<R> = fold( { _ -> Maybe.none() }, { rv -> Maybe.just(rv) })

    companion object {
        /**
         * Creates a new Right value
         * @return a Right value
         */
        fun <R> right(value: R): Either<Nothing, R> = Right(value)

        /**
         * Creates a new Left value
         * @return a Left value
         */
        fun <L> left(value: L): Either<L, Nothing> = Left(value)

        /**
         * If the condition satisfies, return the given A in left, otherwise, return the given B in right.
         * @param cond the condition to be satisfied
         * @param rightF the function to produce the Right value
         * @param leftF the function to produce the Left value
         * @return an Either value
         */
        fun <L, R> either(cond: Boolean, rightF: () -> R, leftF: () -> L): Either<L, R> =
                if (cond) right(rightF()) else left(leftF())

        /**
         * Given a function for both Left and Right to to type a generic type T, collapse down the Either
         * to a value of that type.
         */
        fun <L, R, T> elim(f: (L) -> T, g: (R) -> T, e: Either<L, R>): T = e.fold(f, g)
    }

    private fun <U> foldEither(left: (Left<L>) -> U, right: (Right<R>) -> U): U {
        return when (this) {
            is Right -> right(this)
            is Left -> left(this)
        }
    }
}

private data class Left<out L>(val value: L) : Either<L, Nothing>() {
    override fun isRight(): Boolean = false
    override fun isLeft(): Boolean = true
    override fun toString(): String = "Left($value)"
}

private data class Right<out R>(val value: R) : Either<Nothing, R>() {
    override fun isRight(): Boolean = true
    override fun isLeft(): Boolean = false
    override fun toString(): String = "Right($value)"
}

/**
 * Chain together in many computations that will stop computing if a chain is on a Left.
 * @param f the chaining function
 * @return an Either value
 */
fun <L, R, R2> Either<L, R>.andThen(f: (R) -> Either<L, R2>): Either<L, R2> {
    return when (this) {
        is Left -> this
        is Right -> f(this.value)
    }
}
