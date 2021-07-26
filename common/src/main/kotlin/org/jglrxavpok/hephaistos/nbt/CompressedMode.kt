package org.jglrxavpok.hephaistos.nbt

import java.io.InputStream
import java.io.OutputStream
import java.util.zip.*

open class CompressedMode {

    open fun generateInputStream(originalInputStream: InputStream) = originalInputStream
    open fun generateOutputStream(originalOutputStream: OutputStream) = originalOutputStream

    companion object {
        /**
         * No compresson. Data in and out will remain the same.
         */
        @JvmField
        val NONE = object : CompressedMode() { }

        @JvmField
        val GZIP = ParameterizedGzip()

        @JvmField
        val ZLIB = ParameterizedZlib()
    }

    class ParameterizedGzip(val bufferSize: Int = 512) : CompressedMode() {
        override fun generateInputStream(originalInputStream: InputStream) = GZIPInputStream(originalInputStream, bufferSize)
        override fun generateOutputStream(originalOutputStream: OutputStream) = GZIPOutputStream(originalOutputStream, bufferSize)
    }

    class ParameterizedZlib(val inflater: Inflater = Inflater(), val deflater: Deflater = Deflater(), val bufferSize: Int = 512) : CompressedMode() {
        override fun generateInputStream(originalInputStream: InputStream) = InflaterInputStream(originalInputStream, inflater, bufferSize)
        override fun generateOutputStream(originalOutputStream: OutputStream) = DeflaterOutputStream(originalOutputStream, deflater, bufferSize)
    }

}