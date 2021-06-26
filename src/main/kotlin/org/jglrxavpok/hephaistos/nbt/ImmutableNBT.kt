package org.jglrxavpok.hephaistos.nbt

import java.io.DataOutputStream
import java.io.IOException

interface ImmutableNBT<ValueType: Any> {

    val type: NBTType
    /**
     * ID of this tag type
     */
    val ID: Int get()= type.ordinal

    /**
     * Writes the contents of the tag to the given destination. The tag ID is supposed to be already written
     * @throws IOException if an error occurred during writing
     */
    @Throws(IOException::class)
    fun writeContents(destination: DataOutputStream)

    fun getValue(): ValueType

    /**
     * Produces the stringified version of this NBT (or SNBT version). Is empty for TAG_End
     */
    fun toSNBT(): String

    /**
     * Produces a human-readable version of this tag. Must be the same as `toSNBT()`, except for TAG_End which returns "<TAG_End>"
     */
    override fun toString(): String

    /**
     * Returns a new mutable version of this NBT
     */
    fun asMutable(): MutableNBT<ValueType>

    fun deepClone(): ImmutableNBT<ValueType>

    /**
     * Return an immutable view of this NBT element (Warning: not a copy!)
     */
    fun immutableView(): ImmutableNBT<ValueType>

    fun mutableCopy(): MutableNBT<ValueType> {
        return deepClone().asMutable()
    }
}