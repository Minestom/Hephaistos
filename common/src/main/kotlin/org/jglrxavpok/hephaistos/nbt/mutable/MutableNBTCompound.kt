package org.jglrxavpok.hephaistos.nbt.mutable

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.nbt.*

class MutableNBTCompound @JvmOverloads constructor(private val tags: MutableMap<String, NBT> = mutableMapOf()): NBTCompoundLike {

    constructor(nbt: NBTCompound): this(nbt.asMapView().toMutableMap()) // perform a copy

    override fun toCompound(): NBTCompound = NBT.Compound(tags.toMap())

    override fun equals(other: Any?): Boolean {
        if(other === this) return true

        if(other == null) return false

        if(other !is NBTCompoundLike) return false

        return tags == other.asMapView()
    }

    override fun hashCode() = tags.hashCode()

    override fun toString() = toCompound().toString()
    // Convenience methods

    /**
     * Sets (and overwrites previous) tag associated to the given key (shorthand method that in turn calls `put`)
     */
    operator fun set(key: String, value: NBT): MutableNBTCompound {
        tags[key] = value
        return this
    }

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

    override fun asMapView(): Map<String, NBT> {
        return tags
    }

    override fun toMutableCompound(): MutableNBTCompound {
        return MutableNBTCompound(HashMap(tags))
    }

    // ============================
    // MutableMap-like interface
    // ============================
    /**
     * Removes all elements from this compound.
     */
    fun clear() {
        tags.clear()
    }

    /**
     * Associates the specified value with the specified key in the compound.
     */
    fun put(key: String, value: NBT) = tags.put(key, value)

    /**
     * Updates this compound with key/value pairs from the specified compound from.
     * Returns itself for chaining
     */
    fun putAll(from: Map<String, NBT>): MutableNBTCompound {
        for((key, value) in from) {
            this[key] = value
        }
        return this
    }

    /**
     * Updates this compound with key/value pairs from the specified compound from.
     * Returns itself for chaining
     */
    fun putAll(from: NBTCompoundLike): MutableNBTCompound {
        return putAll(from.asMapView())
    }

    /**
     * Updates this compound with key/value pairs from the specified compound from.
     * Returns itself for chaining
     */
    fun setAll(from: Map<String, NBT>) = putAll(from)

    /**
     * Updates this compound with key/value pairs from the specified compound from.
     * Returns itself for chaining
     */
    fun setAll(from: NBTCompoundLike) = putAll(from)

    /**
     * Updates this compound with key/value pairs from the specified compound other.
     */
    operator fun plusAssign(other: Map<String, NBT>): Unit {
        putAll(other)
    }

    /**
     * Updates this compound with key/value pairs from the specified compound other.
     */
    operator fun plusAssign(other: NBTCompoundLike): Unit {
        plusAssign(other.asMapView())
    }

    /**
     * Updates this compound with the given key/value pair.
     */
    operator fun plusAssign(p: Pair<String, NBT>): Unit {
        tags += p
    }

    /**
     * Creates a new mutable compound by replacing or adding entries to this compound from another compound.
     */
    override operator fun plus(other: NBTCompoundLike): MutableNBTCompound {
        val mutable = MutableNBTCompound(tags.toMutableMap())
        mutable += other
        return mutable
    }

    /**
     * Removes the specified key and its corresponding value from this compound.
     */
    fun remove(key: String) = tags.remove(key)

    /**
     * Removes the entry for the specified key only if it is mapped to the specified value.
     */
    fun remove(key: String, value: NBT) = tags.remove(key, value)

    /**
     * Returns the value for the given key. If the key is not found in the compound, calls the defaultValue function, puts its result into the compound under the given key and returns it.
     */
    fun getOrPut(key: String, defaultValue: () -> NBT) = tags.getOrPut(key, defaultValue)

    /**
     * Returns a MutableIterator over the mutable entries in the MutableNBTCompound.
     */
    override fun iterator(): MutableIterator<MutableCompoundEntry> = tags.iterator()
}