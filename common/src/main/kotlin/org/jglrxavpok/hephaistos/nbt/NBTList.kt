package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class NBTList<Tag: NBT> internal constructor(val subtagType: Int, private val tags: List<Tag> = listOf()): Iterable<Tag>, NBT {

    override val ID = NBTTypes.TAG_List

    val length get() = tags.size

    /**
     * Writes the contents of the list, WITH for the subtag ID
     * @see NBT.writeContents
     */
    override fun writeContents(destination: DataOutputStream) {
        destination.writeByte(subtagType)
        destination.writeInt(length)
        for(tag in tags) {
            tag.writeContents(destination)
        }
    }

    override fun toSNBT(): String {
        return "[${tags.joinToString(",") { it.toSNBT() }}]"
    }

    override fun toString() = toSNBT()

    /**
     * Returns the tag at the given index
     */
    operator fun get(index: Int) = tags[index]

    /**
     * From ArrayList#indexOf:
     * > Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element. More formally, returns the lowest index i such that Objects.equals(o, get(i)), or -1 if there is no such index.
     */
    fun indexOf(tag: Tag): Int {
        return tags.indexOf(tag)
    }

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
        if (length != other.length) return false
        for (i in 0 until length) {
            if(this[i] != other[i]) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = subtagType
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
         * @see NBT.readContents
         */
        @Throws(IOException::class)
        override fun readContents(source: DataInputStream): NBTList<NBT> {
            val subtagType = source.readByte().toInt()
            val length = source.readInt()

            val list = NBT.List(subtagType, ArrayList<NBT>(length).apply {
                for (i in 0 until length) {
                    this[i] = source.readTag(subtagType)
                }
            })

            return list
        }
    }

    override fun iterator(): Iterator<Tag> {
        return tags.iterator()
    }
}

fun interface NBTListGenerator<T: NBT> {
    fun run(index: Int): T
}