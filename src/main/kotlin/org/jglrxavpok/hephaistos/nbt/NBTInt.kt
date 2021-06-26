package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTInt(private var internalValue: Int) : ImmutableNBTInt(), MutableNBT<Int> {

    constructor(): this(0)

    override fun getValue(): Int = internalValue

    override fun readContents(source: DataInputStream) {
        internalValue = source.readInt()
    }

    override fun deepClone() = NBTInt(internalValue)

    override fun asMutable(): NBTInt = this

    override fun setValue(v: Int) {
        internalValue = v
    }
}
