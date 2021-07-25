package org.jglrxavpok.hephaistos.nbt.mutable

import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTCompoundGetters

class MutableNBTCompound(private val tags: MutableMap<String, NBT> = mutableMapOf()): NBTCompoundGetters, MutableMap<String, NBT> by tags {

    fun toNBTCompound(): NBTCompound = NBT.Compound(tags.toMap())

}