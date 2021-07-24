package org.jglrxavpok.hephaistos.json

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTEnd
import org.jglrxavpok.hephaistos.nbt.NBTException
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import java.io.Closeable
import java.io.Reader

class NBTGsonReader internal constructor(private val reader: Reader): AutoCloseable, Closeable {

    private companion object {
        val GsonInstance = Gson()

        @JvmStatic
        fun reader(reader: Reader) = NBTGsonReader(reader)
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
                NBTTypes.TAG_End -> NBTEnd
                NBTTypes.TAG_Byte -> NBT.Byte(element.asByte)
                NBTTypes.TAG_Short -> NBT.Short(element.asShort)
                NBTTypes.TAG_Int -> NBT.Int(element.asInt)
                NBTTypes.TAG_Long -> NBT.Long(element.asLong)
                NBTTypes.TAG_Float -> NBT.Float(element.asFloat)
                NBTTypes.TAG_Double -> NBT.Double(element.asDouble)
                NBTTypes.TAG_Byte_Array -> NBT.ByteArray(*element.asJsonArray.map { it.asByte }.toByteArray())
                NBTTypes.TAG_String -> if(element.isJsonNull) NBT.String("") else NBT.String(element.asString)

                NBTTypes.TAG_Compound -> toCompound(element.asJsonObject)

                NBTTypes.TAG_Int_Array -> NBT.IntArray(*element.asJsonArray.map { it.asInt }.toIntArray())
                NBTTypes.TAG_Long_Array -> NBT.LongArray(*element.asJsonArray.map { it.asLong }.toLongArray())

                NBTTypes.TAG_List -> {
                    if (!element.isJsonArray)
                        throw NBTException("Expected a list, but was: $element")
                    val elements = element.asJsonArray

                    if (elements.size() == 0) { // guess strings
                        NBT.List(NBTTypes.TAG_String)
                    } else {
                        val firstElement = parse<NBT>(guessType(elements[0]), elements[0])
                        val list = NBT.List(firstElement.ID, elements.map { parse(firstElement.ID, it) })
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

    private fun toCompound(jsonObject: JsonObject) = NBT.Kompound {
        for((key, value) in jsonObject.entrySet()) {
            val nbt = parse<NBT>(guessType(value), value)
            this[key] = nbt
        }
    }

    override fun close() {
        reader.close()
    }
}