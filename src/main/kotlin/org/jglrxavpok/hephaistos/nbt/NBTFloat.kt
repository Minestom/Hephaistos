package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTFloat(private var internalValue: Float) : ImmutableNBTFloat(), MutableNBT<Float> {

    constructor(): this(0.0f)

    override fun getValue(): Float = internalValue

    override fun readContents(source: DataInputStream) {
        internalValue = source.readFloat()
    }

    override fun deepClone() = NBTFloat(internalValue)

    override fun asMutable(): NBTFloat = this

    override fun setValue(v: Float) {
        internalValue = v
    }
}
