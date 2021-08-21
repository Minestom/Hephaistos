package org.jglrxavpok.hephaistos.nbt

import org.jetbrains.annotations.Contract
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound
import java.util.function.BiConsumer

typealias CompoundEntry = Map.Entry<String, NBT>
typealias MutableCompoundEntry = MutableMap.MutableEntry<String, NBT>

interface NBTCompoundLike: NBTCompoundGetters {

    /**
     * Creates a NBTCompound. This will be immutable and copied,
     * regardless if the original object is immutable or mutable.
     */
    @Contract(pure = true)
    fun toCompound(): NBTCompound

    /**
     * Returns a Map representing this compound. The map is not modifiable if this is a NBTCompound, which is immutable.
     */
    fun asMapView(): Map<String, NBT>

    /**
     * Returns a new mutable map containing all key-value pairs from the original compound.
     */
    fun toMutableCompound(): MutableNBTCompound

    // ========================
    // Map-like interface
    // ========================
    /**
     * Returns a read-only Set of all key/value pairs in this compound.
     */
    val entries: Set<CompoundEntry> get()= asMapView().entries

    /**
     * Returns a read-only Set of all keys in this compound.
     */
    val keys: Set<String> get()= asMapView().keys

    /**
     * Returns the number of key/value pairs in the compound.
     */
    val size: Int get()= asMapView().size

    /**
     * Returns a read-only Collection of all values in this compound. Note that this collection may contain duplicate values.
     */
    val values: Collection<NBT> get()= asMapView().values

    /**
     * Returns true if the compound maps one or more keys to the specified value.
     */
    fun containsValue(value: NBT): Boolean = asMapView().containsValue(value)

    /**
     * Returns true if the compound contains the specified key.
     */
    fun containsKey(key: String): Boolean = asMapView().containsKey(key)

    /**
     * Returns true if the compound is empty (contains no elements), false otherwise.
     */
    fun isEmpty(): Boolean = asMapView().isEmpty()

    /**
     * Checks if the compound contains the given key.
     */
    operator fun contains(key: String) = asMapView().contains(key)

    /**
     * Performs the given action on each entry.
     */
    fun forEach(action: BiConsumer<String, NBT>) = asMapView().forEach(action)

    /**
     * Performs the given action on each entry.
     */
    fun forEach(action: (CompoundEntry) -> Unit) = asMapView().forEach(action)

    /**
     * Returns an Iterator over the entries in the compound.
     */
    operator fun iterator(): Iterator<CompoundEntry> = asMapView().iterator()

    /**
     * Returns the value corresponding to the given key, or null if such a key is not present in the compound.
     */
    override fun get(key: String) = asMapView()[key]

    /**
     * Creates a new NBTCompoundLike by replacing or adding entries from this compound with entries from 'other'
     */
    operator fun plus(other: NBTCompoundLike): NBTCompoundLike
}