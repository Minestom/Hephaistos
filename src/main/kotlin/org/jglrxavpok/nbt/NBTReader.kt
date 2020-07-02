package org.jglrxavpok.nbt

import java.io.*
import java.util.zip.GZIPInputStream

/**
 * Reads NBT Data from a given input stream.
 * Once the input stream is passed to a NBTReader, use NBTReader#close to close the stream.
 */
class NBTReader @JvmOverloads constructor(source: InputStream, compressed: Boolean = true): AutoCloseable, Closeable {

    private val reader = DataInputStream(
        if(compressed) GZIPInputStream(source)
        else source
    )

    /**
     * Constructs a NBTReader from a file (convenience method, equivalent to `NBTReader(BufferedInputStream(FileInputStream(file)))`)
     */
    @Throws(IOException::class)
    @JvmOverloads constructor(file: File, compressed: Boolean = true): this(BufferedInputStream(FileInputStream(file)), compressed)

    /**
     * Reads a single named tag from the source. 'first' will hold the name, 'second' the tag
     * @throws IOException if an error occurred during reading
     * @throws NBTException if the file does not follow NBT format
     */
    @Throws(IOException::class, NBTException::class)
    fun readNamed(): Pair<String, NBT> {
        return reader.readFullyFormedTag()
    }

    /**
     * Reads a single tag from the source.
     * @throws IOException if an error occurred during reading
     * @throws NBTException if the file does not follow NBT format
     */
    @Throws(IOException::class, NBTException::class)
    fun read(): NBT {
        return readNamed().second
    }

    /**
     * Reads a single tag from the source.
     * @throws IOException if an error occurred during reading
     * @throws NBTException if the file does not follow NBT format
     */
    @Throws(IOException::class, NBTException::class)
    fun readRaw(id: Int): NBT {
        return reader.readTag(id)
    }

    override fun close() {
        reader.close()
    }
}