package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTIntArray internal constructor(val value: ImmutableIntArray) : NBT, Iterable<Int> {

    val length get() = value.size

    override val ID = NBTTypes.TAG_Int_Array

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeInt(value[i])
        }
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

        if (value contentEquals other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun iterator() = value.iterator()

    companion object : NBTReaderCompanion<NBTIntArray> {
        override fun readContents(source: DataInputStream): NBTIntArray {
            val length = source.readInt()
            val value = ImmutableIntArray(length) { source.readInt() }
            return NBTIntArray(value)
        }
    }
}
