package org.jglrxavpok.hephaistos.nbt

import java.lang.IllegalArgumentException

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
        TAG_Byte -> NBTByte.Companion
        TAG_Short -> NBTShort.Companion
        TAG_Int -> NBTInt.Companion
        TAG_Long -> NBTLong.Companion
        TAG_Float -> NBTFloat.Companion
        TAG_Double -> NBTDouble.Companion
        TAG_Byte_Array -> NBTByteArray.Companion
        TAG_String -> NBTString.Companion
        TAG_Compound -> NBTCompound.Companion
        TAG_Int_Array -> NBTIntArray.Companion
        TAG_Long_Array -> NBTLongArray.Companion
        TAG_List -> NBTList.Companion

        else -> throw IllegalArgumentException("$id is not a valid NBT type ID! Must be in 0-12")
    }

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
