package org.jglrxavpok.hephaistos.nbt

import kotlin.reflect.KClass

enum class NBTType(
    val nbtClass: KClass<out NBT>,
    val readerCompanion: NBTReaderCompanion<out NBT>,
    val readableName: String
) {
    TAG_End(NBTEnd::class, NBTEnd, "TAG_End"), // 0
    TAG_Byte(NBTByte::class, NBTByte, "TAG_Byte"), // 1
    TAG_Short(NBTShort::class, NBTShort, "TAG_Short"), // 2
    TAG_Int(NBTInt::class, NBTInt, "TAG_Int"), // 3
    TAG_Long(NBTLong::class, NBTLong, "TAG_Long"), // 4
    TAG_Float(NBTFloat::class, NBTFloat, "TAG_Float"), // 5
    TAG_Double(NBTDouble::class, NBTDouble, "TAG_Double"), // 6
    TAG_Byte_Array(NBTByteArray::class, NBTByteArray, "TAG_Byte_Array"), // 7
    TAG_String(NBTString::class, NBTString, "TAG_String"), // 8
    TAG_List(NBTList::class, NBTList, "TAG_List"), // 9
    TAG_Compound(NBTCompound::class, NBTCompound, "Tag_Compound"), // 10
    TAG_Int_Array(NBTIntArray::class, NBTIntArray, "TAG_Int_Array"), // 11
    TAG_Long_Array(NBTLongArray::class, NBTLongArray, "TAG_Long_Array"); // 12

    companion object {
        @JvmStatic
        fun byIndex(index: Int): NBTType {

            require(index >= 0) { "The index must be greater than 0!" }
            require(index < values().size) { "The index must be smaller than ${values().size}! "}

            return values()[index]
        }

        inline fun <reified T : NBT> byClass(): NBTType? = byClass(T::class)
        fun byClass(clazz: KClass<out NBT>): NBTType? = values().firstOrNull { it.nbtClass == clazz }

        @JvmStatic
        fun byClass(clazz: Class<out NBT>): NBTType? = byClass(clazz.kotlin)
    }
}