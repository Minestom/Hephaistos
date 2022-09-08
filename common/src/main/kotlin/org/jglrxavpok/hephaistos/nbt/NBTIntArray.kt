package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import kotlin.math.min

class NBTIntArray constructor(override val value: ImmutableIntArray) : NBT, Iterable<Int> {

    val size get() = value.size

    override val ID = NBTType.TAG_Int_Array

    constructor(vararg numbers: Int): this(ImmutableIntArray(*numbers))

    override fun writeContents(destination: DataOutputStream) {
        destination.writeInt(size)

        val bufferSize = min(size * 4, 4096)
        val buffer = ByteBuffer.allocate(bufferSize)
        for(i in 0 until size) {
            buffer.putInt(value[i])
            if(buffer.position() >= bufferSize) {
                destination.write(buffer.array(), 0, buffer.position())
                buffer.clear()
            }
        }

        if(buffer.position() > 0) {
            destination.write(buffer.array(), 0, buffer.position())
        }
    }

    operator fun get(index: Int) = value[index]

    override fun toSNBT(): String {
        val list = value.joinToString(",") { "$it" }
        return "[I;$list]"
    }

    override fun toString() = toSNBT()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NBTIntArray

        if (!(value contentEquals other.value)) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun iterator() = value.iterator()

    companion object : NBTReaderCompanion<NBTIntArray> {
        @JvmField
        val EMPTY = NBTIntArray()

        override fun readContents(source: DataInputStream): NBTIntArray {
            val length = source.readInt()
            val inArray = source.readNBytes(length * 4)
            val outArray = IntArray(length)

            ByteBuffer.allocate(inArray.size).put(inArray)
                .also { it.flip() }
                .asIntBuffer().get(outArray)

            val value = ImmutableIntArray(*outArray)
            return NBTIntArray(value)
        }
    }
}
