package org.jglrxavpok.hephaistos.nbt

import org.jetbrains.annotations.Contract
import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * Most basic representation of a NBTTag
 */
sealed interface NBT {

    /**
     * ID of this tag type
     */
    val ID: NBTType<out NBT>

    /**
     * Writes the contents of the tag to the given destination. The tag ID is supposed to be already written
     * @throws IOException if an error occurred during writing
     */
    @Throws(IOException::class)
    fun writeContents(destination: DataOutputStream)

    @Throws(IOException::class)
    fun toByteArray(): ByteArray = ByteArrayOutputStream().also { writeContents(DataOutputStream(it)) }.toByteArray()

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

    companion object {

        /**
         * Static constant of an empty [NBTCompound].
         * Equivalent to `NBT.Compound()`
         */
        @JvmStatic
        val EMPTY = NBTCompound()

        /**
         * Static constant of a [NBTByte] set to 1
         * Equivalent to `NBT.Byte(1)`
         */
        @JvmStatic
        val TRUE = Byte(1)

        /**
         * Static constant of a [NBTByte] set to 0
         * Equivalent to `NBT.Byte(0)`
         */
        @JvmStatic
        val FALSE = Byte(0)

        /**
         * Convenience method to get an [NBTByte] equivalent to the byte value of the [flag]
         *
         * @param flag The flag check against
         *
         * @return [TRUE] if [flag] is true, [FALSE] if [flag] is false
         */
        @JvmStatic
        @Contract(pure = true)
        fun Boolean(flag: Boolean): NBTByte = if (flag) TRUE else FALSE

        /**
         * Creates an [NBTByte] with the value of [value]
         *
         * @param value The value to pass to [NBTByte]
         *
         * @return An [NBTByte] containing passed [value]
         */
        @JvmStatic
        @Contract(pure = true)
        fun Byte(value: Byte) = NBTByte(value)

        @JvmStatic
        @Contract(pure = true)
        fun Byte(value: Int) = NBTByte(value.toByte())

        @JvmStatic
        @Contract(pure = true)
        fun ByteArray(vararg value: Byte) = NBTByteArray(ImmutableByteArray(*value))

        @JvmStatic
        @Contract(pure = true)
        fun ByteArray(vararg value: Int) = NBTByteArray(ImmutableByteArray(*value.map { it.toByte() }.toByteArray()))

        @JvmStatic
        @Contract(pure = true)
        fun ByteArray(array: ImmutableByteArray) = NBTByteArray(array)

        @JvmStatic
        @Contract(pure = true)
        fun Compound(lambda: CompoundBuilder) = NBTCompound(MutableNBTCompound().also { lambda.run(it) })

        @JvmStatic
        @Contract(pure = true)
        fun Compound(tags: Map<String, NBT> = mapOf()) = NBTCompound(tags)

        @Contract(pure = true)
        inline fun Kompound(crossinline lambda: MutableNBTCompound.() -> Unit) = Compound { lambda(it) }

        /**
         * Creates an [NBTDouble] with the value of [value]
         *
         * @param value The value to pass to [NBTDouble]
         *
         * @return An [NBTDouble] containing passed [value]
         */
        @JvmStatic
        @Contract(pure = true)
        fun Double(value: Double) = NBTDouble(value)

        @JvmStatic
        @Contract(pure = true)
        fun Entry(key: String, value: NBT) = NBTCompound.entry(key, value)

        /**
         * Creates an [NBTFloat] with the value of [value]
         *
         * @param value The value to pass to [NBTFloat]
         *
         * @return An [NBTFloat] containing passed [value]
         */
        @JvmStatic
        @Contract(pure = true)
        fun Float(value: Float) = NBTFloat(value)

        /**
         * Creates an [NBTShort] with the value of [value]
         *
         * @param value The value to pass to [NBTShort]
         *
         * @return An [NBTShort] containing passed [value]
         */
        @JvmStatic
        @Contract(pure = true)
        fun Short(value: Short) = NBTShort(value)

        @JvmStatic
        @Contract(pure = true)
        fun Short(value: Int) = NBTShort(value.toShort())

        /**
         * Creates an [NBTInt] with the value of [value]
         *
         * @param value The value to pass to [NBTInt]
         *
         * @return An [NBTInt] containing passed [value]
         */
        @JvmStatic
        @Contract(pure = true)
        fun Int(value: Int) = NBTInt(value)

        @JvmStatic
        @Contract(pure = true)
        fun IntArray(vararg value: Int) = NBTIntArray(ImmutableIntArray(*value))

        @JvmStatic
        @Contract(pure = true)
        fun IntArray(array: ImmutableIntArray) = NBTIntArray(array)

        @JvmStatic
        @Contract(pure = true)
        fun <Tag : NBT> List(subtagType: NBTType<Tag>, tags: List<Tag> = listOf()) = NBTList(subtagType, tags)

        @JvmStatic
        @Contract(pure = true)
        fun <Tag : NBT> List(subtagType: NBTType<Tag>, vararg tags: Tag) = NBTList(subtagType, tags.toList())

        @JvmStatic
        @Contract(pure = true)
        fun <Tag : NBT> List(subtagType: NBTType<Tag>, length: Int, generator: NBTListGenerator<Tag>) = NBTList(subtagType, List(length) {
            generator.run(it)
        })

        /**
         * Creates an [NBTLong] with the value of [value]
         *
         * @param value The value to pass to [NBTLong]
         *
         * @return An [NBTLong] containing passed [value]
         */
        @JvmStatic
        @Contract(pure = true)
        fun Long(value: Long) = NBTLong(value)

        @JvmStatic
        @Contract(pure = true)
        fun LongArray(vararg value: Long) = NBTLongArray(*value)

        @JvmStatic
        @Contract(pure = true)
        fun LongArray(array: ImmutableLongArray) = NBTLongArray(array)

        /**
         * Creates an [NBTString] with the value of [value]
         *
         * @param value The value to pass to [NBTString]
         *
         * @return An [NBTString] containing passed [value]
         */
        @JvmStatic
        @Contract(pure = true)
        fun String(value: String) = NBTString(value)
    }
}