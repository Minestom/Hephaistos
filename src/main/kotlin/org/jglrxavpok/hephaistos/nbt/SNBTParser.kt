package org.jglrxavpok.hephaistos.nbt

import org.jglrxavpok.hephaistos.antlr.SNBTLexer
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.jglrxavpok.hephaistos.antlr.SNBTParsingVisitor
import java.io.Reader
import org.jglrxavpok.hephaistos.antlr.SNBTParser as ANTLRParser

class SNBTParser(val reader: Reader): AutoCloseable, Cloneable {

    fun parse(): NBT {
        val stream = CharStreams.fromString(reader.readText())
        val lexer = SNBTLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = ANTLRParser(tokens)
        return parser.snbt().accept(SNBTParsingVisitor)
    }

    override fun close() {
        reader.close()
    }
}