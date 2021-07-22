package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTByteArray(var value: ByteArray) : NBT {
    val length get()= value.size

    override val ID = NBTTypes.TAG_Byte_Array

    constructor(): this(ByteArray(0))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        destination.write(value)
    }

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "${it}B" }
        return "[B;$list]"
    }

    operator fun get(index: Int) = value[index]

    operator fun set(index: Int, v: Byte): NBTByteArray {
        value[index] = v
        return this
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTByteArray

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(*value.toTypedArray())
    }

    override fun deepClone() = NBTByteArray(value.copyOf())

    companion object : NBTReaderCompanion<NBTByteArray> {
        override fun readContents(source: DataInputStream): NBTByteArray {
            val length = source.readInt()
            val value = ByteArray(length)
            source.readFully(value)
            return NBTByteArray(value)
        }
    }

}
