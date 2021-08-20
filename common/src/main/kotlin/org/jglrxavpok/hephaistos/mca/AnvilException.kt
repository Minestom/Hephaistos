package org.jglrxavpok.hephaistos.mca

import java.lang.Exception

/**
 * Exception thrown when:
 * 1. An Anvil file is malformed (missing fields for example)
 * 2. Trying to access data not available in a RegionFile (like chunks that are not inside the file)
 */
class AnvilException(message: String, cause: Throwable?): Exception(message, cause) {

    constructor(message: String): this(message, null)

    companion object {
        fun missing(name: String): Nothing = throw AnvilException("Missing field named '$name' (or of wrong type)")
    }
}