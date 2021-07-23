package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTByte internal constructor(value: Byte) : NBTNumber<Byte>(value) {

    override val ID = NBTTypes.TAG_Byte

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Byte = value

    override fun writeContents(destination: DataOutputStream) {
        destination.writeByte(value.toInt())
    }

    override fun toSNBT(): String {
        return "${value}B"
    }

    companion object: NBTReaderCompanion<NBTByte> {

        @JvmStatic
        val ONE = NBTByte(1)

        @JvmStatic
        val ZERO = NBTByte(0)

        fun Boolean(flag: Boolean): NBTByte = if (flag) ONE else ZERO

        override fun readContents(source: DataInputStream): NBTByte {
            return NBTByte(source.readByte())
        }
    }
}
