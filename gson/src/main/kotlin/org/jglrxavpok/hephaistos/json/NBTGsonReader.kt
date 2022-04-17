package org.jglrxavpok.hephaistos.json

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.jglrxavpok.hephaistos.nbt.*
import java.io.Closeable
import java.io.Reader

class NBTGsonReader(private val reader: Reader): AutoCloseable, Closeable {

    private companion object {
        val GsonInstance = Gson()

        /**
         * If nbtType is TAG_List, this method will attempt to guess the type of the list elements.
         * The guess is done by getting the first element and guessing its type via #guessType.
         * If the list is empty, this method will always guess that the subtype is TAG_String
         */
        fun <Tag: NBT> parse(nbtType: NBTType<out NBT>, element: JsonElement): Tag {
            try {
                val result = when (nbtType) {
                    NBTType.TAG_End -> NBTEnd
                    NBTType.TAG_Byte -> NBT.Byte(element.asByte)
                    NBTType.TAG_Short -> NBT.Short(element.asShort)
                    NBTType.TAG_Int -> NBT.Int(element.asInt)
                    NBTType.TAG_Long -> NBT.Long(element.asLong)
                    NBTType.TAG_Float -> NBT.Float(element.asFloat)
                    NBTType.TAG_Double -> NBT.Double(element.asDouble)
                    NBTType.TAG_Byte_Array -> NBT.ByteArray(*element.asJsonArray.map { it.asByte }.toByteArray())
                    NBTType.TAG_String -> if(element.isJsonNull) NBT.String("") else NBT.String(element.asString)

                    NBTType.TAG_Compound -> toCompound(element.asJsonObject)

                    NBTType.TAG_Int_Array -> NBT.IntArray(*element.asJsonArray.map { it.asInt }.toIntArray())
                    NBTType.TAG_Long_Array -> NBT.LongArray(*element.asJsonArray.map { it.asLong }.toLongArray())

                    NBTType.TAG_List -> {
                        if (!element.isJsonArray)
                            throw NBTException("Expected a list, but was: $element")
                        val elements = element.asJsonArray

                        if (elements.size() == 0) { // guess strings
                            NBT.List(NBTType.TAG_String)
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

        private fun toCompound(jsonObject: JsonObject) = NBT.Kompound {
            for((key, value) in jsonObject.entrySet()) {
                val nbt = parse<NBT>(guessType(value), value)
                this[key] = nbt
            }
        }

        private fun guessType(element: JsonElement): NBTType<out NBT> {
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
    }

    inline fun <reified Tag: NBT> read(): Tag {
        return read(NBTType.byClass<Tag>() ?: error("Invalid NBTType: ${Tag::class.qualifiedName}")) as Tag
    }

    fun <Tag: NBT> read(nbtClass: Class<Tag>): Tag = read(NBTType.byClass(nbtClass) ?: error("Invalid NBTType: ${nbtClass.canonicalName}")) as Tag


    fun read(nbtType: NBTType<out NBT>): NBT {
        return parse(nbtType, GsonInstance.fromJson(reader, JsonElement::class.java))
    }

    fun readWithGuess(): NBT {
        val element = GsonInstance.fromJson(reader, JsonElement::class.java)
        return parse(guessType(element), element)
    }

    override fun close() {
        reader.close()
    }
}