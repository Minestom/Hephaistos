package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTLongArray internal constructor(val value: ImmutableLongArray) : NBT, Iterable<Long> {

    val size get() = value.size

    override val ID = NBTTypes.TAG_Int_Array

    constructor(vararg numbers: Long): this(ImmutableLongArray(*numbers))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(size)
        value.forEach(destination::writeLong)
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

        if (value contentEquals other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun iterator() = value.iterator()

    companion object : NBTReaderCompanion<NBTLongArray> {
        override fun readContents(source: DataInputStream): NBTLongArray {
            val length = source.readInt()
            val value = ImmutableLongArray(length) { source.readLong() }
            return NBTLongArray(value)
        }
    }
}
