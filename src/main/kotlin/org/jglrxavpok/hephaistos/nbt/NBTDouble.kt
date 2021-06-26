package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTDouble(private var internalValue: Double) : ImmutableNBTDouble(), MutableNBT<Double> {

    constructor(): this(0.0)

    override fun getValue(): Double = internalValue

    override fun readContents(source: DataInputStream) {
        internalValue = source.readDouble()
    }

    override fun deepClone() = NBTDouble(internalValue)

    override fun asMutable(): NBTDouble = this

    override fun setValue(v: Double) {
        internalValue = v
    }
}
