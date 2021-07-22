package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * Most basic representation of a NBTag
 */
interface NBT {

    /**
     * ID of this tag type
     */
    val ID: Int

    /**
     * Writes the contents of the tag to the given destination. The tag ID is supposed to be already written
     * @throws IOException if an error occurred during writing
     */
    @Throws(IOException::class)
    fun writeContents(destination: DataOutputStream)

    /**
     * Produces the stringified version of this NBT (or SNBT version). Is empty for TAG_End
     */
    fun toSNBT(): String

    // TODO @Throws(NBTException::class)
    //fun parseSNBT(snbt: String)

    /**
     * Produces a human-readable version of this tag. Must be the same as `toSNBT()`, except for TAG_End which returns "<TAG_End>"
     */
    override fun toString(): String

    /**
     * Creates a deep clone of this NBT element.
     * For list, this clones the list elements too via deepClone.
     * Same goes for NBTCompound entries.
     *
     * The only exception is NBTString: the String value is not copied into a new String object, as there are immutable in Java
     */
    fun deepClone(): NBT
}