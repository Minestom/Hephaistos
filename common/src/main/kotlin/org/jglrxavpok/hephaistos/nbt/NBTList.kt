package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class NBTList<Tag: NBT> internal constructor(val subtagType: NBTType<Tag>, private val tags: List<Tag> = listOf()): List<Tag> by tags, NBT {

    override val ID = NBTType.TAG_List

    /**
     * Writes the contents of the list, WITH for the subtag ID
     * @see NBT.writeContents
     */
    override fun writeContents(destination: DataOutputStream) {
        destination.writeByte(subtagType.ordinal)
        destination.writeInt(size)

        tags.forEach { it.writeContents(destination) }
    }

    override fun toSNBT(): String {
        return "[${tags.joinToString(",") { it.toSNBT() }}]"
    }

    override fun toString() = toSNBT()

    /**
     * Returns the tag at the given index
     */
    override operator fun get(index: Int) = tags[index]

    /**
     * Casts this list to another list type. Can throw a ClassCastException, so be careful
     */
    @Suppress("UNCHECKED_CAST") // if that throws, it is the user's fault
    fun <T: NBT> asListOf() = this as NBTList<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTList<*>

        if (subtagType != other.subtagType) return false
        if (size != other.size) return false
        for (i in 0 until size) {
            if(this[i] != other[i]) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = subtagType.ordinal
        result = 31 * result + run {
            var hashCodeResult = 1

            tags.forEach { hashCodeResult = 31 * hashCodeResult + it.hashCode() }

            return@run hashCodeResult
        }
        return result
    }

    companion object : NBTReaderCompanion<NBTList<NBT>> {
        /**
         * Reads the contents of the list, except for the subtag ID, which is supposed to be already read
         * @see NBTReaderCompanion.readContents
         */
        @Throws(IOException::class)
        override fun readContents(source: DataInputStream): NBTList<NBT> {
            val subtagType = source.readByte().toInt()
            val length = source.readInt()

            return NBT.List<NBT>(NBTType.byIndex(subtagType), List(length) {
                source.readTag(subtagType)
            })
        }
    }
}

fun interface NBTListGenerator<T: NBT> {
    fun run(index: Int): T
}