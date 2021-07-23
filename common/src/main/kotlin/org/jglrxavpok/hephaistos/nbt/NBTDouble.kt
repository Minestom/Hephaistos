package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTDouble internal constructor(value: Double) : NBTNumber<Double>(value) {

    override val ID = NBTTypes.TAG_Double

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Double = value

    override fun writeContents(destination: DataOutputStream) {
        destination.writeDouble(value)
    }

    override fun toSNBT(): String {
        return "${value}D"
    }

    companion object: NBTReaderCompanion<NBTDouble> {
        override fun readContents(source: DataInputStream): NBTDouble {
            return NBTDouble(source.readDouble())
        }
    }
}
