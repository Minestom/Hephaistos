package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTString

/**
 * Represents a block state in a section NBT
 */
data class BlockState @JvmOverloads constructor(val name: String, val properties: Map<String, String> = HashMap()) {
    companion object {
        @JvmField
        val AIR = BlockState(NBT.Kompound {
            this["Name"] = NBT.String("minecraft:air")
            this["Properties"] = NBT.EMPTY
        })
    }

    var hashCode = 0

    /**
     * Constructs a BlockState from the given TAG_Compound
     * @throws AnvilException if the NBT is malformed
     */
    @Throws(AnvilException::class)
    constructor(nbt: NBTCompound): this(nbt.getString("Name") ?: missing("Name"), loadProperties(nbt.getCompound("Properties") ?: NBT.EMPTY))

    /**
     * Converts this BlockState to a TAG_Compound
     */
    fun toNBT(): NBTCompound = NBT.Kompound {
        this["Name"] = NBT.String(name)
        this["Properties"] = NBT.Kompound {
            for((name, value) in properties) {
                this[name] = NBT.String(value)
            }
        }
    }

    // Ironically, IntelliJ's generator for these produces
    // faster code than the Kotlin compiler does
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BlockState) return false

        if (name != other.name) return false
        if (properties != other.properties) return false

        return true
    }

    override fun hashCode(): Int {
        if (hashCode == 0) {
            var result = name.hashCode()
            result = 31 * result + properties.hashCode()
            hashCode = result
        }
        return hashCode
    }
}

private fun loadProperties(properties: NBTCompound): Map<String, String> {
    val result = HashMap<String, String>()
    for ((name, value) in properties) {
        result[name] = (value as NBTString).value
    }
    return result
}