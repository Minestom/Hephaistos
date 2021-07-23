package org.jglrxavpok.hephaistos.collections

/**
 * Immutable alternative of [ByteArray]
 */
class ImmutableByteArray constructor(private vararg val numbers: Byte): Iterable<Byte> {

    constructor(length: Int, generator: (Int) -> Byte) : this(*ByteArray(length).apply {
        repeat(length) {
            this[it] = generator(it)
        }
    })

    fun copyInto(destination: ByteArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): ByteArray {
        System.arraycopy(this.numbers, startIndex, destination, destinationOffset, endIndex - startIndex)
        return destination
    }

    fun copyArray(): ByteArray = ByteArray(numbers.size).also { copyInto(it) }

    val size get() = numbers.size

    operator fun get(index: Int): Byte = numbers[index]

    infix fun contentEquals(other: ImmutableByteArray?) = numbers.contentEquals(other?.numbers)

    override fun hashCode() = numbers.contentHashCode()

    fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((Byte) -> CharSequence)? = null): String {
        return numbers.joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }

    override fun toString() = "[${joinToString(", ")}]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableByteArray

        if (!numbers.contentEquals(other.numbers)) return false

        return true
    }

    companion object {
        @JvmStatic
        val EMPTY = ImmutableByteArray()

        @JvmStatic
        fun from(vararg numbers: Int) = ImmutableByteArray(*numbers.map { it.toByte() }.toByteArray())
    }

    override fun iterator(): Iterator<Byte> = object : Iterator<Byte> {

        var index = 0

        override fun hasNext() = index < numbers.size

        override fun next() = numbers[index.also { index++ }]
    }

}