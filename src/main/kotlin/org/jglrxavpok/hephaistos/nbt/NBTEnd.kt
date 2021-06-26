package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.UnsupportedOperationException

object NBTEnd: ImmutableNBT<Unit>, MutableNBT<Unit> {
    override val type = NBTType.TAG_End

    override fun readContents(source: DataInputStream) {}

    override fun writeContents(destination: DataOutputStream) {}

    override fun toSNBT() = ""

    override fun toString() = "<TAG_End>"

    override fun deepClone(): NBTEnd = this

    override fun getValue() {
        throw UnsupportedOperationException()
    }

    override fun setValue(v: Unit) {
        throw UnsupportedOperationException()
    }

    override fun asMutable(): NBTEnd = this

    override fun immutableView(): NBTEnd = this
}