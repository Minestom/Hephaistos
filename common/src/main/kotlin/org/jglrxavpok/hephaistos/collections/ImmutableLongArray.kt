package org.jglrxavpok.hephaistos.collections

/**
 * Immutable alternative of [LongArray]
 *
 * Can not be modified -- if needed, use [copyArray] to create a mutable [LongArray] copy.
 */
class ImmutableLongArray(private vararg val numbers: Long): Iterable<Long> {

    constructor(length: Int, generator: (Int) -> Long) : this(*LongArray(length).apply {
        repeat(length) {
            this[it] = generator(it)
        }
    })

    fun copyInto(destination: LongArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): LongArray {
        System.arraycopy(this.numbers, startIndex, destination, destinationOffset, endIndex - startIndex)
        return destination
    }

    fun copyArray(): LongArray = LongArray(numbers.size).also { copyInto(it) }

    val size get() = numbers.size

    operator fun get(index: Int): Long = numbers[index]

    infix fun contentEquals(other: ImmutableLongArray?) = numbers.contentEquals(other?.numbers)

    override fun hashCode() = numbers.contentHashCode()

    fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((Long) -> CharSequence)? = null): String {
        return numbers.joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }

    override fun toString() = "[${joinToString(", ")}]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableLongArray

        if (!numbers.contentEquals(other.numbers)) return false

        return true
    }

    override fun iterator(): Iterator<Long> = object : Iterator<Long> {

        var index = 0

        override fun hasNext() = index < numbers.size

        override fun next() = numbers[index.also { index++ }]
    }

}