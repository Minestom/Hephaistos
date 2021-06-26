package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.UnsupportedOperationException
import java.util.*
import kotlin.collections.ArrayList

open class NBTList<Tag: MutableNBT<out Any>>(override val subtagType: NBTType): ImmutableNBTList<Tag>(), MutableNBT<MutableList<Tag>> {

    override val type = NBTType.TAG_List

    private var internalValue = mutableListOf<Tag>()

    /**
     * Reads the contents of the list, except for the subtag ID, which is supposed to be already read
     * @see MutableNBT.readContents
     */
    override fun readContents(source: DataInputStream) {
        clear()
        val length = source.readInt()
        for (i in 0 until length) {
            @Suppress("UNCHECKED_CAST") // Due to the specs and the way Hephaistos is made, this cast should not fail.
            val tag = source.readTag(subtagType) as Tag
            getValue().add(tag)
        }
    }

    /**
     * Removes all tags from this list
     */
    fun clear() {
        getValue().clear()
    }

    /**
     * Changes the tag at the given index
     */
    operator fun set(index: Int, tag: Tag) {
        getValue()[index] = tag
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
        getValue() += tag
    }

    internal fun unsafeAdd(tag: MutableNBT<out Any>) {
        if(tag.type != subtagType)
            throw NBTException("Element to add is not of type ${subtagType.name} but of type ${NBTType.name(tag.ID)}")
        getValue() += (tag as? Tag) ?: throw NBTException("Could not cast $tag to supported-by-this-list type")
    }

    /**
     * From ArrayList#removeAt:
     * > Removes the element at the specified position in this list. Shifts any subsequent elements to the left (subtracts one from their indices).
     */
    fun removeAt(index: Int): NBTList<Tag>  {
        getValue().removeAt(index)
        return this
    }

    /**
     * From ArrayList#remove:
     * > Removes the first occurrence of the specified element from this list, if it is present. If the list does not contain the element, it is unchanged. More formally, removes the element with the lowest index i such that Objects.equals(o, get(i)) (if such an element exists). Returns true if this list contained the specified element (or equivalently, if this list changed as a result of the call).
     */
    fun remove(tag: Tag): NBTList<Tag> {
        getValue().remove(tag)
        return this
    }

    /**
     * Casts this list to another list type. Can throw a ClassCastException, so be careful
     */
    @Suppress("UNCHECKED_CAST") // if that throws, it is the user's fault
    override fun <T: MutableNBT<out Any>> asListOf(): NBTList<T> = this as NBTList<T>

    companion object {
        @Throws(IOException::class)
        fun readFrom(source: DataInputStream): NBTList<MutableNBT<out Any>> {
            val subtagType = source.readByte().toInt()
            val list = NBTList<MutableNBT<out Any>>(NBTType.fromID(subtagType))
            list.readContents(source)
            return list
        }
    }

    override fun deepClone(): NBTList<Tag> {
        val clone = NBTList<Tag>(subtagType)
        for(subtag in getValue()) {
            clone += subtag.deepClone() as Tag
        }
        return clone
    }

    override fun asMutable(): NBTList<Tag> = this

    override fun getValue() = internalValue

    override fun setValue(v: MutableList<Tag>) {
        internalValue = v
    }
}