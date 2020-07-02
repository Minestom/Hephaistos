package org.jglrxavpok.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTLong(var value: Long) : NBT {
    override val ID = NBTTypes.TAG_Long

    constructor(): this(0)

    override fun readContents(source: DataInputStream) {
        value = source.readLong()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeLong(value)
    }

    override fun toSNBT(): String {
        return "${value}L"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTLong

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }
}
