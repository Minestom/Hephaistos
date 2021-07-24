package org.jglrxavpok.hephaistos.nbt

import java.io.InputStream
import java.io.OutputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.InflaterInputStream

open class CompressedMode {

    open fun generateInputStream(originalInputStream: InputStream) = originalInputStream
    open fun generateOutputStream(originalOutputStream: OutputStream) = originalOutputStream

    companion object {
        @JvmField
        val NONE = object : CompressedMode() { }

        @JvmField
        val GZIP = object : CompressedMode() {
            override fun generateInputStream(originalInputStream: InputStream) = GZIPInputStream(originalInputStream)
            override fun generateOutputStream(originalOutputStream: OutputStream) = GZIPOutputStream(originalOutputStream)
        }

        @JvmField
        val ZLIB = object : CompressedMode() {
            override fun generateInputStream(originalInputStream: InputStream) = InflaterInputStream(originalInputStream)
            override fun generateOutputStream(originalOutputStream: OutputStream) = DeflaterOutputStream(originalOutputStream)
        }
    }

}