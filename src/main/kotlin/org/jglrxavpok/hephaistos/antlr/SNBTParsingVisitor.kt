package org.jglrxavpok.hephaistos.antlr

import org.jglrxavpok.hephaistos.nbt.*

object SNBTParsingVisitor: SNBTBaseVisitor<MutableNBT<out Any>>() {

    override fun aggregateResult(aggregate: MutableNBT<out Any>?, nextResult: MutableNBT<out Any>?): MutableNBT<out Any>? {
        return when {
            aggregate == null && nextResult != null -> nextResult
            aggregate != null && nextResult == null -> aggregate
            aggregate == null && nextResult == null -> null
            else -> /*aggregate != null && nextResult != null ->*/ error("Can't merge $aggregate and $nextResult")
        }
    }

    override fun visitCompound(ctx: SNBTParser.CompoundContext): NBTCompound {
        val compound = NBTCompound()
        ctx.namedElement().forEach {
            compound[(visit(it.name) as NBTString).getValue()] = visit(it.value)
        }
        return compound
    }

    override fun visitList(ctx: SNBTParser.ListContext): NBTList<MutableNBT<out Any>> {
        val elements = ctx.element().map { visit(it) }
        val subtagType = if(elements.isEmpty()) NBTType.TAG_String else elements[0].type
        val list = NBTList<MutableNBT<out Any>>(subtagType)
        for(tag in elements) {
            list += tag
        }
        return list
    }

    override fun visitByteArray(ctx: SNBTParser.ByteArrayContext): NBTByteArray {
        val array = ctx.byteNBT().map { visitByteNBT(it).getValue() }.toByteArray()
        return NBTByteArray(array)
    }

    override fun visitIntArray(ctx: SNBTParser.IntArrayContext): NBTIntArray {
        val array = ctx.intNBT().map { visitIntNBT(it).getValue() }.toIntArray()
        return NBTIntArray(array)
    }

    override fun visitLongArray(ctx: SNBTParser.LongArrayContext): NBTLongArray {
        val array = ctx.longNBT().map { visitLongNBT(it).getValue() }.toLongArray()
        return NBTLongArray(array)
    }

    override fun visitDoubleNBT(ctx: SNBTParser.DoubleNBTContext): NBTDouble {
        var text = ctx.text
        if(text.endsWith('d') || text.endsWith('D')) {
            text = text.dropLast(1)
        }
        return NBTDouble(text.toDouble())
    }

    override fun visitFloatNBT(ctx: SNBTParser.FloatNBTContext): NBTFloat {
        return NBTFloat(ctx.text.dropLast(1).toFloat())
    }

    override fun visitLongNBT(ctx: SNBTParser.LongNBTContext): NBTLong {
        var value = ctx.LONG().text.dropLast(1).toLong()
        return NBTLong(value)
    }

    override fun visitByteNBT(ctx: SNBTParser.ByteNBTContext): NBTByte {
        ctx.BOOLEAN()?.let {
            val booleanValue = ctx.BOOLEAN().text == "true"
            return NBTByte(booleanValue)
        }
        var value = ctx.BYTE().text.dropLast(1).toByte()
        return NBTByte(value)
    }

    override fun visitShortNBT(ctx: SNBTParser.ShortNBTContext): NBTShort {
        var value = ctx.SHORT().text.dropLast(1).toShort()
        return NBTShort(value)
    }

    override fun visitStringNBT(ctx: SNBTParser.StringNBTContext): NBTString {
        return when {
            ctx.DoubleQuoteText() != null -> NBTString(ctx.DoubleQuoteText().text.drop(1).dropLast(1))
            ctx.SingleQuoteText() != null -> NBTString(ctx.SingleQuoteText().text.drop(1).dropLast(1))
            else -> NBTString(ctx.text)
        }
    }

    override fun visitIntNBT(ctx: SNBTParser.IntNBTContext): NBTInt {
        return NBTInt(ctx.INTEGER().text.toInt())
    }

    override fun visitIdentifier(ctx: SNBTParser.IdentifierContext?): MutableNBT<out Any>? {
        error("Should not access this rule directly")
    }
}