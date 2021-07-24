package org.jglrxavpok.hephaistos.nbt.mutable

import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound

class MutableNBTCompound(private val tags: MutableMap<String, NBT> = mutableMapOf()): MutableMap<String, NBT> by tags {

    fun toNBTCompound(): NBTCompound = NBT.Compound(tags.toMap())

}