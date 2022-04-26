package org.jglrxavpok.hephaistos.mca.readers

/**
 * Holds the information about biomes for a single section.
 * There are 3 possible: scenarii:
 * 1. biomes = null and baseBiome = null
 *      This means the section is from before 1.18, so no biome information exists in the section.
 * 2. biomes = null and baseBiome != null
 *      This means the section has biome information, but the entire section is filled with baseBiome.
 *      Technically this means there is a palette with at least 1 element but no data.
 * 3. biomes != null
 *      The biomes array holds the biomes for this section.
 */
data class SectionBiomeInformation(val biomes: Array<String>?, val baseBiome: String?) {
    constructor(): this(null, null)

    /**
     * Likely to be a pre-1.18 section
     */
    fun hasBiomeInformation() = biomes == null && baseBiome == null

    /**
     * Is the entire section filled with a single biome?
     * Note that this can return false even if only a single biome is present inside the 'biomes' array,
     * as this denotes an optimisation when saving worlds to reduce file size.
     *
     * Technically, this means there is a biome palette with at least 1 element inside the section, but no data.
     */
    fun isFilledWithSingleBiome() = biomes == null && baseBiome != null
}
