package org.jglrxavpok.hephaistos.nbt

import java.io.*
import java.nio.file.Files
import java.nio.file.Path

/**
 * Writes NBT data to a given destination
 * Once the output stream is passed to a NBTWriter, use NBTWriter#close to close the stream.
 */
class NBTWriter @JvmOverloads constructor(destination: OutputStream, compressedMode: CompressedMode = CompressedMode.NONE): AutoCloseable, Closeable {

    private val writer = DataOutputStream(
        compressedMode.generateOutputStream(destination)
    )

    /**
     * Constructs a NBTWriter from a file (convenience method, equivalent to `NBTWriter(BufferedOutputStream(FileOutputStream(file)))`)
     */
    @Throws(IOException::class)
    @JvmOverloads constructor(file: File, compressedMode: CompressedMode = CompressedMode.NONE): this(BufferedOutputStream(FileOutputStream(file)), compressedMode)

    /**
     * Constructs a NBTWriter from a path (convenience method, equivalent to `NBTWriter(BufferedOutputStream(Files.newOutputStream(path)))`)
     */
    @Throws(IOException::class)
    @JvmOverloads constructor(path: Path, compressedMode: CompressedMode = CompressedMode.NONE): this(BufferedOutputStream(Files.newOutputStream(path)), compressedMode)

    /**
     * Write a tag with a name inside the destination
     */
    @Throws(IOException::class)
    fun writeNamed(name: String, tag: NBT) {
        writer.writeFullyFormedTag(name, tag)
    }

    /**
     * Writes the tag contents directly to the destination
     */
    @Throws(IOException::class)
    fun writeRaw(tag: NBT) {
        tag.writeContents(writer)
    }

    override fun close() {
        writer.close()
    }
}