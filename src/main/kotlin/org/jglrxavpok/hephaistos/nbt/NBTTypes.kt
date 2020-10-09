package org.jglrxavpok.hephaistos.nbt

import java.lang.IllegalArgumentException

object NBTTypes {
    @JvmField val TAG_End = 0
    @JvmField val TAG_Byte = 1
    @JvmField val TAG_Short= 2
    @JvmField val TAG_Int = 3
    @JvmField val TAG_Long = 4
    @JvmField val TAG_Float = 5
    @JvmField val TAG_Double = 6
    @JvmField val TAG_Byte_Array = 7
    @JvmField val TAG_String = 8
    @JvmField val TAG_List = 9
    @JvmField val TAG_Compound = 10
    @JvmField val TAG_Int_Array = 11
    @JvmField val TAG_Long_Array = 12

    /**
     * Creates a new empty tag based on the given id
     * @throws IllegalArgumentException if the id is not a valid NBT type ID, or a list (for lists, use NBTList#read)
     */
    @JvmStatic
    fun newFromTypeID(id: Int): NBT = when(id) {
        TAG_End -> NBTEnd()
        TAG_Byte -> NBTByte()
        TAG_Short -> NBTShort()
        TAG_Int -> NBTInt()
        TAG_Long -> NBTLong()
        TAG_Float -> NBTFloat()
        TAG_Double -> NBTDouble()
        TAG_Byte_Array -> NBTByteArray()
        TAG_String -> NBTString()
        TAG_Compound -> NBTCompound()
        TAG_Int_Array -> NBTIntArray()
        TAG_Long_Array -> NBTLongArray()

        TAG_List -> throw IllegalArgumentException("")
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

    inline fun <reified Tag: NBT> getID() = getID(Tag::class.java)
}
