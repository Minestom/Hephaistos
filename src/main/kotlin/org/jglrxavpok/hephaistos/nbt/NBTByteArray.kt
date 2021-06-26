package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTByteArray(private var internalValue: ByteArray) : ImmutableNBTByteArray(), MutableNBT<ByteArray> {

    constructor(): this(ByteArray(0))

    override fun readContents(source: DataInputStream) {
        val length = source.readInt()
        internalValue = ByteArray(length)
        source.readFully(internalValue)
    }

    operator fun set(index: Int, v: Byte): NBTByteArray {
        internalValue[index] = v
        return this
    }

    override fun toString() = toSNBT()

    override fun getValue() = internalValue

    override fun setValue(v: ByteArray) {
        internalValue = v
    }

    override fun deepClone() = NBTByteArray(internalValue.copyOf())

    override fun asMutable(): NBTByteArray = this

}
