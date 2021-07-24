package org.jglrxavpok.hephaistos.data

import java.io.Closeable
import java.io.DataInput
import java.io.DataOutput
import java.io.IOException

interface DataSource: DataInput, DataOutput, Closeable {

    @Throws(IOException::class)
    fun seek(position: Long): Unit

    @Throws(IOException::class)
    fun length(): Long

    @Throws(IOException::class)
    fun setLength(newLength: Long): Unit

    @Throws(IOException::class)
    fun writeByte(pos: Long, b: Byte)

    @Throws(IOException::class)
    fun writeBytes(pos: Long, bytes: ByteArray)

    @Throws(IOException::class)
    fun writeInt(pos: Long, int: Int)

    @Throws(IOException::class)
    fun readBytes(pos: Long, destination: ByteArray)

    @Throws(IOException::class)
    fun readByte(pos: Long): Byte

    @Throws(IOException::class)
    fun readInt(pos: Long): Int

} 