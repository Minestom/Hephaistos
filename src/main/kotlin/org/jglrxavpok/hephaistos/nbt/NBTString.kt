package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTString(var value: String): NBT {

    override val ID = NBTTypes.TAG_String

    constructor(): this("")

    override fun readContents(source: DataInputStream) {
        value = source.readUTF()
    }

    override fun writeContents(destination: DataOutputStream) {
        destination.writeUTF(value)
    }

    override fun toSNBT(): String {
        val escaped = value.replace("\"", "\\\"")
        return "\"$escaped\""
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTString

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }

    override fun deepClone() = NBTString(value)
}