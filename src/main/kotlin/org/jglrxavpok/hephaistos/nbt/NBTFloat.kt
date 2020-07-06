package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTFloat(var value: Float) : NBT {
    override val ID = NBTTypes.TAG_Float

    constructor(): this(0f)

    override fun readContents(source: DataInputStream) {
        value = source.readFloat()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeFloat(value)
    }

    override fun toSNBT(): String {
        return "${value}F"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTFloat

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }
}
