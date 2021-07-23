package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTByteArray(val value: ImmutableByteArray) : NBT {

    val length get() = value.size

    override val ID = NBTTypes.TAG_Int_Array

    constructor(): this(ImmutableByteArray())

    constructor(vararg numbers: Byte): this(ImmutableByteArray(*numbers))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeByte(value[i].toInt())
        }
    }

    operator fun get(index: Int) = value[index]

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "$it" }
        return "[B;$list]"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTByteArray

        if (value contentEquals other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    @Deprecated("NBT Arrays are immutable", replaceWith = ReplaceWith("this"))
    override fun deepClone() = this

    companion object : NBTReaderCompanion<NBTByteArray> {
        override fun readContents(source: DataInputStream): NBTByteArray {
            val length = source.readInt()
            val value = ImmutableByteArray(length) { source.readByte() }
            return NBTByteArray(value)
        }
    }
}
