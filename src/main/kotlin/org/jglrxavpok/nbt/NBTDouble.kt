package org.jglrxavpok.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTDouble(var value: Double) : NBT {
    override val ID = NBTTypes.TAG_Double

    constructor(): this(0.0)

    override fun readContents(source: DataInputStream) {
        value = source.readDouble()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeDouble(value)
    }

    override fun toSNBT(): String {
        return "${value}D"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTDouble

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }
}
