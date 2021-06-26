package org.jglrxavpok.hephaistos.json

import com.google.gson.*
import org.jglrxavpok.hephaistos.nbt.*

class NBTGsonWriter(val gson: Gson = GsonInstance) {

    companion object {
        val GsonInstance = Gson()
    }

    fun write(element: ImmutableNBT<out Any>): JsonElement {
        return when(element) {
            is ImmutableNBTByte -> JsonPrimitive(element.getNumberValue())
            is ImmutableNBTDouble -> JsonPrimitive(element.getNumberValue())
            is ImmutableNBTFloat -> JsonPrimitive(element.getNumberValue())
            is ImmutableNBTInt -> JsonPrimitive(element.getNumberValue())
            is ImmutableNBTLong -> JsonPrimitive(element.getNumberValue())
            is ImmutableNBTShort -> JsonPrimitive(element.getNumberValue())

            is ImmutableNBTString -> JsonPrimitive(element.getValue())
            is ImmutableNBTByteArray -> JsonArray().apply { element.getValue().forEach { add(it) } }
            is ImmutableNBTLongArray -> JsonArray().apply { element.getValue().forEach { add(it) } }
            is ImmutableNBTIntArray -> JsonArray().apply { element.getValue().forEach { add(it) } }
            is ImmutableNBTList<out MutableNBT<out Any>> -> JsonArray().apply { element.forEach { add(write(it)) } }
            is ImmutableNBTCompound -> JsonObject().apply {
                for((name, value) in element) {
                    add(name, write(value))
                }
            }

            is NBTEnd -> throw NBTException("Cannot convert TAG_End to a JsonElement")
            else -> error("Cannot serialize type ${element.ID}, not supported by this writer")
        }
    }

}