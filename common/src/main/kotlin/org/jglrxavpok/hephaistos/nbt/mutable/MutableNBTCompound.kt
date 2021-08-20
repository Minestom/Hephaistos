package org.jglrxavpok.hephaistos.nbt.mutable

import org.jglrxavpok.hephaistos.nbt.NBTCompoundLike
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTCompoundGetters

class MutableNBTCompound(private val tags: MutableMap<String, NBT> = mutableMapOf()): NBTCompoundGetters, MutableMap<String, NBT> by tags, NBTCompoundLike {

    override fun toCompound(): NBTCompound = NBT.Compound(tags.toMap())

    override fun equals(other: Any?): Boolean {

        if (other !is MutableNBTCompound) return false

        return tags == other.tags
    }
    override fun hashCode() = tags.hashCode()

}