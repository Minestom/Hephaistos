package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.UnsupportedOperationException
import java.util.*
import kotlin.collections.ArrayList

open class NBTList<Tag: NBT<out Any>>(val subtagType: NBTType): Iterable<Tag>, NBT<List<Tag>> {

    override val type = NBTType.TAG_List

    private val tags = ArrayList<Tag>()
    val length get()= tags.size

    override var value: List<Tag>
        get() = LinkedList(tags)
        set(_value) { throw UnsupportedOperationException("Not allowed to modify the list used internally.") }

    /**
     * Reads the contents of the list, except for the subtag ID, which is supposed to be already read
     * @see NBT.readContents
     */
    override fun readContents(source: DataInputStream) {
        clear()
        val length = source.readInt()
        for (i in 0 until length) {
            @Suppress("UNCHECKED_CAST") // Due to the specs and the way Hephaistos is made, this cast should not fail.
            val tag = source.readTag(subtagType) as Tag
            tags.add(tag)
        }
    }

    /**
     * Writes the contents of the list, WITH for the subtag ID
     * @see NBT.writeContents
     */
    override fun writeContents(destination: DataOutputStream) {
        destination.writeByte(subtagType.asID())
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
        if(tag.type != subtagType)
            throw NBTException("Element to add is not of type ${subtagType.name} but of type ${NBTType.name(tag.ID)}")
        tags += tag
    }

    internal fun unsafeAdd(tag: NBT<out Any>) {
        if(tag.type != subtagType)
            throw NBTException("Element to add is not of type ${subtagType.name} but of type ${NBTType.name(tag.ID)}")
        tags += (tag as? Tag) ?: throw NBTException("Could not cast $tag to supported-by-this-list type")
    }

    /**
     * From ArrayList#removeAt:
     * > Removes the element at the specified position in this list. Shifts any subsequent elements to the left (subtracts one from their indices).
     */
    fun removeAt(index: Int): NBTList<Tag>  {
        tags.removeAt(index)
        return this
    }

    /**
     * From ArrayList#remove:
     * > Removes the first occurrence of the specified element from this list, if it is present. If the list does not contain the element, it is unchanged. More formally, removes the element with the lowest index i such that Objects.equals(o, get(i)) (if such an element exists). Returns true if this list contained the specified element (or equivalently, if this list changed as a result of the call).
     */
    fun remove(tag: Tag): NBTList<Tag> {
        tags.remove(tag)
        return this
    }

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
    fun <T: NBT<out Any>> asListOf() = this as NBTList<T>

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
        var result = subtagType.asID()
        result = 31 * result + Objects.hash(*tags.toArray())
        return result
    }

    companion object {
        @Throws(IOException::class)
        fun readFrom(source: DataInputStream): NBTList<NBT<out Any>> {
            val subtagType = source.readByte().toInt()
            val list = NBTList<NBT<out Any>>(NBTType.fromID(subtagType))
            list.readContents(source)
            return list
        }
    }

    override fun iterator(): Iterator<Tag> {
        return tags.iterator()
    }

    override fun deepClone() = NBTList<Tag>(subtagType).let {
        tags.map { element ->
            element.deepClone() as Tag
        }.forEach(it::add)
        it
    }
}