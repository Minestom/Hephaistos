package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

object NBTEnd: NBT, NBTReaderCompanion<NBTEnd> {
    override val ID = NBTType.TAG_End

    override val value: Any = {
        throw UnsupportedOperationException("This tag has no value")
    };

    override fun readContents(source: DataInputStream): NBTEnd = this

    override fun writeContents(destination: DataOutputStream) {}

    override fun toSNBT() = ""

    override fun toString() = "<TAG_End>"
}