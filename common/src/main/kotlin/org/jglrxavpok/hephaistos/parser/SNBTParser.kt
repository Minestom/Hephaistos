package org.jglrxavpok.hephaistos.parser

import org.antlr.v4.runtime.*
import org.jglrxavpok.hephaistos.antlr.SNBTLexer
import org.jglrxavpok.hephaistos.SNBTParsingVisitor
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTException
import java.io.Reader
import kotlin.jvm.Throws
import org.jglrxavpok.hephaistos.antlr.SNBTParser as ANTLRParser

class SNBTParser(val reader: Reader): BaseErrorListener(), AutoCloseable, Cloneable {

    @Throws(NBTException::class)
    fun parse(): NBT {
        val stream = CharStreams.fromString(reader.readText())
        val lexer = SNBTLexer(stream)
        lexer.removeErrorListeners()
        lexer.addErrorListener(this)

        val tokens = CommonTokenStream(lexer)
        val parser = ANTLRParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(this)

        return parser.snbt().accept(SNBTParsingVisitor)
    }

    override fun close() {
        reader.close()
    }

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        throw NBTException("Failed to parse SNBT: Line $line, column $charPositionInLine $msg", e)
    }
}