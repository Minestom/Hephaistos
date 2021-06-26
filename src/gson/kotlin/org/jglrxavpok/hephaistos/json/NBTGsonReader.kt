package org.jglrxavpok.hephaistos.json

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.jglrxavpok.hephaistos.nbt.*
import java.io.Closeable
import java.io.Reader
import kotlin.IllegalArgumentException

class NBTGsonReader(private val reader: Reader): AutoCloseable, Closeable {

    private companion object {
        val GsonInstance = Gson()
    }

    inline fun <reified Tag: MutableNBT<out Any>> read(): Tag {
        return read(NBTType.getID<Tag>()) as Tag
    }

    fun <Tag: MutableNBT<out Any>> read(nbtClass: Class<Tag>): Tag = read(NBTType.getID(nbtClass)) as Tag

    fun guessType(element: JsonElement): NBTType {
        return when {
            element.isJsonObject -> NBTType.TAG_Compound
            element.isJsonNull -> NBTType.TAG_String
            element.isJsonPrimitive -> {
                val primitive = element.asJsonPrimitive
                when {
                    primitive.isBoolean -> NBTType.TAG_Byte
                    primitive.isString -> NBTType.TAG_String
                    primitive.isNumber ->
                        if(primitive.asLong.toDouble() == primitive.asDouble) {
                            if('.' in primitive.asString) {
                                NBTType.TAG_Double
                            } else {
                                NBTType.TAG_Long
                            }
                        } else {
                            NBTType.TAG_Double
                        }
                    else -> error("Primitive that is neither a boolean, a string, nor a number?")
                }
            }

            element.isJsonArray -> {
                val array = element.asJsonArray
                if(array.size() == 0) {
                    NBTType.TAG_List
                } else {
                    val firstElement = element.asJsonArray.get(0)
                    val guessedType = guessType(firstElement)
                    when(guessedType) {
                        NBTType.TAG_Long -> NBTType.TAG_Long_Array
                        NBTType.TAG_Byte -> NBTType.TAG_Byte_Array
                        NBTType.TAG_Int -> NBTType.TAG_Int_Array

                        else -> NBTType.TAG_List
                    }
                }
            }

            else -> error("Unknown json element type $element")
        }
    }

    /**
     * If nbtType is TAG_List, this method will attempt to guess the type of the list elements.
     * The guess is done by getting the first element and guessing its type via #guessType.
     * If the list is empty, this method will always guess that the subtype is TAG_String
     */
    private fun <Tag: MutableNBT<out Any>> parse(nbtType: NBTType, element: JsonElement): Tag {
        try {
            val result = when (nbtType) {
                NBTType.TAG_End -> NBTEnd
                NBTType.TAG_Byte -> NBTByte(element.asByte)
                NBTType.TAG_Short -> NBTShort(element.asShort)
                NBTType.TAG_Int -> NBTInt(element.asInt)
                NBTType.TAG_Long -> NBTLong(element.asLong)
                NBTType.TAG_Float -> NBTFloat(element.asFloat)
                NBTType.TAG_Double -> NBTDouble(element.asDouble)
                NBTType.TAG_Byte_Array -> NBTByteArray(element.asJsonArray.map { it.asByte }.toByteArray())
                NBTType.TAG_String -> if(element.isJsonNull) NBTString("") else NBTString(element.asString)

                NBTType.TAG_Compound -> toCompound(element.asJsonObject)

                NBTType.TAG_Int_Array -> NBTIntArray(element.asJsonArray.map { it.asInt }.toIntArray())
                NBTType.TAG_Long_Array -> NBTLongArray(element.asJsonArray.map { it.asLong }.toLongArray())

                NBTType.TAG_List -> {
                    if (!element.isJsonArray)
                        throw NBTException("Expected a list, but was: $element")
                    val elements = element.asJsonArray

                    if (elements.size() == 0) { // guess strings
                        NBTList<MutableNBT<out Any>>(NBTType.TAG_String)
                    } else {
                        val firstElement = parse<MutableNBT<out Any>>(guessType(elements[0]), elements[0])
                        val list = NBTList<MutableNBT<out Any>>(firstElement.type)
                        for (elem in elements) {
                            list += parse(list.subtagType, elem)
                        }
                        list
                    }
                }

                else -> throw IllegalArgumentException("$nbtType is not a valid/supported NBT type")
            }
            return result as Tag
        } catch (e: IllegalStateException) {
            throw NBTException("Failed to load NBT from json", e)
        }
    }

    fun read(nbtType: NBTType): MutableNBT<out Any> {
        return parse(nbtType, GsonInstance.fromJson(reader, JsonElement::class.java))
    }

    fun readWithGuess(): MutableNBT<out Any> {
        val element = GsonInstance.fromJson(reader, JsonElement::class.java)
        return parse(guessType(element), element)
    }

    private fun toCompound(jsonObject: JsonObject): NBTCompound {
        val compound = NBTCompound()
        for((key, value) in jsonObject.entrySet()) {
            val nbt = parse<MutableNBT<out Any>>(guessType(value), value)
            compound[key] = nbt
        }
        return compound
    }

    override fun close() {
        reader.close()
    }
}