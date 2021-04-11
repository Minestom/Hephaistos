package org.jglrxavpok.hephaistos.nbt

import java.lang.IllegalArgumentException

enum class NBTType(private val generator: () -> NBT) {
    TAG_End(::NBTEnd),
    TAG_Byte(::NBTByte),
    TAG_Short(::NBTShort),
    TAG_Int(::NBTInt),
    TAG_Long(::NBTLong),
    TAG_Float(::NBTFloat),
    TAG_Double(::NBTDouble),
    TAG_Byte_Array(::NBTByteArray),
    TAG_String(::NBTString),
    TAG_List({ throw UnsupportedOperationException("Lists cannot be created just with a NBT type ID. This is because it needs the element type too.") }),
    TAG_Compound(::NBTCompound),
    TAG_Int_Array(::NBTIntArray),
    TAG_Long_Array(::NBTLongArray);

    fun create() = generator()
    fun asID() = ordinal

    companion object {
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
            in 0 until values().size -> values()[type].name
            else -> throw IllegalArgumentException("$type is not a valid NBT type ID! Must be in 0-12")
        }

        inline fun <reified Tag: NBT> getID() = getID(Tag::class.java)

        fun fromID(id: Int) = when(id) {
            in 0 until values().size -> values()[id]
            else -> throw IllegalArgumentException("$id is not a valid NBT type ID! Must be in 0-12")
        }
    }
}
