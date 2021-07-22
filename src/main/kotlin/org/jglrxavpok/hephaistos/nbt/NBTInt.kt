package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTInt(value: Int) : NBTNumber<Int>(value) {
    override val ID = NBTTypes.TAG_Int

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Int = value

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(value)
    }

    override fun toSNBT(): String {
        return "$value"
    }

    companion object: NBTReaderCompanion<NBTInt> {
        override fun readContents(source: DataInputStream): NBTInt {
            return NBTInt(source.readInt())
        }
    }
}
