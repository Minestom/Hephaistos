package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTByte(private var internalValue: Byte) : ImmutableNBTByte(), MutableNBT<Byte> {

    constructor(boolean: Boolean): this(if(boolean) 1 else 0)

    constructor(): this(0)

    override fun getValue(): Byte = internalValue

    override fun readContents(source: DataInputStream) {
        internalValue = source.readByte()
    }

    override fun deepClone() = NBTByte(internalValue)

    override fun asMutable(): NBTByte = this

    override fun setValue(v: Byte) {
        internalValue = v
    }
}
