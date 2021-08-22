package org.jglrxavpok.hephaistos.nbt

import org.jetbrains.annotations.Contract
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTCompound @JvmOverloads constructor(_tags: Map<String, NBT> = mapOf()): NBT, NBTCompoundLike {

    private val tags: Map<String, NBT> = java.util.Map.copyOf(_tags)

    override val ID = NBTType.TAG_Compound

    override fun writeContents(destination: DataOutputStream) {
        for(entry in tags.entries) {
            val name = entry.key
            val tag = entry.value
            destination.writeFullyFormedTag(name, tag)
        }
        destination.writeEndTag()
    }

    override fun toSNBT(): String {
        val tagStr = tags.map { entry ->
            "\"${entry.key.replace("\"", "\\\"")}\":${entry.value.toSNBT()}"
        }.joinToString(",")
        return "{$tagStr}"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if(other === this) return true

        if(other == null) return false

        if(other !is NBTCompoundLike) return false

        return tags == other.asMapView()
    }

    override fun hashCode(): Int {
        return tags.hashCode()
    }

    @Contract(pure = true)
    fun modify(lambda: CompoundBuilder) = MutableNBTCompound(tags.toMutableMap()).also { lambda.run(it) }.toCompound()

    @Contract(pure = true)
    fun kmodify(lambda: MutableNBTCompound.() -> Unit) = modify(lambda)

    @Contract(pure = true)
    fun withRemovedKeys(vararg keys: String) = kmodify { keys.forEach { remove(it) } }

    @Contract(pure = true)
    fun withEntries(vararg entries: CompoundEntry) = kmodify { entries.forEach { this[it.key] = it.value } }

    companion object : NBTReaderCompanion<NBTCompound> {

        override fun readContents(source: DataInputStream) = NBT.Kompound {
            do {
                val tag = source.readFullyFormedTag()
                if(tag.second !is NBTEnd) {
                    this[tag.first] = tag.second
                }
            } while(tag.second !is NBTEnd)
        }

        @Contract(pure = true)
        internal fun entry(key: String, value: NBT) = object: CompoundEntry {
            override val key: String
                get() = key
            override val value: NBT
                get() = value
        }

        @JvmField
        val EMPTY = NBTCompound()
    }

    override fun toCompound() = this

    override fun asMapView() = tags

    override fun toMutableCompound() = MutableNBTCompound(this)

    override fun plus(other: NBTCompoundLike): NBTCompound = modify { nbt ->
        nbt += other
    }
}

fun interface CompoundBuilder {
    fun run(map: MutableNBTCompound)
}
