package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.UnsupportedOperationException

class NBTEnd: NBT<Unit> {
    override val type = NBTType.TAG_End

    override var value: Unit
        get() = throw UnsupportedOperationException("NBTEnd has no value.")
        set(_value) { throw UnsupportedOperationException("NBTEnd has no value.") }

    override fun readContents(source: DataInputStream) {}

    override fun writeContents(destination: DataOutputStream) {}

    override fun toSNBT() = ""

    override fun toString() = "<TAG_End>"

    override fun deepClone() = NBTEnd()
}