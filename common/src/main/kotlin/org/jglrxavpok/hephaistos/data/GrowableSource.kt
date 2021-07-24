package org.jglrxavpok.hephaistos.data

import java.io.EOFException
import java.nio.ByteBuffer

class GrowableSource: DataSource {

    private var cursor: Long = 0
    private var size: Long = 0
    private var internalBuffer = ByteArray(0)

    override fun seek(position: Long) {
        cursor = position
    }

    override fun length(): Long {
        return size
    }

    private fun realloc(length: Long) {
        val oldBuffer = internalBuffer
        internalBuffer = ByteArray(length.toInt())
        val l = minOf(size, length)
        oldBuffer.copyInto(internalBuffer, 0, 0, l.toInt())
        size = length
    }

    private fun resizeIfNeeded(targetCursor: Long) {
        if(targetCursor > size) {
            setLength(targetCursor)
        }
    }

    override fun setLength(newLength: Long) {
        realloc(newLength)
    }

    override fun writeByte(pos: Long, b: Byte) {
        internalBuffer[pos.toInt()] = b
    }

    override fun writeByte(v: Int) {
        resizeIfNeeded(cursor+1)
        internalBuffer[(cursor++).toInt()] = v.toByte()
    }

    override fun writeBytes(pos: Long, bytes: ByteArray) {
        resizeIfNeeded(pos+bytes.size)
        bytes.copyInto(internalBuffer, pos.toInt(), 0, bytes.size)
    }

    override fun write(b: ByteArray) {
        resizeIfNeeded(cursor + b.size)
        writeBytes(cursor, b)
        cursor += b.size
    }

    override fun writeBytes(s: String) {
        write(s.toByteArray())
    }

    override fun writeInt(pos: Long, int: Int) {
        writeBytes(pos, ByteBuffer.allocate(4).putInt(0, int).array())
    }

    override fun writeInt(v: Int) {
        resizeIfNeeded(cursor+4)
        writeInt(cursor, v)
        cursor += 4
    }

    override fun readBytes(pos: Long, destination: ByteArray) {
        resizeIfNeeded(pos + destination.size)
        internalBuffer.copyInto(destination, 0, pos.toInt(), pos.toInt()+destination.size)
    }

    override fun readByte(pos: Long): Byte {
        return internalBuffer[pos.toInt()]
    }

    override fun readByte(): Byte {
        return internalBuffer[(cursor++).toInt()]
    }

    private fun readByteBuffer(buf: ByteBuffer, pos: Long) {
        if(pos + buf.remaining() > size)
            throw EOFException()
        buf.put(internalBuffer, pos.toInt(), buf.remaining())
    }

    override fun readInt(pos: Long): Int {
        val buf = ByteBuffer.allocate(4)
        readByteBuffer(buf, pos)
        return buf.getInt(0)
    }

    override fun readInt(): Int {
        val buf = ByteBuffer.allocate(4)
        readByteBuffer(buf, cursor)
        cursor += 4
        return buf.getInt(0)
    }

    override fun readFully(b: ByteArray) {
        readFully(b, 0, b.size)
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        internalBuffer.copyInto(b, off, cursor.toInt(), (cursor+len).toInt())
        cursor += len
    }

    override fun skipBytes(n: Int): Int {
        val startCursor = cursor
        cursor += n
        cursor = cursor.coerceAtMost(size)
        return (size - startCursor).toInt()
    }

    override fun readBoolean(): Boolean {
        return readByte() != 0.toByte()
    }

    override fun readUnsignedByte(): Int {
        return readByte().toInt() and 0xFF
    }

    override fun readShort(): Short {
        val buf = ByteBuffer.allocate(2)
        readByteBuffer(buf, cursor)
        cursor += 2
        return buf.getShort(0)
    }

    override fun readUnsignedShort(): Int {
        val buf = ByteBuffer.allocate(2)
        readByteBuffer(buf, cursor)
        cursor += 2
        return buf.getShort(0).toInt() and 0xFFFF
    }

    override fun readChar(): Char {
        val buf = ByteBuffer.allocate(2)
        readByteBuffer(buf, cursor)
        cursor += 2
        return buf.getChar(0)
    }

    override fun readLong(): Long {
        val buf = ByteBuffer.allocate(8)
        readByteBuffer(buf, cursor)
        cursor += 8
        return buf.getLong(0)
    }

    override fun readFloat(): Float {
        val buf = ByteBuffer.allocate(4)
        readByteBuffer(buf, cursor)
        cursor += 4
        return buf.getFloat(0)
    }

    override fun readDouble(): Double {
        val buf = ByteBuffer.allocate(8)
        readByteBuffer(buf, cursor)
        cursor += 8
        return buf.getDouble(0)
    }

    override fun readLine(): String {
        TODO("Not yet implemented")
    }

    override fun readUTF(): String {
        val length = readShort()
        val start = cursor
        cursor += length
        if(cursor > size)
            throw EOFException()
        return String(internalBuffer, start.toInt(), length.toInt())
    }

    override fun write(b: Int) {
        writeByte(b)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        for (i in off until (off+len)) {
            writeByte(b[i].toInt())
        }
    }

    override fun writeBoolean(v: Boolean) {
        write(if(v) 1 else 0)
    }

    private fun writeByteBuffer(byteBuffer: ByteBuffer) {
        for(i in 0 until byteBuffer.remaining())
            writeByte(byteBuffer.get().toInt() and 0xFF)
    }

    override fun writeShort(v: Int) {
        writeByteBuffer(ByteBuffer.allocate(2).putShort(0, v.toShort()))
    }

    override fun writeChar(v: Int) {
        writeShort(v)
    }

    override fun writeLong(v: Long) {
        writeByteBuffer(ByteBuffer.allocate(8).putLong(0, v))
    }

    override fun writeFloat(v: Float) {
        writeByteBuffer(ByteBuffer.allocate(4).putFloat(0, v))
    }

    override fun writeDouble(v: Double) {
        writeByteBuffer(ByteBuffer.allocate(8).putDouble(0, v))
    }

    override fun writeChars(s: String) {
        for(c in s)
            writeChar(c.toInt())
    }

    override fun writeUTF(s: String) {
        writeShort(s.length)
        write(s.toByteArray(Charsets.UTF_8))
    }

    override fun close() {
        internalBuffer = ByteArray(0)
        cursor = 0
        size = 0
    }
} 