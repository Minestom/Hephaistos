package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

abstract class ImmutableNBTDouble(): ImmutableNBT<Double> {

    final override val type = NBTType.TAG_Double

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getNumberValue(): Double = getValue()

    abstract override fun getValue(): Double

    override fun writeContents(destination: DataOutputStream) {
        destination.writeDouble(getValue())
    }

    override fun toSNBT(): String {
        return "${getValue()}D"
    }

    override fun toString(): String {
        return toSNBT()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTDouble) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun immutableView(): ImmutableNBTDouble = this

    abstract override fun deepClone(): ImmutableNBTDouble

    abstract override fun asMutable(): NBTDouble

    override fun mutableCopy(): NBTDouble {
        return deepClone().asMutable()
    }
}
