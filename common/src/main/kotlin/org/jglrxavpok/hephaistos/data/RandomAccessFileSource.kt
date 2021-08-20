package org.jglrxavpok.hephaistos.data

import java.io.Closeable
import java.io.DataInput
import java.io.DataOutput
import java.io.RandomAccessFile
import java.nio.ByteBuffer

class RandomAccessFileSource(val file: RandomAccessFile): DataSource, DataInput by file, DataOutput by file, Closeable by file {
    override fun seek(position: Long) {
        file.seek(position)
    }

    override fun length(): Long {
        return file.length()
    }

    override fun setLength(newLength: Long) {
        file.setLength(newLength)
    }

    override fun writeByte(pos: Long, b: Byte) {
        file.channel.write(ByteBuffer.allocate(1).put(0, b), pos)
    }

    override fun writeBytes(pos: Long, bytes: ByteArray) {
        file.channel.write(ByteBuffer.wrap(bytes), pos)
    }

    override fun writeInt(pos: Long, int: Int) {
        file.channel.write(ByteBuffer.allocate(4).putInt(0, int), pos)
    }

    override fun readBytes(pos: Long, destination: ByteArray) {
        val buf = ByteBuffer.allocateDirect(destination.size)
        file.channel.read(buf, pos)
        for (i in destination.indices) destination[i] = buf[i]
    }

    override fun readByte(pos: Long): Byte {
        val buf = ByteBuffer.allocate(1)
        file.channel.read(buf, pos)
        return buf[0]
    }

    override fun readInt(pos: Long): Int {
        val buf = ByteBuffer.allocate(4)
        file.channel.read(buf, pos)
        return buf.getInt(0)
    }
}
