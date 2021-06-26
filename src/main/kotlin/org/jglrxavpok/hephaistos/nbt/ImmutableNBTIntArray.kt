package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

abstract class ImmutableNBTIntArray: ImmutableNBT<IntArray> {
    override val type = NBTType.TAG_Int_Array

    val length get()= getValue().size

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeInt(getValue()[i])
        }
    }

    override fun toSNBT(): String {
        val list = getValue().joinToString(",") { "${it}" }
        return "[I;$list]"
    }

    operator fun get(index: Int) = getValue()[index]

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTIntArray) return false

        if (!getValue().contentEquals(other.getValue())) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(*getValue().toTypedArray())
    }

    override fun immutableView(): ImmutableNBTIntArray = this

    abstract override fun deepClone(): ImmutableNBTIntArray

    abstract override fun asMutable(): NBTIntArray

    override fun mutableCopy(): NBTIntArray {
        return deepClone().asMutable()
    }

}
