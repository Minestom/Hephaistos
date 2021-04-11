package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTInt(value: Int) : NBTNumber<Int>(value) {
    override val type = NBTType.TAG_Int

    constructor(): this(0)

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Int = value

    override fun readContents(source: DataInputStream) {
        value = source.readInt()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(value)
    }

    override fun toSNBT(): String {
        return "$value"
    }

    override fun deepClone() = NBTInt(value)
}
