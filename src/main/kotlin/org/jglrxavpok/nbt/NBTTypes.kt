package org.jglrxavpok.nbt

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

    internal inline fun <reified Tag: NBT> getID() = when(Tag::class) {
        NBTEnd::class -> TAG_End
        NBTByte::class -> TAG_Byte
        NBTShort::class -> TAG_Short
        NBTInt::class -> TAG_Int
        NBTLong::class -> TAG_Long
        NBTFloat::class -> TAG_Float
        NBTDouble::class -> TAG_Double
        NBTByteArray::class -> TAG_Byte_Array
        NBTString::class -> TAG_String
        NBTCompound::class -> TAG_Compound
        NBTIntArray::class -> TAG_Int_Array
        NBTLongArray::class -> TAG_Long_Array
        NBTList::class -> TAG_List

        else -> throw IllegalArgumentException("Unrecognized NBT class: ${Tag::class.qualifiedName}")
    }
}
