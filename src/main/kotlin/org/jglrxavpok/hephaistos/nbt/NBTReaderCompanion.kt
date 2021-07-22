package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.IOException

/**
 * Companion object for all final NBT types
 * that allows creation of said [NBT] object from a [DataInputStream].
 */
interface NBTReaderCompanion<T : NBT> {

    /**
     * Reads the contents of the tag from the given source. The tag ID is supposed to be already read.
     *
     * For NBTLists, it assumes the subtag type ID as already been read
     * @throws IOException if an error occurred during reading
     * @throws NBTException if the data stream does not respect NBT specs
     */
    @Throws(IOException::class, NBTException::class)
    fun readContents(source: DataInputStream): T

}