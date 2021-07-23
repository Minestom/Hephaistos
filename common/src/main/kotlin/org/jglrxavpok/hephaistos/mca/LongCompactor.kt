package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Compresses the given ints into a long array, at the given lengthInBits bitrate.
 *
 * lengthInBits must be at least 1
 */
fun compress(data: IntArray, lengthInBits: Int): ImmutableLongArray {
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
            compacted[beginLongIndex] = compacted[beginLongIndex] and (maxMask shl bitIndex%64).inv() or (value.toLong() and mask shl (bitIndex%64))
            compacted[endLongIndex] = compacted[endLongIndex] or ((value.toLong() and maxMask shr (64-(bitIndex%64))) and highMask.toLong())
        } else {
            compacted[beginLongIndex] = compacted[beginLongIndex] and (maxMask shl bitIndex%64).inv() or (value.toLong() and maxMask shl (bitIndex%64))
        }
        bitIndex += lengthInBits
    }
    return ImmutableLongArray(*compacted)
}

/**
 * Decompresses compressed 'data' into a int array, with lengthInBits bits per int.
 */
fun decompress(data: ImmutableLongArray, lengthInBits: Int): IntArray {
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

/**
 * Unpacks int values of 'lengthInBits' bits from a long array.
 * Contrary to decompress, this method will produce unused bits and do not overflow remaining bits to the next long.
 *
 * (ie 2 >32 bit long values will produce two longs, but the highest bits of each long will be unused)
 */
fun unpack(longs: ImmutableLongArray, lengthInBits: Int): IntArray {
    val intPerLong = floor(64.0 / lengthInBits)
    val intCount = ceil(longs.size * intPerLong).toInt()
    val ints = IntArray(intCount)
    val intPerLongCeil = ceil(intPerLong).toInt()
    val mask = (1 shl lengthInBits)-1L
    for(i in ints.indices) {
        val longIndex = i / intPerLongCeil
        val subIndex = i % intPerLongCeil
        val value = ((longs[longIndex] shr (subIndex*lengthInBits)) and mask).toInt()
        ints[i] = value
    }
    return ints
}

/**
 * Packs ints into a long array. Produces unused bits and does not partially overflow to next long on boundaries.
 */
fun pack(ints: IntArray, lengthInBits: Int): ImmutableLongArray {
    val intPerLong = floor(64.0 / lengthInBits).toInt()
    val longCount = ceil(ints.size / intPerLong.toDouble()).toInt()
    val longs = LongArray(longCount)
    val mask = (1 shl lengthInBits)-1L
    for(i in longs.indices) {
        var long = 0L
        for(intIndex in 0 until intPerLong) {
            val bitIndex = intIndex * lengthInBits
            val intActualIndex = intIndex+i*intPerLong
            if(intActualIndex < ints.size) {
                val value = (ints[intActualIndex].toLong() and mask) shl bitIndex
                long = long or (value)
            }
        }
        longs[i] = long
    }
    return ImmutableLongArray(*longs)
}