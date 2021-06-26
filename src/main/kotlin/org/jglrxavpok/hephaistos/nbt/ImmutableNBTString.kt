package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

abstract class ImmutableNBTString(): ImmutableNBT<String> {

    override val type = NBTType.TAG_String

    override fun writeContents(destination: DataOutputStream) {
        destination.writeUTF(getValue())
    }

    override fun toSNBT(): String {
        val escaped = getValue().replace("\"", "\\\"")
        return "\"$escaped\""
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTString) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(getValue())
    }

    override fun immutableView(): ImmutableNBTString = this

    abstract override fun deepClone(): ImmutableNBTString

    abstract override fun asMutable(): NBTString

    override fun mutableCopy(): NBTString {
        return deepClone().asMutable()
    }
}