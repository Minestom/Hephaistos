package org.jglrxavpok.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTIntArray(var value: IntArray) : NBT {
    val length get()= value.size

    override val ID = NBTTypes.TAG_Int_Array

    constructor(): this(IntArray(0))

    override fun readContents(source: DataInputStream) {
        val length = source.readInt()
        value = IntArray(length)
        for(i in 0 until length) {
            value[i] = source.readInt()
        }
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeInt(value[i])
        }
    }

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "$it" }
        return "[I;$list]"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTIntArray

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(*value.toTypedArray())
    }
}
