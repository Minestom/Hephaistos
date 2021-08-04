package org.jglrxavpok.hephaistos.nbt

import java.io.InputStream
import java.io.OutputStream
import java.util.zip.*

/**
 * Processes an [InputStream] or an [OutputStream] into a compressed stream.
 */
open class CompressedProcesser<I : InputStream, O : OutputStream> {

    /**
     * Generates a compressed input stream from the [originalInputStream]
     *
     * @param originalInputStream The input stream to generate the new input stream from.
     *
     * @return A new compressed input stream.
     */
    open fun generateInputStream(originalInputStream: InputStream) = originalInputStream

    /**
     * Generates a compressed output stream from the [originalOutputStream]
     *
     * @param originalOutputStream The output stream to generate the new output stream from.
     *
     * @return A new compressed output stream.
     */
    open fun generateOutputStream(originalOutputStream: OutputStream) = originalOutputStream

    companion object {
        /**
         * No compresson. Data in and out will remain the same.
         */
        @JvmField
        val NONE = object : CompressedProcesser<InputStream, OutputStream>() { }

        /** GZIP compression. Buffer size is 512 */
        @JvmField
        val GZIP = ParameterizedGzip()

        /**
         * ZLIP compression.
         *
         * Buffer size is 512 and the inflater/deflater
         * used is [ParameterizedZlib.DEFAULT_INFLATER] / [ParameterizedZlib.DEFAULT_DEFLATER]
         */
        @JvmField
        val ZLIB = ParameterizedZlib()
    }

    class ParameterizedGzip(val bufferSize: Int = 512) : CompressedProcesser<GZIPInputStream, GZIPOutputStream>() {
        override fun generateInputStream(originalInputStream: InputStream) = GZIPInputStream(originalInputStream, bufferSize)
        override fun generateOutputStream(originalOutputStream: OutputStream) = GZIPOutputStream(originalOutputStream, bufferSize)
    }

    class ParameterizedZlib(
        val inflater: Inflater = DEFAULT_INFLATER,
        val deflater: Deflater = DEFAULT_DEFLATER,
        val bufferSize: Int = 512
    ) : CompressedProcesser<InflaterInputStream, InflaterOutputStream>() {

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
            /** Represents the default inflater. Using `new Inflater()` instead of `DEFAULT_INFLATER` causes issues. */
            val DEFAULT_INFLATER = Inflater()

            /** Represents the default deflater. Using `new Deflater()` instead of `DEFAULT_DEFLATER` causes issues. */
            val DEFAULT_DEFLATER = Deflater()
        }
    }

}