package org.jglrxavpok.hephaistos.nbt

import java.io.*
import java.nio.file.Files
import java.nio.file.Path

/**
 * Reads NBT Data from a given input stream.
 * Once the input stream is passed to a NBTReader, use NBTReader#close to close the stream.
 */
class NBTReader @JvmOverloads constructor(source: InputStream, compressedMode: CompressedMode = CompressedMode.NONE): AutoCloseable, Closeable {

    private val reader = DataInputStream(
        compressedMode.generateInputStream(source)
    )

    /**
     * Constructs a NBTReader from a file (convenience method, equivalent to `NBTReader(BufferedInputStream(FileInputStream(file)))`)
     */
    @Throws(IOException::class)
    @JvmOverloads constructor(file: File, compressed: CompressedMode = CompressedMode.NONE): this(BufferedInputStream(FileInputStream(file)), compressed)

    /**
     * Constructs a NBTWriter from a path (convenience method, equivalent to `NBTWriter(BufferedOutputStream(Files.newOutputStream(path)))`)
     */
    @Throws(IOException::class)
    @JvmOverloads constructor(path: Path, compressed: CompressedMode = CompressedMode.NONE): this(BufferedInputStream(Files.newInputStream(path)), compressed)

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

    companion object {
        /**
         * Creates an [NBTReader] from a [ByteArray]
         *
         * @param array The byte array to create it from.
         *
         * @return The created [NBTReader] from the [ByteArray].
         */
        @JvmStatic
        @JvmOverloads
        fun fromArray(array: ByteArray, compressedMode: CompressedMode = CompressedMode.NONE) = NBTReader(ByteArrayInputStream(array), compressedMode);
    }
}