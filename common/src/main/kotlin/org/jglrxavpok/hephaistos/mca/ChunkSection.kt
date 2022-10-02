package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.Options
import org.jglrxavpok.hephaistos.mca.readers.ChunkSectionReader
import org.jglrxavpok.hephaistos.mca.writer.ChunkSectionWriter
import org.jglrxavpok.hephaistos.mcdata.Biome
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * 16x16x16 subchunk.
 */
class ChunkSection(val y: Byte) {

    companion object {
        private const val BlockStateSize = 16*16*16
        val BiomeArraySize = 4*4*4
    }

    /**
     * Palette used by this section, best not to touch if you don't know what you are doing
     */
    private var blockPalette: BlockPalette? = null
    val empty get()= blockPalette == null
    private val blockStates: Array<BlockState> = Array(BlockStateSize) { BlockState.AIR }
    var blockLights = ByteArray(0)
    var skyLights = ByteArray(0)

    /**
     * May not exist. If it does, it is 4*4*4 Strings of Biome IDs, for a 4x4x4 volume in the chunk (ie 1 string correspond
     * to the biome for a cube of 4x4x4 blocks).
     * Arranged by X, Z and then Y.
     */
    var biomes: Array<String>? = null

    private var baseBiome = Biome.UnknownBiome

    /**
     * Constructs a ChunkSection from a TAG_Compound.
     * @throws AnvilException if the Compound in the argument is missing fields for loading
     */
    @Throws(AnvilException::class)
    @JvmOverloads
    constructor(nbt: NBTCompound, version: SupportedVersion = SupportedVersion.Latest): this(ChunkSectionReader.getY(nbt)) {
        if(version < SupportedVersion.MC_1_17_0) {
            if(y !in 0..15)
                throw AnvilException("Invalid section Y: $y. Must be in 0..15 for pre-1.17 sections")
        }

        val reader = ChunkSectionReader(version, nbt)

        // empty palette can happen if the section exist but requires more than 8 bits to save the block IDs (in that case, the global ID is directly used)
        val blockPaletteNBT = reader.getBlockPalette()
        blockPalette = blockPaletteNBT?.let { BlockPalette(it) } // We consider that there are no blocks inside this section if the palette is null (because we cannot interpret IDs)

        if(Options.WarnWhenLoadingSectionWithNoPaletteButWithBlocks.active && blockPalette == null) {
            if(reader.hasBlockStates()) {
                System.err.println("[Hephaistos] Attempted to load a ChunkSection with no palette but with block states. Because Hephaistos cannot interpret global IDs, block states will be skipped")
            }
        }

        if(blockPalette != null) {
            val ids = reader.getUncompressedBlockStateIDs()

            var nonAir = false

            for((index, id) in ids.withIndex()) {
                val state = blockPalette!!.elements[id]
                blockStates[index] = state
                if(state !=  BlockState.AIR) nonAir = true
            }

            initializePalette(blockPalette!!, !nonAir)
        }

        reader.getBlockLight()?.let {
            blockLights = ByteArray(it.size)
            it.copyInto(blockLights)
        }
        reader.getSkyLight()?.let {
            skyLights = ByteArray(it.size)
            it.copyInto(skyLights)
        }

        val (biomesArray, baseBiomeValue) = reader.getBiomeInformation()
        biomes = biomesArray
        baseBiome = baseBiomeValue ?: Biome.UnknownBiome
    }

    /**
     * Sets the block state in this section at the given position.
     *
     * Will create the palette if necessary, and will update it after (either created by the call or somewhen else)
     *
     * X,Y,Z must be inside this section (ie in a 16x16x16 cube)
     */
    operator fun set(x: Int, y: Int, z: Int, block: BlockState) {
        checkBounds(x, y, z)
        if(blockPalette == null) {
            blockPalette = BlockPalette() // initialize new palette
            blockPalette!!.elements += BlockState.AIR
            initializePalette(blockPalette!!, true) // load as all air
            blockPalette!!.increaseReference(block)
            blockPalette!!.decreaseReference(BlockState.AIR)
            blockStates[index(x, y, z)] = block
        } else {
            val previous = this[x, y, z]
            blockPalette!!.increaseReference(block)
            blockPalette!!.decreaseReference(previous)
            blockStates[index(x, y, z)] = block
        }
    }

    private fun checkBounds(x: Int, y: Int, z: Int) {
        if(x !in 0..15) throw IllegalArgumentException("x ($x) is not in 0..15")
        if(y !in 0..15) throw IllegalArgumentException("y ($y) is not in 0..15")
        if(z !in 0..15) throw IllegalArgumentException("z ($z) is not in 0..15")
    }

    private fun initializePalette(palette: BlockPalette, isAir: Boolean) {
        if (empty || isAir) {
            palette.referenceCounts.clear()
            palette.referenceCounts[BlockState.AIR] = BlockStateSize
        } else {
            palette.loadReferences(blockStates)
        }
    }

