package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTLong(value: Long) : NBTNumber<Long>(value) {
    override val ID = NBTTypes.TAG_Long

    constructor(): this(0)

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getValue(): Long = value

    override fun writeContents(destination: DataOutputStream) {
        destination.writeLong(value)
    }

    override fun toSNBT(): String {
        return "${value}L"
    }

    companion object: NBTReaderCompanion<NBTLong> {
        override fun readContents(source: DataInputStream): NBTLong {
            return NBTLong(source.readLong())
        }
    }
}
