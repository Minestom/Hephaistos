package org.jglrxavpok.mca

import org.jglrxavpok.mca.AnvilException.Companion.missing
import org.jglrxavpok.nbt.NBTCompound
import org.jglrxavpok.nbt.NBTString

/**
 * Represents a block state in a section NBT
 */
data class BlockState(val name: String, val properties: Map<String, String>) {
    companion object {
        @JvmField
        val Air = BlockState(NBTCompound().setString("Name", "minecraft:air").set("Properties", NBTCompound()))
    }

    constructor(nbt: NBTCompound): this(nbt.getString("Name") ?: missing("Name"), loadProperties(nbt.getCompound("Properties") ?: NBTCompound()))

    fun toNBT(): NBTCompound {
        val propertiesNBT = NBTCompound()
        for((name, value) in properties) {
            propertiesNBT[name] = NBTString(value)
        }
        return NBTCompound().setString("Name", name).set("Properties", propertiesNBT)
    }
}

private fun loadProperties(properties: NBTCompound): Map<String, String> {
    val result = HashMap<String, String>()
    for ((name, value) in properties) {
        result[name] = (value as NBTString).value
    }
    return result
}