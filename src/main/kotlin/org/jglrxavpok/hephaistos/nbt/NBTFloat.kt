package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTFloat(value: Float) : NBTNumber<Float>(value) {
    override val type = NBTType.TAG_Float

    constructor(): this(0f)

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Float = value

    override fun readContents(source: DataInputStream) {
        value = source.readFloat()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeFloat(value)
    }

    override fun toSNBT(): String {
        return "${value}F"
    }

    override fun deepClone() = NBTFloat(value)
}
