package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.UnsupportedOperationException
import java.util.concurrent.ConcurrentHashMap

open class NBTCompound(): NBT<Map<String, NBT<out Any>>> {
    override val type = NBTType.TAG_Compound

    private val tags = ConcurrentHashMap<String, NBT<out Any>>()

    override var value: Map<String, NBT<out Any>>
        get() = LinkedHashMap(tags)
        set(_value) { throw UnsupportedOperationException("Not allowed to modify the map used internally.") }

    /**
     * Number of tags inside this compound
     */
    val size get()= tags.size

    override fun readContents(source: DataInputStream) {
        do {
            val tag = source.readFullyFormedTag()
            if(tag.second !is NBTEnd) {
                tags[tag.first] = tag.second
            }
        } while(tag.second !is NBTEnd)
    }

    override fun writeContents(destination: DataOutputStream) {
        for(entry in tags.entries) {
            val name = entry.key
            val tag = entry.value
            destination.writeFullyFormedTag(name, tag)
        }
        destination.writeEndTag()
    }

    /**
     * Removes all tags from this compound
     */
    fun clear() {
        tags.clear()
    }

    /**
     * Does a tag exist inside this compound with the given key?
     */
    fun containsKey(key: String): Boolean {
        return tags.containsKey(key)
    }

    /**
     * Sets (and overwrites previous) tag associated to the given key
     */
    operator fun set(key: String, tag: NBT<out Any>): NBTCompound {
        tags[key] = tag
        return this
    }

    /**
     * Creates a list with the current available keys
     */
    fun getKeys(): List<String> = tags.keys.toList()

    override fun toSNBT(): String {
        val tagStr = tags.map { entry ->
            "\"${entry.key.replace("\"", "\\\"")}\":${entry.value.toSNBT()}"
        }.joinToString(",")
        return "{$tagStr}"
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
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getByte(key: String) = (get(key) as? NBTByte)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getByteArray(key: String) = (get(key) as? NBTByteArray)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getCompound(key: String) = (get(key) as? NBTCompound)

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getDouble(key: String) = (get(key) as? NBTDouble)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getFloat(key: String) = (get(key) as? NBTFloat)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getInt(key: String) = (get(key) as? NBTInt)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getIntArray(key: String) = (get(key) as? NBTIntArray)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getLong(key: String) = (get(key) as? NBTLong)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getLongArray(key: String) = (get(key) as? NBTLongArray)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getShort(key: String) = (get(key) as? NBTShort)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getNumber(key: String) = (get(key) as? NBTNumber<out Number>)?.value

    /**
     * Calls getNumber and cast to the desired type.
     * This is different from the get#Type# methods as this will attempt to cast the value instead of returning 'null'
     * if the tag is not the exact type (eg getByte returns null for a float, but getAsByte will cast)
     */
    fun getAsByte(key: String) = getNumber(key)?.toByte()

    /**
     * Calls getNumber and cast to the desired type.
     * This is different from the get#Type# methods as this will attempt to cast the value instead of returning 'null'
     * if the tag is not the exact type (eg getByte returns null for a float, but getAsByte will cast)
     */
    fun getAsDouble(key: String) = getNumber(key)?.toDouble()

    /**
     * Calls getNumber and cast to the desired type.
     * This is different from the get#Type# methods as this will attempt to cast the value instead of returning 'null'
     * if the tag is not the exact type (eg getByte returns null for a float, but getAsByte will cast)
     */
    fun getAsFloat(key: String) = getNumber(key)?.toFloat()

    /**
     * Calls getNumber and cast to the desired type.
     * This is different from the get#Type# methods as this will attempt to cast the value instead of returning 'null'
     * if the tag is not the exact type (eg getByte returns null for a float, but getAsByte will cast)
     */
    fun getAsInt(key: String) = getNumber(key)?.toInt()

    /**
     * Calls getNumber and cast to the desired type.
     * This is different from the get#Type# methods as this will attempt to cast the value instead of returning 'null'
     * if the tag is not the exact type (eg getByte returns null for a float, but getAsByte will cast)
     */
    fun getAsLong(key: String) = getNumber(key)?.toLong()

    /**
     * Calls getNumber and cast to the desired type.
     * This is different from the get#Type# methods as this will attempt to cast the value instead of returning 'null'
     * if the tag is not the exact type (eg getByte returns null for a float, but getAsByte will cast)
     */
    fun getAsShort(key: String) = getNumber(key)?.toShort()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getString(key: String) = (get(key) as? NBTString)?.value

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun <T: NBT<out Any>> getList(key: String): NBTList<T>? = get(key) as? NBTList<T>

    /**
     * Returns the tag associated to the given key, if any. Returns 'null' otherwise.
     *
     * The generic parameter can be used to ask for a cast. Will return null if the type does not match
     */
    operator fun <T: NBT<out Any>> get(key: String) = tags[key] as? T

    /**
     * Returns the tag associated to the given key, if any. Returns the given default value otherwise.
     *
     * The generic parameter can be used to ask for a cast. Will return the default value if the type does not match
     */
    fun <R: Any, T: NBT<R>> getOrDefault(key: String, defaultValue: R): R {
        return get<T>(key)?.value ?: defaultValue
    }

    /**
     * Removes the tag with the given key.
     * If no such key exists, no changes are made to this compound.
     *
     * @return 'this' for chaining
     */
    fun removeTag(key: String): NBTCompound {
        tags.remove(key)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTCompound

        if (tags != other.tags) return false

        return true
    }

    override fun hashCode(): Int {
        return tags.hashCode()
    }

    operator fun iterator(): Iterator<Pair<String, NBT<out Any>>> {
        return object: Iterator<Pair<String, NBT<out Any>>> {
            private val backing = this@NBTCompound.tags.entries.iterator()

            override fun hasNext(): Boolean {
                return backing.hasNext()
            }

            override fun next(): Pair<String, NBT<out Any>> {
                val (name, value) = backing.next()
                return name to value
            }
        }
    }

    override fun deepClone() = NBTCompound().let {
        for((key, value) in tags) {
            it[key] = value.deepClone()
        }
        it
    }

}
