package org.jglrxavpok.hephaistos.nbt.mutable

import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound

class MutableNBTCompound(private val tags: MutableMap<String, NBT> = mutableMapOf()): MutableMap<String, NBT> {

    override val entries = tags.entries
    override val keys = tags.keys
    override val size = tags.size
    override val values = tags.values

    override fun containsKey(key: String) = tags.containsKey(key)

    override fun containsValue(value: NBT) = tags.containsValue(value)

    override fun get(key: String) = tags[key]

    override fun isEmpty() = tags.isEmpty()

    override fun clear() = tags.clear()

    override fun put(key: String, value: NBT) = tags.put(key, value)

    override fun putAll(from: Map<out String, NBT>) = tags.putAll(from)

    override fun remove(key: String) = tags.remove(key)

    fun toNBTCompound(): NBTCompound = NBT.Compound(tags.toMap())

}