package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import java.io.DataOutputStream
import java.io.IOException

/**
 * Most basic representation of a NBTag
 */
sealed interface NBT {

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

    companion object {

        @JvmStatic
        fun Byte(value: Byte) = NBTByte(value)

        @JvmStatic
        fun Byte(value: Int) = NBTByte(value.toByte())

        @JvmStatic
        fun ByteArray(vararg value: Byte) = NBTByteArray(*value)

        @JvmStatic
        fun ByteArray(vararg value: Int) = NBTByteArray(*value.map { it.toByte() }.toByteArray())

        @JvmStatic
        fun ByteArray(array: ImmutableByteArray) = NBTByteArray(array)

        @JvmStatic
        fun Compound(lambda: CompoundBuilder) = NBTCompound.compound(lambda)

        inline fun Kompound(crossinline lambda: CompoundMap.() -> Unit) = NBTCompound.kompound(lambda)

        @JvmStatic
        fun Double(value: Double) = NBTDouble(value)

        @JvmStatic
        fun Float(value: Float) = NBTFloat(value)

        @JvmStatic
        fun Short(value: Short) = NBTShort(value)

        @JvmStatic
        fun Short(value: Int) = NBTShort(value.toShort())

        @JvmStatic
        fun Int(value: Int) = NBTInt(value)

        @JvmStatic
        fun IntArray(vararg value: Int) = NBTIntArray(*value)

        @JvmStatic
        fun IntArray(array: ImmutableIntArray) = NBTIntArray(array)

        @JvmStatic
        fun Long(value: Long) = NBTLong(value)

        @JvmStatic
        fun LongArray(vararg value: Long) = NBTLongArray(*value)

        @JvmStatic
        fun LongArray(array: ImmutableLongArray) = NBTLongArray(array)

        @JvmStatic
        fun String(value: String) = NBTString(value)
    }
}