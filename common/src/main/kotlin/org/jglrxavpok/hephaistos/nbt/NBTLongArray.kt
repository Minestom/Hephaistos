package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTLongArray constructor(override val value: ImmutableLongArray) : NBT, Iterable<Long> {

    val size get() = value.size

    override val ID = NBTType.TAG_Long_Array

    constructor(vararg numbers: Long): this(ImmutableLongArray(*numbers))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(size)
        value.numbers.forEach(destination::writeLong)
    }

    operator fun get(index: Int) = value[index]

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "${it}L" }
        return "[L;$list]"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTLongArray

        if (!(value contentEquals other.value)) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun iterator() = value.iterator()

    companion object : NBTReaderCompanion<NBTLongArray> {
        @JvmField
        val EMPTY = NBTLongArray()

        override fun readContents(source: DataInputStream): NBTLongArray {
            val length = source.readInt()
            val inArray = source.readNBytes(length * 8)
            val outArray = LongArray(length)

            for(i in 0 until length) {
                val index = i * 8
                outArray[i] = (inArray[index].toLong() shl 56) or
                        ((inArray[index+1].toLong() and 0xFF) shl 48) or
                        ((inArray[index+2].toLong() and 0xFF) shl 40) or
                        ((inArray[index+3].toLong() and 0xFF) shl 32) or
                        ((inArray[index+4].toLong() and 0xFF) shl 24) or
                        ((inArray[index+5].toLong() and 0xFF) shl 16) or
                        ((inArray[index+6].toLong() and 0xFF) shl 8) or
                        (inArray[index+7].toLong() and 0xFF)
            }

            return NBTLongArray(*outArray)
        }
    }
}
