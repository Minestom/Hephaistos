package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

abstract class ImmutableNBTInt(): ImmutableNBT<Int> {

    final override val type = NBTType.TAG_Int

    // help Java compiler to find the correct type (boxed vs primitive types)
    fun getNumberValue(): Int = getValue()

    abstract override fun getValue(): Int

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(getValue().toInt())
    }

    override fun toSNBT(): String {
        return "${getValue()}"
    }

    override fun toString(): String {
        return toSNBT()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTInt) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun immutableView(): ImmutableNBTInt = this

    abstract override fun deepClone(): ImmutableNBTInt

    abstract override fun asMutable(): NBTInt

    override fun mutableCopy(): NBTInt {
        return deepClone().asMutable()
    }
}
