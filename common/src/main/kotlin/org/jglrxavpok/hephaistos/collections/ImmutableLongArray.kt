package org.jglrxavpok.hephaistos.collections

/**
 * Immutable alternative of [ByteArray]
 */
class ImmutableLongArray(private vararg val numbers: Long) {

    constructor(length: Int, generator: (Int) -> Long) : this(*LongArray(length).apply {
        repeat(length) {
            this[it] = generator(it)
        }
    })

    fun copyInto(destination: LongArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): LongArray {
        System.arraycopy(this.numbers, startIndex, destination, destinationOffset, endIndex - startIndex)
        return destination
    }

    val size get() = numbers.size

    operator fun get(index: Int): Long = numbers[index]

    infix fun contentEquals(other: ImmutableLongArray?) = numbers.contentEquals(other?.numbers)

    override fun hashCode() = numbers.contentHashCode()

    fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((Long) -> CharSequence)? = null): String {
        return numbers.joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableLongArray

        if (!numbers.contentEquals(other.numbers)) return false

        return true
    }

}