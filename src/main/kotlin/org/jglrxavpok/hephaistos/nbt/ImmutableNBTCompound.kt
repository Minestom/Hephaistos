package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.UnsupportedOperationException
import java.util.concurrent.ConcurrentHashMap

abstract class ImmutableNBTCompound: ImmutableNBT<MutableMap<String, MutableNBT<out Any>>> {
    override val type = NBTType.TAG_Compound

    /**
     * Number of tags inside this compound
     */
    val size get()= getValue().size

    override fun writeContents(destination: DataOutputStream) {
        for(entry in getValue().entries) {
            val name = entry.key
            val tag = entry.value
            destination.writeFullyFormedTag(name, tag)
        }
        destination.writeEndTag()
    }

    /**
     * Does a tag exist inside this compound with the given key?
     */
    fun containsKey(key: String): Boolean {
        return getValue().containsKey(key)
    }

    /**
     * Creates a list with the current available keys
     */
    fun getKeys(): List<String> = getValue().keys.toList()

    override fun toSNBT(): String {
        val tagStr = getValue().map { entry ->
            "\"${entry.key.replace("\"", "\\\"")}\":${entry.value.toSNBT()}"
        }.joinToString(",")
        return "{$tagStr}"
    }

    override fun toString() = toSNBT()


    // Convenience methods

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getByte(key: String) = (get(key) as? NBTByte)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getByteArray(key: String) = (get(key) as? NBTByteArray)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getCompound(key: String) = (get(key) as? NBTCompound)

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getDouble(key: String) = (get(key) as? NBTDouble)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getFloat(key: String) = (get(key) as? NBTFloat)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getInt(key: String) = (get(key) as? NBTInt)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getIntArray(key: String) = (get(key) as? NBTIntArray)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getLong(key: String) = (get(key) as? NBTLong)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getLongArray(key: String) = (get(key) as? NBTLongArray)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getShort(key: String) = (get(key) as? NBTShort)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun getNumber(key: String): Number? {
        val tag = getValue()[key]
        when (tag) {
            is ImmutableNBTByte -> return tag.getValue()
            is ImmutableNBTDouble -> return tag.getValue()
            is ImmutableNBTFloat -> return tag.getValue()
            is ImmutableNBTInt -> return tag.getValue()
            is ImmutableNBTLong -> return tag.getValue()
            is ImmutableNBTShort -> return tag.getValue()
            else -> return null
        }
    }

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
    fun getString(key: String) = (get(key) as? NBTString)?.getValue()

    /**
     * Returns the value associated to the given key, if any. Returns 'null' otherwise.
     * Also returns 'null' if the tag is not of the correct type (eg getByte on a NBTCompound will yield 'null')
     */
    fun <T: MutableNBT<out Any>> getList(key: String): NBTList<T>? = get(key) as? NBTList<T>

    /**
     * Returns the tag associated to the given key, if any. Returns 'null' otherwise.
     *
     * The generic parameter can be used to ask for a cast. Will return null if the type does not match
     */
    operator fun <T: MutableNBT<out Any>> get(key: String) = getValue()[key] as? T

    /**
     * Returns the tag associated to the given key, if any. Returns the given default value otherwise.
     *
     * The generic parameter can be used to ask for a cast. Will return the default value if the type does not match
     */
    fun <R: Any, T: MutableNBT<R>> getOrDefault(key: String, defaultValue: R): R {
        return get<T>(key)?.getValue() ?: defaultValue
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTCompound) return false

        if (getValue() != other.getValue()) return false

        return true
    }

    override fun hashCode(): Int {
        return getValue().hashCode()
    }

    operator fun iterator(): Iterator<Pair<String, MutableNBT<out Any>>> {
        return object: Iterator<Pair<String, MutableNBT<out Any>>> {
            private val backing = this@ImmutableNBTCompound.getValue().entries.iterator()

            override fun hasNext(): Boolean {
                return backing.hasNext()
            }

            override fun next(): Pair<String, MutableNBT<out Any>> {
                val (name, value) = backing.next()
                return name to value
            }
        }
    }

    override fun immutableView(): ImmutableNBTCompound = this

    abstract override fun deepClone(): ImmutableNBTCompound

    abstract override fun asMutable(): NBTCompound

    override fun mutableCopy(): NBTCompound {
        return deepClone().asMutable()
    }
}
