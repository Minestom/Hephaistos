package org.jglrxavpok.hephaistos.nbt

import java.util.*

abstract class NBTNumber<Type: Number>(open val value: Type): NBT {

    override fun hashCode(): Int {
        return Objects.hash(value)
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as NBTNumber<Type>

        if (value != other.value) return false

        return true
    }

}