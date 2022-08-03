package org.jglrxavpok.hephaistos.collections

/**
 * Immutable alternative of [IntArray]
 *
 * Can not be modified -- if needed, use [copyArray] to create a mutable [IntArray] copy.
 */
class ImmutableIntArray(private vararg val numbers: Int): Iterable<Int> {

    constructor(size: Int, generator: (Int) -> Int) : this(*IntArray(size).apply {
        repeat(size) {
            this[it] = generator(it)
        }
    })

    fun copyInto(destination: IntArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): IntArray {
        System.arraycopy(this.numbers, startIndex, destination, destinationOffset, endIndex - startIndex)
        return destination
    }

    fun copyArray(): IntArray = IntArray(numbers.size).also { copyInto(it) }

    val size get() = numbers.size

    operator fun get(index: Int) = numbers[index]

    infix fun contentEquals(other: ImmutableIntArray?) = numbers.contentEquals(other?.numbers)

    override fun hashCode() = numbers.contentHashCode()

    fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((Int) -> CharSequence)? = null): String {
        return numbers.joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }

    override fun toString(): String {
        return joinToString(prefix = "[", postfix = "]")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableIntArray

        if (!numbers.contentEquals(other.numbers)) return false

        return true
    }

    override fun iterator(): Iterator<Int> = object : Iterator<Int> {

        var index = 0

        override fun hasNext() = index < numbers.size

        override fun next() = numbers[index.also { index++ }]
    }

}