package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTFloat internal constructor(value: Float) : NBTNumber<Float>(value) {
    override val ID = NBTTypes.TAG_Float

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Float = value

    override fun writeContents(destination: DataOutputStream) {
        destination.writeFloat(value)
    }

    override fun toSNBT(): String {
        return "${value}F"
    }

    companion object: NBTReaderCompanion<NBTFloat> {
        override fun readContents(source: DataInputStream): NBTFloat {
            return NBTFloat(source.readFloat())
        }
    }
}
