package org.jglrxavpok.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTShort(var value: Short) : NBT {
    override val ID = NBTTypes.TAG_Short

    constructor(): this(0)

    override fun readContents(source: DataInputStream) {
        value = source.readShort()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeShort(value.toInt())
    }

    override fun toSNBT(): String {
        return "${value}S"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTShort

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }
}
