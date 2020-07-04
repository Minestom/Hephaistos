package org.jglrxavpok.mca

import kotlin.math.ceil

fun compress(data: IntArray, lengthInBits: Int): LongArray {
    val compacted = LongArray(ceil(data.size*lengthInBits/64.0).toInt())
    var bitIndex = 0
    val maxMask = (1L shl lengthInBits) -1

    for(i in data.indices) {
        val value = data[i] and 0b11111_1111

        val beginLongIndex = bitIndex / 64
        val endLongIndex = (bitIndex + lengthInBits-1) / 64

        if(beginLongIndex != endLongIndex) { // split over two longs
            val overshoot = (bitIndex+lengthInBits) % 64
            val highMask = (1 shl overshoot) -1
            val mask = ((1 shl lengthInBits-overshoot)-1).toLong()
            compacted[beginLongIndex] = compacted[beginLongIndex] and (maxMask shl bitIndex%64).inv().toLong() or (value.toLong() and mask shl (bitIndex%64))
            compacted[endLongIndex] = compacted[endLongIndex] or ((value.toLong() and maxMask shr (64-(bitIndex%64))) and highMask.toLong())
        } else {
            compacted[beginLongIndex] = compacted[beginLongIndex] and (maxMask shl bitIndex%64).inv() or (value.toLong() and maxMask shl (bitIndex%64))
        }
        bitIndex += lengthInBits
    }
    return compacted
}

fun decompress(data: LongArray, lengthInBits: Int): IntArray {
    var bitIndex = 0
    val count = (data.size.toLong()*64 / lengthInBits).toInt()
    val result = IntArray(count)
    val maxMask = (1L shl lengthInBits) -1

    for(i in result.indices) {
        val beginLongIndex = bitIndex / 64
        val endLongIndex = (bitIndex + lengthInBits-1) / 64

        var value = 0
        value = if(beginLongIndex != endLongIndex) { // split over two longs
            val overshoot = (bitIndex+lengthInBits) % 64
            val lowMask = (1L shl lengthInBits-overshoot) -1
            val highMask = (1L shl overshoot) -1
            val low = data[beginLongIndex] shr (bitIndex % 64) and lowMask
            val high = data[endLongIndex] and highMask
            (low or (high shl lengthInBits-overshoot)).toInt()
        } else {
            ((data[beginLongIndex] shr (bitIndex%64)) and maxMask).toInt()
        }

        result[bitIndex/lengthInBits] = value

        bitIndex += lengthInBits
    }
    return result
}