package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTLongArray(var value: LongArray) : NBT {
    val length get()= value.size

    override val ID = NBTTypes.TAG_Long_Array

    constructor(): this(LongArray(0))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeLong(value[i])
        }
    }

    operator fun get(index: Int) = value[index]

    operator fun set(index: Int, v: Long): NBTLongArray {
        value[index] = v
        return this
    }

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "${it}L" }
        return "[L;$list]"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTLongArray

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(*value.toTypedArray())
    }

    override fun deepClone() = NBTLongArray(value.copyOf())

    companion object : NBTReaderCompanion<NBTLongArray> {
        override fun readContents(source: DataInputStream): NBTLongArray {
            val length = source.readInt()
            val value = LongArray(length)
            for(i in 0 until length) {
                value[i] = source.readLong()
            }

            return NBTLongArray(value)
        }
    }
}
