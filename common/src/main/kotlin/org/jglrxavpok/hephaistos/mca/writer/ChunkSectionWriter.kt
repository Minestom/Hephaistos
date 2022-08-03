package org.jglrxavpok.hephaistos.mca.writer

import org.jglrxavpok.hephaistos.mca.*
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound

/**
 * Writes section data to a NBTCompound.
 * Allows to save section without having to endure the overhead of ChunkSection (& BlockState)
 */
class ChunkSectionWriter(val version: SupportedVersion, val y: Byte) {

    /**
     * See ChunkSection#empty
     */
    private val empty: Boolean get() = blockStatePalette == null

    private var blockLights = ByteArray(0)
    private var skyLights = ByteArray(0)

    private var biomePalette: BiomePalette? = null
    private var biomeIndices: IntArray? = null

    private var blockStatePalette: BlockPalette? = null
    private var blockStateIndices: IntArray? = null

    /**
     * Sets the block lights of this section. Does not copy the input array
     */
    fun setBlockLights(lights: ByteArray) {
        blockLights = lights
    }

    /**
     * Sets the sky lights of this section. Does not copy the input array
     */
    fun setSkyLights(lights: ByteArray) {
        skyLights = lights
    }

    /**
     * Use this to set biomes directly. Is less performant than setPalettedBiomes
     */
    fun setAllBiomes(biomes: Array<String>) {
        check(biomes.size == ChunkSection.BiomeArraySize) { "biomes.size (${biomes.size}) != ChunkSection.BiomeArraySize (${ChunkSection.BiomeArraySize})" }
        val biomePalette = BiomePalette()
        for(b in biomes) {
            biomePalette.increaseReference(b)
        }

        setPalettedBiomes(biomePalette, IntArray(ChunkSection.BiomeArraySize) { index ->
            biomePalette.elements.indexOf(biomes[index])
        })
    }

    /**
     * Use this to set biomes directly. Is less performant than setPalettedBiomes
     */
    fun setAllBlockStates(blockStates: Array<BlockState>) {
        check(blockStates.size == 16*16*16) { "blockStates.size (${blockStates.size}) != 16x16x16 (${16*16*16})" }
        val blockPalette = BlockPalette()
        for(b in blockStates) {
            blockPalette.increaseReference(b)
        }

        setPalettedBlockStates(blockPalette, IntArray(16*16*16) { index ->
            blockPalette.elements.indexOf(blockStates[index])
        })
    }

    /**
     * Use this to set the biome palette and biome data directly. More performant than setAllBiomes
     */
    fun setPalettedBiomes(biomePalette: BiomePalette, palettedBiomes: IntArray) {
        this.biomePalette = biomePalette
        this.biomeIndices = palettedBiomes
    }

    /**
     * Use this to set the block state palette and block state data directly. More performant than setAllBlockStates
     */
    fun setPalettedBlockStates(blockPalette: BlockPalette, palettedBlockStates: IntArray) {
        this.blockStatePalette = blockPalette
        this.blockStateIndices = palettedBlockStates
    }

    fun toNBT(): NBTCompound = NBT.Kompound {
        this["Y"] = NBT.Byte(y)
        if(blockLights.isNotEmpty()) {
            this["BlockLight"] = NBT.ByteArray(*blockLights)
        }
        if(skyLights.isNotEmpty()) {
            this["SkyLight"] = NBT.ByteArray(*skyLights)
        }
        if(!empty) {
            if(version < SupportedVersion.MC_1_18_PRE_4) {
                this["Palette"] = blockStatePalette!!.toNBT()
                this["BlockStates"] = NBT.LongArray(blockStatePalette!!.compactPreProcessedIDs(blockStateIndices!!, version, 4))
            } else {
                this["block_states"] = NBT.Kompound {
                    this["palette"] = blockStatePalette!!.toNBT()
                    this["data"] = NBT.LongArray(blockStatePalette!!.compactPreProcessedIDs(blockStateIndices!!, version, 4))
                }

                if(biomePalette != null) {
                    this["biomes"] = NBT.Kompound {
                        this["palette"] = biomePalette!!.toNBT()
                        this["data"] = NBT.LongArray(biomePalette!!.compactPreProcessedIDs(biomeIndices!!, version))
                    }
                }
            }
        }
    }

}