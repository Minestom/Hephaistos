package org.jglrxavpok.hephaistos.nbt

import kotlin.reflect.KClass

open class NBTType<T : NBT>(
    val nbtClass: KClass<T>,
    val readerCompanion: NBTReaderCompanion<T>,
    val readableName: String,
    val ordinal: Int
) {

    companion object {

        @JvmField
        val TAG_End = object
            : NBTType<NBTEnd>(NBTEnd::class, NBTEnd, "TAG_End", 0) {}

        @JvmField
        val TAG_Byte = object
            : NBTType<NBTByte>(NBTByte::class, NBTByte, "TAG_Byte", 1) {}

        @JvmField
        val TAG_Short = object
            : NBTType<NBTShort>(NBTShort::class, NBTShort, "TAG_Short", 2) {}

        @JvmField
        val TAG_Int = object
            : NBTType<NBTInt>(NBTInt::class, NBTInt, "TAG_Int", 3) {}

        @JvmField
        val TAG_Long = object
            : NBTType<NBTLong>(NBTLong::class, NBTLong, "TAG_Long", 4) {}

        @JvmField
        val TAG_Float = object
            : NBTType<NBTFloat>(NBTFloat::class, NBTFloat, "TAG_Float", 5) {}

        @JvmField
        val TAG_Double = object
            : NBTType<NBTDouble>(NBTDouble::class, NBTDouble, "TAG_Double", 6) {}

        @JvmField
        val TAG_Byte_Array = object
            : NBTType<NBTByteArray>(NBTByteArray::class, NBTByteArray, "TAG_Byte_Array", 7) {}

        @JvmField
        val TAG_String = object
            : NBTType<NBTString>(NBTString::class, NBTString, "TAG_String", 8) {}

        @JvmField
        val TAG_List = object
            : NBTType<NBTList<*>>(NBTList::class, NBTList as NBTReaderCompanion<NBTList<*>>, "TAG_List", 9) {}

        @JvmField
        val TAG_Compound = object
            : NBTType<NBTCompound>(NBTCompound::class, NBTCompound, "Tag_Compound", 10) {}

        @JvmField
        val TAG_Int_Array = object
            : NBTType<NBTIntArray>(NBTIntArray::class, NBTIntArray, "TAG_Int_Array", 11) {}

        @JvmField
        val TAG_Long_Array = object
            : NBTType<NBTLongArray>(NBTLongArray::class, NBTLongArray, "TAG_Long_Array", 12) {}

        val values = arrayOf(
            TAG_End,
            TAG_Byte,
            TAG_Short,
            TAG_Int,
            TAG_Long,
            TAG_Float,
            TAG_Double,
            TAG_Byte_Array,
            TAG_String,
            TAG_List,
            TAG_Compound,
            TAG_Int_Array,
            TAG_Long_Array
        )
        
        @JvmStatic
        fun byIndex(index: Int): NBTType<*> {

            require(index >= 0) { "The index must be greater than 0!" }
            require(index < values.size) { "The index must be smaller than ${values.size}! "}

            return values[index]
        }

        inline fun <reified T : NBT> byClass(): NBTType<T>? = byClass(T::class)
        fun <T : NBT> byClass(clazz: KClass<T>): NBTType<T>? = values.firstOrNull { it.nbtClass == clazz } as? NBTType<T>

        @JvmStatic
        fun <T : NBT> byClass(clazz: Class<T>): NBTType<T>? = byClass(clazz.kotlin)
    }
}