package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTByte constructor(value: Byte) : NBTNumber<Byte>(value) {

    override val ID = NBTType.TAG_Byte

    constructor(value: Boolean): this(if(value) 1 else 0)

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Byte = value

    /**
     * Returns true iif the value is not 0
     */
    fun asBoolean(): Boolean = value != 0.toByte()

    override fun writeContents(destination: DataOutputStream) {
        destination.writeByte(value.toInt())
    }

    override fun toSNBT(): String {
        return "${value}B"
    }

    companion object: NBTReaderCompanion<NBTByte> {
        override fun readContents(source: DataInputStream): NBTByte {
            return NBTByte(source.readByte())
        }

        @JvmField
        val ONE = NBTByte(1)

        @JvmField
        val ZERO = NBTByte(0)
    }
}
