package org.jglrxavpok.hephaistos.nbt

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * Reads a full tag (ID, name and value when applicable) from this input stream
 */
@Throws(IOException::class, NBTException::class)
fun DataInputStream.readFullyFormedTag(): Pair<String, NBT> {
    val id = readByte().toInt()
    if(id == NBTType.TAG_End.ordinal) {
        return "" to NBTEnd
    }
    val name = readUTF()

    return name to readTag(id)
}

/**
 * Reads a tag (value only) from this input stream
 */
@Throws(IOException::class, NBTException::class)
fun DataInputStream.readTag(id: Int): NBT {
    val readerCompanion = NBTType.byIndex(id).readerCompanion
    return readerCompanion.readContents(this)
}

/**
 * Writes a full tag (ID, name and value when applicable) to this output stream
 */
@Throws(IOException::class)
fun DataOutputStream.writeFullyFormedTag(name: String, tag: NBT) {
    writeByte(tag.ID.ordinal)
    writeUTF(name)
    tag.writeContents(this)
}

/**
 * Writes a TAG_End to this output stream
 */
@Throws(IOException::class)
fun DataOutputStream.writeEndTag() {
    writeByte(NBTType.TAG_End.ordinal)
}