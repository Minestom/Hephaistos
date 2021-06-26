package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

abstract class ImmutableNBTByteArray: ImmutableNBT<ByteArray> {
    override val type = NBTType.TAG_Byte_Array

    val length get()= getValue().size

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        destination.write(getValue())
    }

    override fun toSNBT(): String {
        val list = getValue().joinToString(",") { "${it}B" }
        return "[B;$list]"
    }

    operator fun get(index: Int) = getValue()[index]

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTByteArray) return false

        if (!getValue().contentEquals(other.getValue())) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(*getValue().toTypedArray())
    }

    override fun immutableView(): ImmutableNBTByteArray = this

    abstract override fun deepClone(): ImmutableNBTByteArray

    abstract override fun asMutable(): NBTByteArray

    override fun mutableCopy(): NBTByteArray {
        return deepClone().asMutable()
    }

}
