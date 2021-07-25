package org.jglrxavpok.hephaistos.nbt

import org.jetbrains.annotations.Contract
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTCompound internal constructor(val tags: Map<String, NBT> = mapOf()): NBT, NBTCompoundGetters, Map<String, NBT> by tags {

    override val ID = NBTTypes.TAG_Compound

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
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTCompound

        if (tags != other.tags) return false

        return true
    }

    override fun hashCode(): Int {
        return tags.hashCode()
    }

    @Contract(pure = true)
    fun modify(lambda: CompoundBuilder) = NBTCompound(MutableNBTCompound(tags.toMutableMap()).also { lambda.run(it) })

    @Contract(pure = true)
    fun kmodify(lambda: MutableNBTCompound.() -> Unit) = modify(lambda)

    @Contract(pure = true)
    fun withRemovedKeys(vararg keys: String) = kmodify { keys.forEach { remove(it) } }

    companion object : NBTReaderCompanion<NBTCompound> {

        override fun readContents(source: DataInputStream) = NBT.Kompound {
            do {
                val tag = source.readFullyFormedTag()
                if(tag.second !is NBTEnd) {
                    this[tag.first] = tag.second
                }
            } while(tag.second !is NBTEnd)
        }
    }

}

fun interface CompoundBuilder {
    fun run(map: MutableNBTCompound)
}
