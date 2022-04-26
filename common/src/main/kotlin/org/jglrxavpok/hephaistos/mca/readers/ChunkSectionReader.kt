package org.jglrxavpok.hephaistos.mca.readers

import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.mca.*
import org.jglrxavpok.hephaistos.mcdata.Biome
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTString
import kotlin.math.ceil

/**
 * Extracts section data from a NBTCompound.
 * Does not cache nor convert the raw data.
 * Use when you want to read parts of a section without having the memory overhead of ChunkSection.
 *
 * Only two piece of information are cached with this object: the version of the parent chunk, which is used to know how to extract the section data, and the input NBT.
 *
 * Can throw AnvilException if the NBT data is not recognized or incomplete.
 *
 * For information on what the different fields extracted from the NBT are, see [Chunk Format on the Minecraft wiki](https://minecraft.fandom.com/wiki/Chunk_format)
 */
class ChunkSectionReader constructor(val version: SupportedVersion, val nbt: NBTCompound) {

    companion object {
        /**
         * Gets the Y value of the section.
         * Inside a companion object as ChunkSection needs it very early in its construction
         */
        @Throws(AnvilException::class)
        @JvmStatic
        fun getY(nbt: NBTCompound) = nbt.getByte("Y") ?: AnvilException.missing("Y")

        private val BiomeArraySize = 4*4*4
    }

    fun getBlockPalette() = when {
        version < SupportedVersion.MC_1_18_PRE_4 -> nbt.getList<NBTCompound>("Palette")
        else -> nbt.getCompound("block_states")?.getList<NBTCompound>("palette")
    }

    fun getCompactedBlockStates() = when {
        version < SupportedVersion.MC_1_18_PRE_4 -> nbt.getLongArray("BlockStates") ?: AnvilException.missing("BlockStates")

        /* no data is seemingly allowed since 1.18 and represents a section which is full of the block at palette index 0
        * + path has changed
        * */
        else -> (nbt.getCompound("block_states") ?: AnvilException.missing("block_states")).getLongArray("data") ?: ImmutableLongArray()
    }

    fun hasBlockStates() = if(version < SupportedVersion.MC_1_18_PRE_4) {
        nbt.containsKey("BlockStates")
    } else {
        nbt.getCompound("block_states")?.containsKey("data") ?: false
    }

    /**
     * Can be null if absent
     */
    fun getBlockLight() = nbt.getByteArray("BlockLight")

    /**
     * Can be null if absent
     */
    fun getSkyLight() = nbt.getByteArray("SkyLight")

    /**
     * Returns whether this section has biome info (since 1.18)
     */
    fun hasBiomes() = version >= SupportedVersion.MC_1_18_PRE_4 && "biomes" in nbt

    /**
     * Can be null if absent
     */
    fun getBiomes() = nbt.getCompound("biomes")

    fun getUncompressedBlockStateIDs(): IntArray {
        val compactedBlockStates = getCompactedBlockStates()

        val sizeInBits = compactedBlockStates.size*64 / 4096
        when {
            version == SupportedVersion.MC_1_15 -> {
                val ids = decompress(compactedBlockStates, sizeInBits)
                if(ids.size != 16*16*16) {
                    throw AnvilException("Invalid decompressed BlockStates length (${ids.size}). Must be 4096 (16x16x16)")
                }
                return ids
            }

            version >= SupportedVersion.MC_1_16 -> {
                val expectedCompressedLength =
                    if(compactedBlockStates.size == 0) {
                        -1 /* force invalid value */
                    } else {
                        val intPerLong = 64 / sizeInBits
                        ceil(4096.0 / intPerLong).toInt()
                    }
                var unpack = true
                if(compactedBlockStates.size != expectedCompressedLength) {
                    if(version >= SupportedVersion.MC_1_18_PRE_4 && compactedBlockStates.size == 0) {
                        // palette only has a single element
                        unpack = false
                    } else {
                        throw AnvilException("Invalid compressed BlockStates length (${compactedBlockStates.size}). At $sizeInBits bit per value, expected $expectedCompressedLength bytes. Note that 0 length is not allowed with pre 1.18 formats.")
                    }
                }

                return if(unpack) {
                    unpack(compactedBlockStates, sizeInBits).sliceArray(0 until 4096)
                } else {
                    IntArray(4096) { 0 }
                }
            }

            else -> throw AnvilException("Unsupported version for compressed block states: $version")
        }
    }

    fun getUncompressedBiomeIndices(): IntArray {
        if(version < SupportedVersion.MC_1_18_PRE_4)
            throw AnvilException("No biomes inside sections before 1.18")

        val biomesNBT = getBiomes()!!
        val paletteNBT = biomesNBT.getList<NBTString>("palette") ?: AnvilException.missing("biomes.palette")
        val biomePalette = BiomePalette(paletteNBT)

        val compressedBiomes = biomesNBT.getLongArray("data")!!

        val sizeInBits = compressedBiomes.size * 64 / BiomeArraySize
        val intPerLong = 64 / sizeInBits
        val expectedCompressedLength = ceil(BiomeArraySize.toDouble() / intPerLong).toInt()
        if (compressedBiomes.size != expectedCompressedLength) {
            throw AnvilException("Invalid compressed biomes length (${compressedBiomes.size}). At $sizeInBits bit per value, expected $expectedCompressedLength bytes")
        }
        return unpack(compressedBiomes, sizeInBits).sliceArray(0 until BiomeArraySize)
    }

    /**
     * Returns the corresponding SectionBiomeInformation for this biome. This does *not* throw if no biome information exists, but simply returns an empty object
     */
    fun getBiomeInformation(): SectionBiomeInformation {
        if(!hasBiomes()) {
            return SectionBiomeInformation()
        }
        var biomes: Array<String>? = null
        val biomesNBT = getBiomes()!!
        val paletteNBT = biomesNBT.getList<NBTString>("palette") ?: AnvilException.missing("biomes.palette")
        val biomePalette = BiomePalette(paletteNBT)
        if("data" !in biomesNBT) {
            if(biomePalette.elements.size > 0) {
                return SectionBiomeInformation(biomes = null, baseBiome = biomePalette.elements[0])
            }
            return SectionBiomeInformation()
        } else {
            biomes = Array<String>(BiomeArraySize) { Biome.UnknownBiome }
            val ids = getUncompressedBiomeIndices()
            for ((index, id) in ids.withIndex()) {
                biomes[index] = biomePalette.elements[id]
            }
            return SectionBiomeInformation(biomes = biomes, baseBiome = null)
        }
    }
}