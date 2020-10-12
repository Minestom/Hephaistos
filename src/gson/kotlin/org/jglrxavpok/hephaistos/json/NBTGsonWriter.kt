package org.jglrxavpok.hephaistos.json

import com.google.gson.*
import org.jglrxavpok.hephaistos.nbt.*

class NBTGsonWriter(val gson: Gson = GsonInstance) {

    companion object {
        val GsonInstance = Gson()
    }

    fun write(element: NBT): JsonElement {
        return when(element) {
            is NBTNumber<out Number> -> JsonPrimitive(element.value)

            is NBTString -> JsonPrimitive(element.value)
            is NBTByteArray -> JsonArray().apply { element.value.forEach { add(it) } }
            is NBTLongArray -> JsonArray().apply { element.value.forEach { add(it) } }
            is NBTIntArray -> JsonArray().apply { element.value.forEach { add(it) } }
            is NBTList<out NBT> -> JsonArray().apply { element.forEach { add(write(it)) } }
            is NBTCompound -> JsonObject().apply {
                for((name, value) in element) {
                    add(name, write(value))
                }
            }

            is NBTEnd -> throw NBTException("Cannot convert TAG_End to a JsonElement")
            else -> error("Cannot serialize type ${element.ID}, not supported by this writer")
        }
    }

}