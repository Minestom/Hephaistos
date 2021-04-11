package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTShort(value: Short) : NBTNumber<Short>(value) {
    override val type = NBTType.TAG_Short

    constructor(): this(0)

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Short = value

    override fun readContents(source: DataInputStream) {
        value = source.readShort()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeShort(value.toInt())
    }

    override fun toSNBT(): String {
        return "${value}S"
    }

    override fun deepClone() = NBTShort(value)
}
