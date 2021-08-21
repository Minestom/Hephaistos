package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTIntArray constructor(val value: ImmutableIntArray) : NBT, Iterable<Int> {

    val size get() = value.size

    override val ID = NBTType.TAG_Int_Array

    constructor(vararg numbers: Int): this(ImmutableIntArray(*numbers))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(size)
        value.forEach(destination::writeInt)
    }

    operator fun get(index: Int) = value[index]

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "$it" }
        return "[I;$list]"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTIntArray

        if (!(value contentEquals other.value)) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun iterator() = value.iterator()

    companion object : NBTReaderCompanion<NBTIntArray> {

        @JvmField
        val EMPTY = NBTIntArray()

        override fun readContents(source: DataInputStream): NBTIntArray {
            val length = source.readInt()
            val value = ImmutableIntArray(length) { source.readInt() }
            return NBTIntArray(value)
        }
    }
}
