package org.jglrxavpok.mca

import java.lang.Exception

class AnvilException(message: String, cause: Throwable?): Exception(message, cause) {

    constructor(message: String): this(message, null)
}