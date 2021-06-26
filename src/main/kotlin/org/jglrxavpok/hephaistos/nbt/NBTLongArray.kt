package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTLongArray(private var internalValue: LongArray) : ImmutableNBTLongArray(), MutableNBT<LongArray> {

    constructor(): this(LongArray(0))

    override fun readContents(source: DataInputStream) {
        val length = source.readInt()
        internalValue = LongArray(length)
        for (i in 0 until length) {
            internalValue[i] = source.readLong()
        }
    }

    operator fun set(index: Int, v: Long): NBTLongArray {
        internalValue[index] = v
        return this
    }

    override fun toString() = toSNBT()

    override fun getValue() = internalValue

    override fun setValue(v: LongArray) {
        internalValue = v
    }

    override fun deepClone() = NBTLongArray(internalValue.copyOf())

    override fun asMutable(): NBTLongArray = this

}
