package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTByte(value: Byte) : NBTNumber<Byte>(value) {

    constructor(boolean: Boolean): this(if(boolean) 1 else 0)

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
        override fun readContents(source: DataInputStream): NBTByte {
            return NBTByte(source.readByte())
        }
    }
}
