package org.jglrxavpok.hephaistos.collections

/**
 * Immutable alternative of [ByteArray]
 */
class ImmutableByteArray(private vararg val numbers: Byte) {

    constructor(length: Int, generator: (Int) -> Byte) : this(*ByteArray(length).apply {
        repeat(length) {
            this[it] = generator(it)
        }
    })

    fun copyInto(destination: ByteArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): ByteArray {
        System.arraycopy(this.numbers, startIndex, destination, destinationOffset, endIndex - startIndex)
        return destination
    }

    val size get() = numbers.size

    operator fun get(index: Int): Byte = numbers[index]

    infix fun contentEquals(other: ImmutableByteArray?) = numbers.contentEquals(other?.numbers)

    override fun hashCode() = numbers.contentHashCode()

    fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((Byte) -> CharSequence)? = null): String {
        return numbers.joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableByteArray

        if (!numbers.contentEquals(other.numbers)) return false

        return true
    }

}