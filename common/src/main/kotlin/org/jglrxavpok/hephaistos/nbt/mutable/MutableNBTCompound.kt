package org.jglrxavpok.hephaistos.nbt.mutable

import org.jglrxavpok.hephaistos.nbt.CompoundLike
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTCompoundGetters

class MutableNBTCompound(private val tags: MutableMap<String, NBT> = mutableMapOf()): NBTCompoundGetters, MutableMap<String, NBT> by tags, CompoundLike {

    override fun toCompound(): NBTCompound = NBT.Compound(tags.toMap())

    override fun equals(other: Any?): Boolean = tags == other
    override fun hashCode() = tags.hashCode()

}