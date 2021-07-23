package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream

class NBTCompound internal constructor(val tags: Map<String, NBT> = mapOf()): NBT {

    override val ID = NBTTypes.TAG_Compound

    /**
     * Number of tags inside this compound
     */
    val size get() = tags.size

    override fun writeContents(destination: DataOutputStream) {
        for(entry in tags.entries) {
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
        return tags.containsKey(key)
    }

    /**
     * Returns the tag associated to the given key, if any. Returns 'null' otherwise
     */
    operator fun get(key: String): NBT? {
        return tags[key]
    }

    /**
     * Creates a list with the current available keys
     */
    fun getKeys(): Set<String> = tags.keys

    override fun toSNBT(): String {
        val tagStr = tags.map { entry ->
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
    fun <T: NBT> getList(key: String): NBTList<T>? = get(key) as? NBTList<T>

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

    operator fun iterator(): Iterator<Pair<String, NBT>> {
        return object: Iterator<Pair<String, NBT>> {
            private val backing = this@NBTCompound.tags.entries.iterator()

            override fun hasNext(): Boolean {
                return backing.hasNext()
            }

            override fun next(): Pair<String, NBT> {
                val (name, value) = backing.next()
                return name to value
            }
        }
    }

    fun modify(lambda: CompoundBuilder) = NBTCompound(tags.toMutableMap().also { lambda.run(it) })

    @Deprecated("NBTCompound is immutable", replaceWith = ReplaceWith("this"))
    override fun deepClone() = this

    companion object : NBTReaderCompanion<NBTCompound> {

        @JvmStatic
        val EMPTY = NBTCompound()

        @JvmStatic
        fun compound(lambda: CompoundBuilder) = NBTCompound(mutableMapOf<String, NBT>().also { lambda.run(it) })

        inline fun kompound(crossinline lambda: CompoundMap.() -> Unit) = compound { lambda(it) }

        override fun readContents(source: DataInputStream) = compound {
            do {
                val tag = source.readFullyFormedTag()
                if(tag.second !is NBTEnd) {
                    it[tag.first] = tag.second
                }
            } while(tag.second !is NBTEnd)
        }
    }

}

typealias CompoundMap = MutableMap<String, NBT>

fun interface CompoundBuilder {
    fun run(map: CompoundMap)
}
