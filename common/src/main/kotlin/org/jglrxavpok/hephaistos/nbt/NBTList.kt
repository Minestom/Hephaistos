package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*
import java.util.function.Consumer

class NBTList<Tag: NBT> @JvmOverloads constructor(val subtagType: NBTType<out Tag>, _tags: List<out Tag> = listOf()): NBT {

    private val tags: List<out Tag> = java.util.List.copyOf(_tags)

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
    operator fun get(index: Int) = tags[index]

    /**
     * Casts this list to another list type. Can throw a ClassCastException, so be careful
     */
    @Suppress("UNCHECKED_CAST") // if that throws, it is the user's fault
    fun <T: NBT> asListOf() = this as NBTList<T>

    /**
     * Returns a List representing this compound. The list is not modifiable
     */
    fun asListView(): List<Tag> = tags

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

            return NBT.List(NBTType.byIndex(subtagType), List(length) {
                source.readTag(subtagType)
            })
        }
    }

    // ===============
    // List-like interface
    // ===============
    /**
     * Returns the size of the list.
     */
    val size get()= tags.size

    /**
     * Checks if the specified element is contained in this list.
     */
    operator fun contains(element: Tag) = element in tags

    /**
     * Checks if all elements in the specified collection are contained in this list.
     */
    fun containsAll(elements: Collection<Tag>) = tags.containsAll(elements)

    /**
     * Returns the index of the first occurrence of the specified element in the list, or -1 if the specified element is not contained in the list.
     */
    fun indexOf(element: Tag) = tags.indexOf(element)

    /**
     * Returns true if the collection is empty (contains no elements), false otherwise.
     */
    fun isEmpty() = tags.isEmpty()

    /**
     * Returns false if the collection is empty (contains no elements), true otherwise.
     */
    fun isNotEmpty() = tags.isNotEmpty()

    /**
     * Returns an iterator over the elements of this object.
     */
    operator fun iterator() = tags.iterator()

    /**
     * Returns a list iterator over the elements in this list (in proper sequence), starting at the specified index.
     */
    fun listIterator(index: Int = 0) = tags.listIterator(index)

    /**
     * Returns a view of the portion of this list between the specified fromIndex (inclusive) and toIndex (exclusive). The returned list is backed by this list.
     */
    fun subList(fromIndex: Int, toIndex: Int) = NBTList(subtagType, tags.subList(fromIndex, toIndex))

    /**
     * Performs the given action on each element.
     */
    fun forEach(action: (Tag) -> Unit) = tags.forEach(action)

    /**
     * Performs the given action on each element.
     */
    fun forEach(action: Consumer<Tag>) = tags.forEach(action)
}

fun interface NBTListGenerator<T: NBT> {
    fun run(index: Int): T
}