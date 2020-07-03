package org.jglrxavpok.mca

// TODO: doc
class Heightmap() {

    private var heights = IntArray(16*16)

    constructor(compactVersion: LongArray): this() {
        if(compactVersion.size != 36) { // 16x16
            throw AnvilException("Wrong length for compacted heightmap")
        }
        var bitIndex = 0
        for(i in heights.indices) {
            val beginLongIndex = bitIndex / 64
            val endLongIndex = (bitIndex + 8) / 64

            var height = 0
            if(beginLongIndex != endLongIndex) { // split over two longs
                val overshoot = (bitIndex+9) % 64
                val lowMask = (1 shl 9-overshoot) -1
                val highMask = (1 shl overshoot) -1
                val low = compactVersion[beginLongIndex] shr (bitIndex % 64) and lowMask.toLong()
                val high = compactVersion[endLongIndex] and highMask.toLong()
                height = (low or (high shl 9-overshoot)).toInt()
            } else {
                height = ((compactVersion[beginLongIndex] shr (bitIndex%64)) and 0b1111_11111).toInt()
            }

            heights[bitIndex/9] = height

            bitIndex += 9
        }
    }

    operator fun get(x: Int, z: Int): Int {
        checkBounds(x, z)
        return heights[z*16+x]
    }

    operator fun set(x: Int, z: Int, height: Int) {
        checkBounds(x, z)
        heights[z*16+x] = height
    }

    fun compact(): LongArray {
        val compacted = LongArray(36)
        var bitIndex = 0

        for(i in heights.indices) {
            val height = heights[i] and 0b11111_1111

            val beginLongIndex = bitIndex / 64
            val endLongIndex = (bitIndex + 8) / 64

            if(beginLongIndex != endLongIndex) { // split over two longs
                val overshoot = (bitIndex+9) % 64
                val highMask = (1 shl overshoot) -1
                val mask = ((1 shl 9-overshoot)-1).toLong()
                compacted[beginLongIndex] = compacted[beginLongIndex] and (0b1111_11111.toLong() shl bitIndex%64).inv().toLong() or (height.toLong() and mask shl (bitIndex%64))
                compacted[endLongIndex] = compacted[endLongIndex] or ((height.toLong() and 0b1111_11111 shr (64-(bitIndex%64))) and highMask.toLong())
            } else {
                compacted[beginLongIndex] = compacted[beginLongIndex] and (0b1111_11111.toLong() shl bitIndex%64).inv() or (height.toLong() and 0b1111_11111 shl (bitIndex%64))
            }
            bitIndex += 9
        }
        return compacted
    }

    private fun checkBounds(x: Int, z: Int) {
        if(x !in 0..15)
            throw IndexOutOfBoundsException("x must be in 0..15")
        if(z !in 0..15)
            throw IndexOutOfBoundsException("z must be in 0..15")
    }
}
