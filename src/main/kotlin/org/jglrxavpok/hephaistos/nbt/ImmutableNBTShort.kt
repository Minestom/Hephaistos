package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

abstract class ImmutableNBTShort(): ImmutableNBT<Short> {

    final override val type = NBTType.TAG_Short

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getNumberValue(): Short = getValue()

    abstract override fun getValue(): Short

    override fun writeContents(destination: DataOutputStream) {
        destination.writeShort(getValue().toInt())
    }

    override fun toSNBT(): String {
        return "${getValue()}S"
    }

    override fun toString(): String {
        return toSNBT()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTShort) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun immutableView(): ImmutableNBTShort = this

    abstract override fun deepClone(): ImmutableNBTShort

    abstract override fun asMutable(): NBTShort

    override fun mutableCopy(): NBTShort {
        return deepClone().asMutable()
    }
}
