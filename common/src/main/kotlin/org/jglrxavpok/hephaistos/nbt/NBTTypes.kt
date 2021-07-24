package org.jglrxavpok.hephaistos.nbt

import kotlin.reflect.KClass

object NBTTypes {
    const val TAG_End = 0
    const val TAG_Byte = 1
    const val TAG_Short = 2
    const val TAG_Int = 3
    const val TAG_Long = 4
    const val TAG_Float = 5
    const val TAG_Double = 6
    const val TAG_Byte_Array = 7
    const val TAG_String = 8
    const val TAG_List = 9
    const val TAG_Compound = 10
    const val TAG_Int_Array = 11
    const val TAG_Long_Array = 12

    /**
     * Gets the [NBTReaderCompanion] from the tag ID
     * @throws IllegalArgumentException if the id is not a valid NBT type ID, or a list (for lists, use NBTList#read)
     */
    @JvmStatic
    fun readerCompanionFromID(id: Int): NBTReaderCompanion<out NBT> = when(id) {
        TAG_End -> NBTEnd
        TAG_Byte -> NBTByte
        TAG_Short -> NBTShort
        TAG_Int -> NBTInt
        TAG_Long -> NBTLong
        TAG_Float -> NBTFloat
        TAG_Double -> NBTDouble
        TAG_Byte_Array -> NBTByteArray
        TAG_String -> NBTString
        TAG_Compound -> NBTCompound
        TAG_Int_Array -> NBTIntArray
        TAG_Long_Array -> NBTLongArray
        TAG_List -> NBTList

        else -> throw IllegalArgumentException("$id is not a valid NBT type ID! Must be in 0-12")
    }

    @JvmStatic
    fun <T: NBT> getID(nbtClass: Class<T>) = when(nbtClass) {
        NBTEnd::class.java -> TAG_End
        NBTByte::class.java -> TAG_Byte
        NBTShort::class.java -> TAG_Short
        NBTInt::class.java -> TAG_Int
        NBTLong::class.java -> TAG_Long
        NBTFloat::class.java -> TAG_Float
        NBTDouble::class.java -> TAG_Double
        NBTByteArray::class.java -> TAG_Byte_Array
        NBTString::class.java -> TAG_String
        NBTCompound::class.java -> TAG_Compound
        NBTIntArray::class.java -> TAG_Int_Array
        NBTLongArray::class.java -> TAG_Long_Array
        NBTList::class.java -> TAG_List

        else -> throw IllegalArgumentException("Unrecognized NBT class: ${nbtClass.canonicalName}")
    }

    inline fun <reified Tag: NBT> getID() = getID(Tag::class.java)

    @JvmStatic
    fun getClass(id: Int): KClass<out NBT> = when(id) {
        TAG_End -> NBTEnd::class
        TAG_Byte -> NBTByte::class
        TAG_Short -> NBTShort::class
        TAG_Int -> NBTInt::class
        TAG_Long -> NBTLong::class
        TAG_Float -> NBTFloat::class
        TAG_Double -> NBTDouble::class
        TAG_Byte_Array -> NBTByteArray::class
        TAG_String -> NBTString::class
        TAG_Compound -> NBTCompound::class
        TAG_Int_Array -> NBTIntArray::class
        TAG_Long_Array -> NBTLongArray::class
        TAG_List -> NBTList::class

        else -> throw IllegalArgumentException("$id is not a valid NBT type ID! Must be in 0-12")
    }

    @JvmStatic
    fun name(type: Int): String = when(type) {
        TAG_End -> "TAG_End"
        TAG_Byte -> "TAG_Byte"
        TAG_Short -> "TAG_Short"
        TAG_Int -> "TAG_Int"
        TAG_Long -> "TAG_Long"
        TAG_Float -> "TAG_Float"
        TAG_Double -> "TAG_Double"
        TAG_Byte_Array -> "TAG_Byte_Array"
        TAG_String -> "TAG_String"
        TAG_Compound -> "TAG_Compound"
        TAG_Int_Array -> "TAG_Int_Array"
        TAG_Long_Array -> "TAG_Long_Array"
        TAG_List -> "TAG_List"
        else -> throw IllegalArgumentException("$type is not a valid NBT type ID! Must be in 0-12")
    }
}
