package org.jglrxavpok.mca

import java.lang.Exception

class AnvilException(message: String, cause: Throwable?): Exception(message, cause) {

    constructor(message: String): this(message, null)

    companion object {
        fun missing(name: String): Nothing = throw AnvilException("Missing field named '$name' (or of wrong type)")
    }
}