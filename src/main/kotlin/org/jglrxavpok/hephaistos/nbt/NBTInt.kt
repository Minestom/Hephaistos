package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTInt(var value: Int) : NBT {
    override val ID = NBTTypes.TAG_Int

    constructor(): this(0)

    override fun readContents(source: DataInputStream) {
        value = source.readInt()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(value)
    }

    override fun toSNBT(): String {
        return "$value"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTInt

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }
}
