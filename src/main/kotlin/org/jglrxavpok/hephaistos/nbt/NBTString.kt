package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTString(private var internalValue: String) : ImmutableNBTString(), MutableNBT<String> {

    constructor(): this("")

    override fun getValue(): String = internalValue

    override fun readContents(source: DataInputStream) {
        internalValue = source.readUTF()
    }

    override fun deepClone() = NBTString(internalValue)

    override fun asMutable(): NBTString = this

    override fun setValue(v: String) {
        internalValue = v
    }
}
