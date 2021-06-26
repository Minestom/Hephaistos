package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

abstract class ImmutableNBTLong(): ImmutableNBT<Long> {

    final override val type = NBTType.TAG_Long

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getNumberValue(): Long = getValue()

    abstract override fun getValue(): Long

    override fun writeContents(destination: DataOutputStream) {
        destination.writeLong(getValue())
    }

    override fun toSNBT(): String {
        return "${getValue()}L"
    }

    override fun toString(): String {
        return toSNBT()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTLong) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun immutableView(): ImmutableNBTLong = this

    abstract override fun deepClone(): ImmutableNBTLong

    abstract override fun asMutable(): NBTLong

    override fun mutableCopy(): NBTLong {
        return deepClone().asMutable()
    }
}
