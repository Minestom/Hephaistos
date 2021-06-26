package org.jglrxavpok.hephaistos.nbt

class DSLCompound(): NBTCompound() {
    infix fun String.to(value: Int) = set(this, value)
    infix fun String.to(value: Float) = set(this, value)
    infix fun String.to(value: Long) = set(this, value)
    infix fun String.to(value: Double) = set(this, value)
    infix fun String.to(value: Byte) = set(this, value)
    infix fun String.to(value: Short) = set(this, value)
    infix fun String.to(value: ByteArray) = set(this, value)
    infix fun String.to(value: LongArray) = set(this, value)
    infix fun String.to(value: IntArray) = set(this, value)
    infix fun String.to(value: String) = set(this, value)
    infix fun String.to(value: MutableNBT<out Any>) = set(this, value)
}

inline fun compound(builder: DSLCompound.() -> Unit) = DSLCompound().apply(builder)

operator fun NBTCompound.set(key: String, value: Int) = setInt(key, value)
operator fun NBTCompound.set(key: String, value: Float) = setFloat(key, value)
operator fun NBTCompound.set(key: String, value: Long) = setLong(key, value)
operator fun NBTCompound.set(key: String, value: Double) = setDouble(key, value)
operator fun NBTCompound.set(key: String, value: Byte) = setByte(key, value)
operator fun NBTCompound.set(key: String, value: Short) = setShort(key, value)
operator fun NBTCompound.set(key: String, value: ByteArray) = setByteArray(key, value)
operator fun NBTCompound.set(key: String, value: LongArray) = setLongArray(key, value)
operator fun NBTCompound.set(key: String, value: IntArray) = setIntArray(key, value)
operator fun NBTCompound.set(key: String, value: String) = setString(key, value)

class DSLList<Tag: MutableNBT<out Any>>(type: NBTType): NBTList<Tag>(type) {
    operator fun Int.not() = unsafeAdd(NBTInt(this))
    operator fun Float.not() = unsafeAdd(NBTFloat(this))
    operator fun Long.not() = unsafeAdd(NBTLong(this))
    operator fun Double.not() = unsafeAdd(NBTDouble(this))
    operator fun Byte.not() = unsafeAdd(NBTByte(this))
    operator fun Short.not() = unsafeAdd(NBTShort(this))
    operator fun ByteArray.not() = unsafeAdd(NBTByteArray(this))
    operator fun LongArray.not() = unsafeAdd(NBTLongArray(this))
    operator fun IntArray.not() = unsafeAdd(NBTIntArray(this))
    operator fun String.not() = unsafeAdd(NBTString(this))
    operator fun MutableNBT<*>.not() = unsafeAdd(this)
}

inline fun <reified Tag: MutableNBT<out Any>> list(builder: DSLList<Tag>.() -> Unit) =
    DSLList<Tag>(NBTType.getID<Tag>()).apply(builder)

