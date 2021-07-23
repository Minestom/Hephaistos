package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTLongArray(val value: ImmutableLongArray) : NBT {

    val length get() = value.size

    override val ID = NBTTypes.TAG_Int_Array

    constructor(): this(ImmutableLongArray())

    constructor(vararg numbers: Long): this(ImmutableLongArray(*numbers))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeLong(value[i])
        }
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

    @Deprecated("NBT Arrays are immutable", replaceWith = ReplaceWith("this"))
    override fun deepClone() = this

    companion object : NBTReaderCompanion<NBTLongArray> {
        override fun readContents(source: DataInputStream): NBTLongArray {
            val length = source.readInt()
            val value = ImmutableLongArray(length) { source.readLong() }
            return NBTLongArray(value)
        }
    }
}
