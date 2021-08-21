package org.jglrxavpok.hephaistos.nbt.mutable

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.nbt.*

class MutableNBTCompound @JvmOverloads constructor(private val tags: MutableMap<String, NBT> = mutableMapOf()): MutableMap<String, NBT> by tags, NBTCompoundLike {

    constructor(nbt: NBTCompound): this(HashMap(nbt.tags)) // perform a copy

    override fun toCompound(): NBTCompound = NBT.Compound(tags.toMap())

    override fun equals(other: Any?): Boolean {
        if(other is NBTCompound) return tags == other.tags

        if (other !is MutableNBTCompound) return false

        return tags == other.tags
    }

    override fun hashCode() = tags.hashCode()

    // Convenience methods

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `put`)
     */
    fun set(key: String, value: NBT) = put(key, value)

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setByte(key: String, value: Byte) = set(key, NBTByte(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setByteArray(key: String, value: ImmutableByteArray) = set(key, NBTByteArray(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setByteArray(key: String, value: ByteArray) = set(key, NBTByteArray(*value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setDouble(key: String, value: Double) = set(key, NBTDouble(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setFloat(key: String, value: Float) = set(key, NBTFloat(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setInt(key: String, value: Int) = set(key, NBTInt(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setIntArray(key: String, value: ImmutableIntArray) = set(key, NBTIntArray(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setIntArray(key: String, value: IntArray) = set(key, NBTIntArray(*value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setLong(key: String, value: Long) = set(key, NBTLong(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setLongArray(key: String, value: ImmutableLongArray) = set(key, NBTLongArray(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setLongArray(key: String, value: LongArray) = set(key, NBTLongArray(*value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setShort(key: String, value: Short) = set(key, NBTShort(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setString(key: String, value: String) = set(key, NBTString(value))
}