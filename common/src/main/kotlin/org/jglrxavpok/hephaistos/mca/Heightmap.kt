package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.collections.ImmutableLongArray

/**
 * Represents a heightmap for a 16x16 area. Heights are assumed to be in the range in 0..255
 */
class Heightmap() {

    private var heights = IntArray(16*16)

    /**
     * Constructs the heightmap from a compressed heightmap (36 longs long)
     */
    @JvmOverloads
    constructor(compactVersion: ImmutableLongArray, version: SupportedVersion = SupportedVersion.Latest): this() {
        when {
            version == SupportedVersion.MC_1_15 -> {
                if(compactVersion.size != 36) { // 16x16
                    throw AnvilException("Wrong length for 1.15 compacted heightmap (found ${compactVersion.size}, expected 36)")
                }
                heights = decompress(compactVersion, 9)
            }

            version >= SupportedVersion.MC_1_16 -> {
                if(compactVersion.size != 37) { // 16x16
                    throw AnvilException("Wrong length for 1.16 compacted heightmap (found ${compactVersion.size}, expected 37)")
                }
                heights = unpack(compactVersion, 9)
            }
        }
    }

    /**
     * Gets the height saved at the given coordinates. Coordinates must be in 16x16 square
     *
     * @throws IllegalArgumentException if the coordinates are not inside the heightmap
     */
    operator fun get(x: Int, z: Int): Int {
        checkBounds(x, z)
        return heights[z*16+x]
    }

    /**
     * Sets the height saved at the given coordinates. Coordinates must be in 16x16 square
     *
     * @throws IllegalArgumentException if the coordinates are not inside the heightmap
     */
    operator fun set(x: Int, z: Int, height: Int) {
        checkBounds(x, z)
        heights[z*16+x] = height
    }

    /**
     * Creates a compressed version of this heightmap, to be transferred over network, or saved to disk.
     * (9bit heights saved in longs, resulting in a long array of 36 longs)
     */
    @JvmOverloads
    fun compact(version: SupportedVersion = SupportedVersion.Latest): ImmutableLongArray {
        return when {
            version == SupportedVersion.MC_1_15 -> compress(heights, 9)
            version >= SupportedVersion.MC_1_16 -> pack(heights, 9)

            else -> throw AnvilException("Unsupported data version for heightmap: $version")
        }
    }

    private fun checkBounds(x: Int, z: Int) {
        if(x !in 0..15)
            throw IllegalArgumentException("x must be in 0..15")
        if(z !in 0..15)
            throw IllegalArgumentException("z must be in 0..15")
    }
}
