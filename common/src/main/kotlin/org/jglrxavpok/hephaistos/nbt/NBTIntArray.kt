package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTIntArray(val value: ImmutableIntArray) : NBT {

    val length get() = value.size

    override val ID = NBTTypes.TAG_Int_Array

    constructor(): this(ImmutableIntArray())

    constructor(vararg numbers: Int): this(ImmutableIntArray(*numbers))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(length)
        for(i in 0 until length) {
            destination.writeInt(value[i])
        }
    }

    operator fun get(index: Int) = value[index]

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "$it" }
        return "[I;$list]"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTIntArray

        if (value contentEquals other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    @Deprecated("NBT Arrays are immutable", replaceWith = ReplaceWith("this"))
    override fun deepClone() = this

    companion object : NBTReaderCompanion<NBTIntArray> {
        override fun readContents(source: DataInputStream): NBTIntArray {
            val length = source.readInt()
            val value = ImmutableIntArray(length) { source.readInt() }
            return NBTIntArray(value)
        }
    }
}
