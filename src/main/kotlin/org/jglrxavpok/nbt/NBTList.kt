package org.jglrxavpok.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class NBTList<Tag: NBT>(val subtagType: Int): Iterable<Tag>, NBT {

    override val ID = NBTTypes.TAG_List

    private val tags = ArrayList<Tag>()
    val length get()= tags.size

    /**
     * Reads the contents of the list, except for the subtag ID, which is supposed to be already read
     * @see NBT.readContents
     */
    override fun readContents(source: DataInputStream) {
        clear()
        val length = source.readInt()
        for (i in 0 until length) {
            val tag = source.readTag(subtagType) as Tag
            tags.add(tag)
        }
    }

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
     * Removes all tags from this list
     */
    fun clear() {
        tags.clear()
    }

    /**
     * Returns the tag at the given index
     */
    operator fun get(index: Int) = tags[index]

    /**
     * Changes the tag at the given index
     */
    operator fun set(index: Int, tag: Tag) {
        tags[index] = tag
    }

    operator fun plusAssign(tag: Tag) {
        add(tag)
    }

    /**
     * Appends a tag of the end of this list
     */
    fun add(tag: Tag) {
        tags += tag
    }

    /**
     * Casts this list to another list type. Can throw a ClassCastException, so be careful
     */
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
        result = 31 * result + Objects.hash(*tags.toArray())
        return result
    }

    companion object {
        @Throws(IOException::class)
        fun readFrom(source: DataInputStream): NBTList<NBT> {
            val subtagType = source.readByte().toInt()
            val list = NBTList<NBT>(subtagType)
            list.readContents(source)
            return list
        }
    }

    override fun iterator(): Iterator<Tag> {
        return tags.iterator();
    }
}