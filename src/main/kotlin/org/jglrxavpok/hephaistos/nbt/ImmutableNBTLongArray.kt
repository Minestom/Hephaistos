package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

abstract class ImmutableNBTLongArray: ImmutableNBT<LongArray> {
    override val type = NBTType.TAG_Long_Array

    val length get()= getValue().size

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeLong(getValue()[i])
        }
    }

    override fun toSNBT(): String {
        val list = getValue().joinToString(",") { "${it}L" }
        return "[L;$list]"
    }

    operator fun get(index: Int) = getValue()[index]

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTLongArray) return false

        if (!getValue().contentEquals(other.getValue())) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(*getValue().toTypedArray())
    }

    override fun immutableView(): ImmutableNBTLongArray = this

    abstract override fun deepClone(): ImmutableNBTLongArray

    abstract override fun asMutable(): NBTLongArray

    override fun mutableCopy(): NBTLongArray {
        return deepClone().asMutable()
    }

}
