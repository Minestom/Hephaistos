package org.jglrxavpok.hephaistos.json

import com.google.gson.Gson
import com.google.gson.JsonArray
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

    inline fun <reified Tag: NBT> read(): Tag {
        return read(NBTTypes.getID<Tag>()) as Tag
    }

    fun <Tag: NBT> read(nbtClass: Class<Tag>): Tag = read(NBTTypes.getID(nbtClass)) as Tag

    fun guessType(element: JsonElement): Int {
        return when {
            element.isJsonObject -> NBTTypes.TAG_Compound
            element.isJsonNull -> NBTTypes.TAG_String
            element.isJsonPrimitive -> {
                val primitive = element.asJsonPrimitive
                when {
                    primitive.isBoolean -> NBTTypes.TAG_Byte
                    primitive.isString -> NBTTypes.TAG_String
                    primitive.isNumber ->
                        if(primitive.asLong.toDouble() == primitive.asDouble) {
                            if('.' in primitive.asString) {
                                NBTTypes.TAG_Double
                            } else {
                                NBTTypes.TAG_Long
                            }
                        } else {
                            NBTTypes.TAG_Double
                        }
                    else -> error("Primitive that is neither a boolean, a string, nor a number?")
                }
            }

            element.isJsonArray -> {
                val array = element.asJsonArray
                if(array.size() == 0) {
                    NBTTypes.TAG_List
                } else {
                    val firstElement = element.asJsonArray.get(0)
                    val guessedType = guessType(firstElement)
                    when(guessedType) {
                        NBTTypes.TAG_Long -> NBTTypes.TAG_Long_Array
                        NBTTypes.TAG_Byte -> NBTTypes.TAG_Byte_Array
                        NBTTypes.TAG_Int -> NBTTypes.TAG_Int_Array

                        else -> NBTTypes.TAG_List
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
    private fun <Tag: NBT> parse(nbtType: Int, element: JsonElement): Tag {
        try {
            val result = when (nbtType) {
                NBTTypes.TAG_End -> NBTEnd()
                NBTTypes.TAG_Byte -> NBTByte(element.asByte)
                NBTTypes.TAG_Short -> NBTShort(element.asShort)
                NBTTypes.TAG_Int -> NBTInt(element.asInt)
                NBTTypes.TAG_Long -> NBTLong(element.asLong)
                NBTTypes.TAG_Float -> NBTFloat(element.asFloat)
                NBTTypes.TAG_Double -> NBTDouble(element.asDouble)
                NBTTypes.TAG_Byte_Array -> NBTByteArray(element.asJsonArray.map { it.asByte }.toByteArray())
                NBTTypes.TAG_String -> if(element.isJsonNull) NBTString("") else NBTString(element.asString)

                NBTTypes.TAG_Compound -> toCompound(element.asJsonObject)

                NBTTypes.TAG_Int_Array -> NBTIntArray(element.asJsonArray.map { it.asInt }.toIntArray())
                NBTTypes.TAG_Long_Array -> NBTLongArray(element.asJsonArray.map { it.asLong }.toLongArray())

                NBTTypes.TAG_List -> {
                    if (!element.isJsonArray)
                        throw NBTException("Expected a list, but was: $element")
                    val elements = element.asJsonArray

                    if (elements.size() == 0) { // guess strings
                        NBTList<NBT>(NBTTypes.TAG_String)
                    } else {
                        val firstElement = parse<NBT>(guessType(elements[0]), elements[0])
                        val list = NBTList<NBT>(firstElement.ID)
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

    fun read(nbtType: Int): NBT {
        return parse(nbtType, GsonInstance.fromJson(reader, JsonElement::class.java))
    }

    fun readWithGuess(): NBT {
        val element = GsonInstance.fromJson(reader, JsonElement::class.java)
        return parse(guessType(element), element)
    }

    private fun toCompound(jsonObject: JsonObject): NBTCompound {
        val compound = NBTCompound()
        for((key, value) in jsonObject.entrySet()) {
            val nbt = parse<NBT>(guessType(value), value)
            compound[key] = nbt
        }
        return compound
    }

    override fun close() {
        reader.close()
    }
}