package org.jglrxavpok.mca

// TODO: doc
class Heightmap() {

    private var heights = IntArray(16*16)

    constructor(compactVersion: LongArray): this() {
        if(compactVersion.size != 36) { // 16x16
            throw AnvilException("Wrong length for compacted heightmap")
        }
        heights = decompress(compactVersion, 9)
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
        return compress(heights, 9)
    }

    private fun checkBounds(x: Int, z: Int) {
        if(x !in 0..15)
            throw IndexOutOfBoundsException("x must be in 0..15")
        if(z !in 0..15)
            throw IndexOutOfBoundsException("z must be in 0..15")
    }
}
