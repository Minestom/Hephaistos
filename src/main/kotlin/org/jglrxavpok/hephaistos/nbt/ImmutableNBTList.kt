package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.UnsupportedOperationException
import java.util.*
import kotlin.collections.ArrayList

abstract class ImmutableNBTList<Tag: MutableNBT<out Any>>: Iterable<Tag>, ImmutableNBT<MutableList<Tag>> {

    override val type = NBTType.TAG_List

    val length get()= getValue().size

    abstract val subtagType: NBTType

    /**
     * Writes the contents of the list, WITH for the subtag ID
     * @see MutableNBT.writeContents
     */
    override fun writeContents(destination: DataOutputStream) {
        destination.writeByte(subtagType.asID())
        destination.writeInt(length)
        for(tag in getValue()) {
            tag.writeContents(destination)
        }
    }

    override fun toSNBT(): String {
        return "[${getValue().joinToString(",") { it.toSNBT() }}]"
    }

    override fun toString() = toSNBT()

    /**
     * Returns the tag at the given index
     */
    operator fun get(index: Int) = getValue()[index]

    /**
     * From ArrayList#indexOf:
     * > Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element. More formally, returns the lowest index i such that Objects.equals(o, get(i)), or -1 if there is no such index.
     */
    fun indexOf(tag: Tag): Int {
        return getValue().indexOf(tag)
    }

    /**
     * Casts this list to another list type. Can throw a ClassCastException, so be careful
     */
    @Suppress("UNCHECKED_CAST") // if that throws, it is the user's fault
    open fun <T: MutableNBT<out Any>> asListOf() = this as ImmutableNBTList<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableNBTList<*>) return false

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
        result = 31 * result + Objects.hash(*ArrayList(getValue()).toArray())
        return result
    }

    override fun iterator(): Iterator<Tag> {
        return getValue().iterator()
    }

    override fun immutableView(): ImmutableNBTList<Tag> = this

    abstract override fun deepClone(): ImmutableNBTList<Tag>

    abstract override fun asMutable(): NBTList<Tag>

    override fun mutableCopy(): NBTList<Tag> {
        return deepClone().asMutable()
    }
}