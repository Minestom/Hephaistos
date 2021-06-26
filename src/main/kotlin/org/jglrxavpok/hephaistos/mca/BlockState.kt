package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTString

/**
 * Represents a block state in a section NBT
 */
data class BlockState @JvmOverloads constructor(val name: String, val properties: Map<String, String> = HashMap()) {
    companion object {
        @JvmField
        val Air = BlockState(NBTCompound().setString("Name", "minecraft:air").set("Properties", NBTCompound()))
    }

    /**
     * Constructs a BlockState from the given TAG_Compound
     * @throws AnvilException if the NBT is malformed
     */
    @Throws(AnvilException::class)
    constructor(nbt: NBTCompound): this(nbt.getString("Name") ?: missing("Name"), loadProperties(nbt.getCompound("Properties") ?: NBTCompound()))

    /**
     * Converts this BlockState to a TAG_Compound
     */
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
        result[name] = (value as NBTString).getValue()
    }
    return result
}