package org.jglrxavpok.hephaistos.nbt

import java.io.*
import java.util.zip.GZIPOutputStream

/**
 * Writes NBT data to a given destination
 * Once the output stream is passed to a NBTWriter, use NBTWriter#close to close the stream.
 */
class NBTWriter @JvmOverloads constructor(destination: OutputStream, compressed: Boolean = true): AutoCloseable, Closeable {

    private val writer = DataOutputStream(
        if(compressed) GZIPOutputStream(destination)
        else destination
    )

    /**
     * Constructs a NBTWriter from a file (convenience method, equivalent to `NBTWriter(BufferedOutputStream(FileOutputStream(file)))`)
     */
    @Throws(IOException::class)
    @JvmOverloads constructor(file: File, compressed: Boolean = true): this(BufferedOutputStream(FileOutputStream(file)), compressed)

    /**
     * Write a tag with a name inside the destination
     */
    @Throws(IOException::class)
    fun writeNamed(name: String, tag: MutableNBT<out Any>) {
        writer.writeFullyFormedTag(name, tag)
    }

    /**
     * Writes the tag contents directly to the destination
     */
    @Throws(IOException::class)
    fun writeRaw(tag: MutableNBT<out Any>) {
        tag.writeContents(writer)
    }

    override fun close() {
        writer.close()
    }
}