package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTDouble(value: Double) : NBTNumber<Double>(value) {

    override val type = NBTType.TAG_Double

    constructor(): this(0.0)

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Double = value

    override fun readContents(source: DataInputStream) {
        value = source.readDouble()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeDouble(value)
    }

    override fun toSNBT(): String {
        return "${value}D"
    }

    override fun deepClone() = NBTDouble(value)
}