    /**
     * Returns the block light at the given position
     *
     * If this section is empty, will throw AnvilException
     *
     * X,Y,Z must be in a 16x16x16 cube.
     *
     * @throws AnvilException if a get is attempted while this section is empty
     */
    @Throws(AnvilException::class)
    fun getBlockLight(x: Int, y: Int, z: Int): Byte {
        if(empty)
            throw AnvilException("Trying to access empty section!")
        checkBounds(x, y, z)
        val index = index(x,y,z)
        return if(index % 2 == 0) {
            blockLights[index/2] and 0x0F
        } else {
            ((blockLights[index/2].toInt() shr 4) and 0x0F).toByte()
        }
    }

    /**
     * Returns the sky light at the given position
     *
     * If this section is empty, will throw AnvilException
     *
     * X,Y,Z must be in a 16x16x16 cube.
     *
     * @throws AnvilException if a get is attempted while this section is empty
     */
    @Throws(AnvilException::class)
    fun getSkyLight(x: Int, y: Int, z: Int): Byte {
        if(empty)
            throw AnvilException("Trying to access empty section!")
        checkBounds(x, y, z)
        val index = index(x,y,z)
        return if(index % 2 == 0) {
            skyLights[index/2] and 0x0F
        } else {
            ((skyLights[index/2].toInt() shr 4) and 0x0F).toByte()
        }
    }

    /**
     * Sets the sky light in this section at the given position.
     *
     * X,Y,Z must be inside this section (ie in a 16x16x16 cube)
     */
    fun setSkyLight(x: Int, y: Int, z: Int, light: Byte) {
        checkBounds(x, y, z)
        fillInIfEmpty()
        if(skyLights.isEmpty()) {
            skyLights = ByteArray(2048)
        }
        val index = index(x,y,z)
        if(index % 2 == 0) {
            skyLights[index/2] = (skyLights[index/2] and 0xF0.toByte()) or (light and 0x0F)
        } else {
            skyLights[index/2] = (skyLights[index/2] and 0x0F.toByte()) or ((light.toInt() shl 4) and 0x0F).toByte()
        }
    }

    /**
     * Sets the sky light in this section at the given position.
     *
     * X,Y,Z must be inside this section (ie in a 16x16x16 cube)
     */
    fun setBlockLight(x: Int, y: Int, z: Int, light: Byte) {
        checkBounds(x, y, z)
        fillInIfEmpty()
        if(blockLights.isEmpty()) {
            blockLights = ByteArray(2048)
        }
        val index = index(x,y,z)
        if(index % 2 == 0) {
            blockLights[index/2] = (blockLights[index/2] and 0xF0.toByte()) or (light and 0x0F)
        } else {
            blockLights[index/2] = (blockLights[index/2] and 0x0F.toByte()) or ((light.toInt() shl 4) and 0x0F).toByte()
        }
    }

    private fun fillInIfEmpty() {
        if(empty) {
            blockPalette = BlockPalette() // initialize new palette
            blockPalette!!.elements += BlockState.AIR
            initializePalette(blockPalette!!, true) // load as all air
        }
    }

    /**
     * Returns the block state at the given position
     *
     * If this section is empty, will throw AnvilException
     *
     * X,Y,Z must be in a 16x16x16 cube.
     *
     * @throws AnvilException if a get is attempted while this section is empty
     */
    @Throws(AnvilException::class)
    operator fun get(x: Int, y: Int, z: Int): BlockState {
        checkBounds(x, y, z)
        if(empty)
            throw AnvilException("Trying to access empty section!")
        return blockStates[index(x,y,z)]
    }

    /**
     * Returns the biome stored inside this section at the given position
     * Be aware that biome data may not be present inside this column, in that case, this method returns UnknownBiome
     *
     * Coordinates must be section-local
     */
    fun getBiome(x: Int, y: Int, z: Int): String {
        checkBounds(x, y, z)
        if(biomes == null) {
            return baseBiome
        }
        val index = x/4 + (z/4) * 4 + (y/4) * 16
        return biomes!![index]
    }

    /**
     * Sets the biome stored inside this section at the given position
     * If biome data did not exist before calling this method, the biome array is created then filled with UnknownBiome
     *
     * Coordinates must be section-local
     */
    fun setBiome(x: Int, y: Int, z: Int, biomeID: String) {
        checkBounds(x, y, z)
        fillInIfEmpty()
        if(biomes == null) {
            biomes = Array<String>(BiomeArraySize) { Biome.UnknownBiome }
        }
        biomes?.set(x/4 + (z/4) * 4 + (y/4) * 16, biomeID)
    }

    /**
     * Returs true iif this section has biome data
     */
    fun hasBiomeData() = biomes != null

    private fun index(x: Int, y: Int, z: Int) = y * 16 * 16 + z * 16 + x

    /**
     * Converts this ChunkSection into its NBT representation
     */
    @JvmOverloads
    fun toNBT(version: SupportedVersion = SupportedVersion.Latest): NBTCompound = ChunkSectionWriter(version, y).apply {
        if(biomes != null) {
            setAllBiomes(biomes!!)
        }
        setBlockLights(blockLights)
        setSkyLights(skyLights)

        setAllBlockStates(blockStates)
    }.toNBT()


}