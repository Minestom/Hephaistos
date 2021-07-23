package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

object NBTEnd: NBT, NBTReaderCompanion<NBTEnd> {
    override val ID = NBTTypes.TAG_End

    override fun readContents(source: DataInputStream): NBTEnd = this

    override fun writeContents(destination: DataOutputStream) {}

    override fun toSNBT() = ""

    override fun toString() = "<TAG_End>"
}