package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

abstract class ImmutableNBTByte(): ImmutableNBT<Byte> {

    final override val type = NBTType.TAG_Byte

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getNumberValue(): Byte = getValue()

    abstract override fun getValue(): Byte

    override fun writeContents(destination: DataOutputStream) {
        destination.writeByte(getValue().toInt())
    }

    override fun toSNBT(): String {
        return "${getValue()}B"
    }

    override fun toString(): String {
        return toSNBT()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTByte) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun immutableView(): ImmutableNBTByte = this

    abstract override fun deepClone(): ImmutableNBTByte

    abstract override fun asMutable(): NBTByte

    override fun mutableCopy(): NBTByte {
        return deepClone().asMutable()
    }
}
