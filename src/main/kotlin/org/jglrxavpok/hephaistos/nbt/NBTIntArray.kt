package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class NBTIntArray(private var internalValue: IntArray) : ImmutableNBTIntArray(), MutableNBT<IntArray> {

    constructor(): this(IntArray(0))

    override fun readContents(source: DataInputStream) {
        val length = source.readInt()
        internalValue = IntArray(length)
        for (i in 0 until length) {
            internalValue[i] = source.readInt()
        }
    }

    operator fun set(index: Int, v: Int): NBTIntArray {
        internalValue[index] = v
        return this
    }

    override fun toString() = toSNBT()

    override fun getValue() = internalValue

    override fun setValue(v: IntArray) {
        internalValue = v
    }

    override fun deepClone() = NBTIntArray(internalValue.copyOf())

    override fun asMutable(): NBTIntArray = this

}
