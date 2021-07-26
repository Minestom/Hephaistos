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

    class ParameterizedZlib(val inflater: Inflater = DEFAULT_INFLATER, val deflater: Deflater = DEFAULT_DEFLATER, val bufferSize: Int = 512) : CompressedMode() {
        override fun generateInputStream(originalInputStream: InputStream) = if (inflater == DEFAULT_INFLATER) {
            InflaterInputStream(originalInputStream)
        } else {
            InflaterInputStream(originalInputStream, inflater, bufferSize)
        }

        override fun generateOutputStream(originalOutputStream: OutputStream) = if (deflater == DEFAULT_DEFLATER) {
            DeflaterOutputStream(originalOutputStream)
        } else {
            DeflaterOutputStream(originalOutputStream, deflater, bufferSize)
        }

        companion object {
            val DEFAULT_INFLATER = Inflater()
            val DEFAULT_DEFLATER = Deflater()
        }
    }

}