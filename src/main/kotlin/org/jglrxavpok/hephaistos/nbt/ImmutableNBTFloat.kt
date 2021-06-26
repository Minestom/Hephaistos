package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

abstract class ImmutableNBTFloat(): ImmutableNBT<Float> {

    final override val type = NBTType.TAG_Float

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getNumberValue(): Float = getValue()

    abstract override fun getValue(): Float

    override fun writeContents(destination: DataOutputStream) {
        destination.writeFloat(getValue())
    }

    override fun toSNBT(): String {
        return "${getValue()}F"
    }

    override fun toString(): String {
        return toSNBT()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTFloat) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun immutableView(): ImmutableNBTFloat = this

    abstract override fun deepClone(): ImmutableNBTFloat

    abstract override fun asMutable(): NBTFloat

    override fun mutableCopy(): NBTFloat {
        return deepClone().asMutable()
    }
}
