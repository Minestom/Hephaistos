package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.UnsupportedOperationException
import java.util.concurrent.ConcurrentHashMap

open class NBTCompound(): ImmutableNBTCompound(), MutableNBT<MutableMap<String, MutableNBT<out Any>>> {
    private var internalValue = mutableMapOf<String, MutableNBT<out Any>>()

    override fun readContents(source: DataInputStream) {
        do {
            val tag = source.readFullyFormedTag()
            if(tag.second !is NBTEnd) {
                getValue()[tag.first] = tag.second
            }
        } while(tag.second !is NBTEnd)
    }

    /**
     * Removes all tags from this compound
     */
    fun clear() {
        getValue().clear()
    }

    /**
     * Sets (and overwrites previous) tag associated to the given key
     */
    operator fun set(key: String, tag: MutableNBT<out Any>): NBTCompound {
        getValue()[key] = tag
        return this
    }

    override fun toString() = toSNBT()

    // Convenience methods

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setByte(key: String, value: Byte) = set(key, NBTByte(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setByteArray(key: String, value: ByteArray) = set(key, NBTByteArray(value))

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
    fun setIntArray(key: String, value: IntArray) = set(key, NBTIntArray(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setLong(key: String, value: Long) = set(key, NBTLong(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setLongArray(key: String, value: LongArray) = set(key, NBTLongArray(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setShort(key: String, value: Short) = set(key, NBTShort(value))

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `set`)
     */
    fun setString(key: String, value: String) = set(key, NBTString(value))

    /**
     * Removes the tag with the given key.
     * If no such key exists, no changes are made to this compound.
     *
     * @return 'this' for chaining
     */
    fun removeTag(key: String): NBTCompound {
        getValue().remove(key)
        return this
    }

    override fun deepClone(): NBTCompound {
        val compound = NBTCompound()
        for((key, value) in getValue()) {
            compound[key] = value.deepClone()
        }
        return compound
    }

    override fun asMutable(): NBTCompound = this

    override fun getValue() = internalValue

    override fun setValue(v: MutableMap<String, MutableNBT<out Any>>) {
        internalValue = v
    }
}
