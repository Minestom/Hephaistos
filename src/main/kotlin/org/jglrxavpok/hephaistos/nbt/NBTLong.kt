package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTLong(private var internalValue: Long) : ImmutableNBTLong(), MutableNBT<Long> {

    constructor(): this(0)

    override fun getValue(): Long = internalValue

    override fun readContents(source: DataInputStream) {
        internalValue = source.readLong()
    }

    override fun deepClone() = NBTLong(internalValue)

    override fun asMutable(): NBTLong = this

    override fun setValue(v: Long) {
        internalValue = v
    }
}
