package org.jglrxavpok.hephaistos

import org.jglrxavpok.hephaistos.antlr.SNBTBaseVisitor
import org.jglrxavpok.hephaistos.antlr.SNBTParser
import org.jglrxavpok.hephaistos.nbt.*

object SNBTParsingVisitor: SNBTBaseVisitor<NBT>() {

    override fun aggregateResult(aggregate: NBT?, nextResult: NBT?): NBT? {
        return when {
            aggregate == null && nextResult != null -> nextResult
            aggregate != null && nextResult == null -> aggregate
            aggregate == null && nextResult == null -> null
            else -> /*aggregate != null && nextResult != null ->*/ error("Can't merge $aggregate and $nextResult")
        }
    }

    override fun visitCompound(ctx: SNBTParser.CompoundContext): NBT = NBT.Kompound {
        ctx.namedElement().forEach {
            this[(visit(it.name) as NBTString).value] = visit(it.value)
        }
    }


    override fun visitList(ctx: SNBTParser.ListContext): NBT {
        val elements = ctx.element().map { visit(it) }
        val subtagType = if (elements.isEmpty()) NBTTypes.TAG_String else elements[0].ID
        return NBT.List(subtagType, elements)
    }

    override fun visitByteArray(ctx: SNBTParser.ByteArrayContext): NBT {
        val array = ctx.byteNBT().map { (visitByteNBT(it) as NBTByte).value }.toByteArray()
        return NBT.ByteArray(*array)
    }

    override fun visitIntArray(ctx: SNBTParser.IntArrayContext): NBT {
        val array = ctx.intNBT().map { (visitIntNBT(it) as NBTInt).value }.toIntArray()
        return NBT.IntArray(*array)
    }

    override fun visitLongArray(ctx: SNBTParser.LongArrayContext): NBT {
        val array = ctx.longNBT().map { (visitLongNBT(it) as NBTLong).value }.toLongArray()
        return NBT.LongArray(*array)
    }

    override fun visitDoubleNBT(ctx: SNBTParser.DoubleNBTContext): NBT {
        var text = ctx.text
        if (text.endsWith('d') || text.endsWith('D')) {
            text = text.dropLast(1)
        }
        return NBT.Double(text.toDouble())
    }

    override fun visitFloatNBT(ctx: SNBTParser.FloatNBTContext): NBT {
        return NBT.Float(ctx.text.dropLast(1).toFloat())
    }

    override fun visitLongNBT(ctx: SNBTParser.LongNBTContext): NBT {
        val value = ctx.LONG().text.dropLast(1).toLong()
        return NBT.Long(value)
    }

    override fun visitByteNBT(ctx: SNBTParser.ByteNBTContext): NBT {
        ctx.BOOLEAN()?.let {
            val booleanValue = ctx.BOOLEAN().text == "true"
            return NBT.Boolean(booleanValue)
        }
        val value = ctx.BYTE().text.dropLast(1).toByte()
        return NBT.Byte(value)
    }

    override fun visitShortNBT(ctx: SNBTParser.ShortNBTContext): NBT {
        val value = ctx.SHORT().text.dropLast(1).toShort()
        return NBT.Short(value)
    }

    override fun visitStringNBT(ctx: SNBTParser.StringNBTContext): NBT {
        return when {
            ctx.DoubleQuoteText() != null -> NBT.String(ctx.DoubleQuoteText().text.drop(1).dropLast(1))
            ctx.SingleQuoteText() != null -> NBT.String(ctx.SingleQuoteText().text.drop(1).dropLast(1))
            else -> NBT.String(ctx.text)
        }
    }

    override fun visitIntNBT(ctx: SNBTParser.IntNBTContext): NBT {
        return NBT.Int(ctx.INTEGER().text.toInt())
    }

    override fun visitIdentifier(ctx: SNBTParser.IdentifierContext?): NBT? {
        error("Should not access this rule directly")
    }
}