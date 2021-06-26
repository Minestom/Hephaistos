package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTShort(private var internalValue: Short) : ImmutableNBTShort(), MutableNBT<Short> {

    constructor(): this(0)

    override fun getValue(): Short = internalValue

    override fun readContents(source: DataInputStream) {
        internalValue = source.readShort()
    }

    override fun deepClone() = NBTShort(internalValue)

    override fun asMutable(): NBTShort = this

    override fun setValue(v: Short) {
        internalValue = v
    }
}
