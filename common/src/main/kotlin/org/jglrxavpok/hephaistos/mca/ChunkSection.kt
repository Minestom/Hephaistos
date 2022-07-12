package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.Options
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.mcdata.Biome
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTLongArray
import org.jglrxavpok.hephaistos.nbt.NBTString
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil
import kotlin.math.log2

/**
 * 16x16x16 subchunk.
 */
class ChunkSection(val y: Byte) {

    /**
     * Palette used by this section, best not to touch if you don't know what you are doing
     */
    private var blockPalette: BlockPalette? = null
    val empty get()= blockPalette == null
    private val blockStates: Array<BlockState> = Array(16*16*16) { BlockState.AIR }
    var blockLights = ByteArray(0)
    var skyLights = ByteArray(0)

    /**
     * May not exist. If it does, it is 4*4*4 Strings of Biome IDs, for a 4x4x4 volume in the chunk (ie 1 string correspond
     * to the biome for a cube of 4x4x4 blocks).
     * Arranged by X, Z and then Y.
     */
    var biomes: Array<String>? = null

    private val biomeArraySize get()= 4*4*4

    private var baseBiome = Biome.UnknownBiome

    /**
     * Constructs a ChunkSection from a TAG_Compound.
     * @throws AnvilException if the Compound in the argument is missing fields for loading
     */
    @Throws(AnvilException::class)
    @JvmOverloads
    constructor(nbt: NBTCompound, version: SupportedVersion = SupportedVersion.Latest): this(nbt.getByte("Y") ?: missing("Y")) {
        if(version < SupportedVersion.MC_1_17_0) {
            if(y !in 0..15)
                throw AnvilException("Invalid section Y: $y. Must be in 0..15 for pre-1.17 sections")
        }

        // empty palette can happen if the section exist but requires more than 8 bits to save the block IDs (in that case, the global ID is directly used)
        val blockPaletteNBT =
            when {
                version < SupportedVersion.MC_1_18_PRE_4 -> nbt.getList<NBTCompound>("Palette")
                else -> nbt.getCompound("block_states")?.getList<NBTCompound>("palette")
            }
        blockPalette = blockPaletteNBT?.let { BlockPalette(it) } // We consider that there are no blocks inside this section if the palette is null (because we cannot interpret IDs)

        if(Options.WarnWhenLoadingSectionWithNoPaletteButWithBlocks.active && blockPalette == null) {
            val hasBlockStates = if(version < SupportedVersion.MC_1_18_PRE_4) {
                nbt.containsKey("BlockStates")
            } else {
                nbt.getCompound("block_states")?.containsKey("data") ?: false
            }

            if(hasBlockStates) {
                System.err.println("[Hephaistos] Attempted to load a ChunkSection with no palette but with block states. Because Hephaistos cannot interpret global IDs, block states will be skipped")
            }
        }

        if(blockPalette != null) {
            val compactedBlockStates =
                when {
                    version < SupportedVersion.MC_1_18_PRE_4 -> nbt.getLongArray("BlockStates") ?: missing("BlockStates")

                    /* no data is seemingly allowed and represents a section which is full of the block at palette index 0
                    * + path has changed
                    * */
                    else -> (nbt.getCompound("block_states") ?: missing("block_states")).getLongArray("data") ?: ImmutableLongArray()
                }

            val sizeInBits = compactedBlockStates.size*64 / 4096
            val ids: IntArray
            when {
                version == SupportedVersion.MC_1_15 -> {
                    ids = decompress(compactedBlockStates, sizeInBits)
                    if(ids.size != 16*16*16) {
                        throw AnvilException("Invalid decompressed BlockStates length (${ids.size}). Must be 4096 (16x16x16)")
                    }
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

                    ids = if(unpack) {
                        unpack(compactedBlockStates, sizeInBits).sliceArray(0 until 4096)
                    } else {
                        IntArray(4096) { 0 }
                    }
                }

                else -> throw AnvilException("Unsupported version for compressed block states: $version")
            }

            for((index, id) in ids.withIndex()) {
                blockStates[index] = blockPalette!!.elements[id]
            }

            blockPalette!!.loadReferences(blockStates.asIterable())
        }

        nbt.getByteArray("BlockLight")?.let {
            blockLights = ByteArray(it.size)
            it.copyInto(blockLights)
        }
        nbt.getByteArray("SkyLight")?.let {
            skyLights = ByteArray(it.size)
            it.copyInto(skyLights)
        }

        if(version >= SupportedVersion.MC_1_18_PRE_4) {
            if("biomes" in nbt) {
                val biomesNBT = nbt.getCompound("biomes")!!
                val paletteNBT = biomesNBT.getList<NBTString>("palette") ?: missing("biomes.palette")
                val biomePalette = BiomePalette(paletteNBT)
                if("data" !in biomesNBT) {
                    if(biomePalette.elements.size > 0) {
                        baseBiome = biomePalette.elements[0]
                    }
                } else {
                    biomes = Array<String>(biomeArraySize) { Biome.UnknownBiome }
                    val compressedBiomes = biomesNBT.getLongArray("data")!!

                    check(biomePalette.elements.isNotEmpty()) { "Cannot have an empty biome palette with biome data!" }
                    if(biomePalette.elements.size == 1) {
                        biomes!!.fill(biomePalette.elements[0])
                    } else {
                        val sizeInBits = ceil(log2(biomePalette.elements.size.toDouble())).toInt()
                        val intPerLong = 64 / sizeInBits
                        val expectedCompressedLength = ceil(biomeArraySize.toDouble() / intPerLong).toInt()
                        if (compressedBiomes.size != expectedCompressedLength) {
                            throw AnvilException("Invalid compressed biomes length (${compressedBiomes.size}). At $sizeInBits bit per value, expected $expectedCompressedLength bytes")
                        }
                        val ids = unpack(compressedBiomes, sizeInBits).sliceArray(0 until biomeArraySize)
                        for ((index, id) in ids.withIndex()) {
                            biomes!![index] = biomePalette.elements[id]
                        }
                    }
                }
            }
        }
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
            blockPalette!!.loadReferences(blockStates.asIterable()) // load as all air
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
            blockPalette!!.loadReferences(blockStates.asIterable()) // load as all air
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
        val index = x/4+(z/4)*4+(y/4)*16
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
            biomes = Array<String>(biomeArraySize) { Biome.UnknownBiome }
        }
        biomes?.set(x/4+(z/4)*4+(y/4)*16, biomeID)
    }

    /**
     * Returs true iif this section has biome data
     */
    fun hasBiomeData() = biomes != null

    private fun index(x: Int, y: Int, z: Int) = y*16*16+z*16+x

    /**
     * Converts this ChunkSection into its NBT representation
     */
    @JvmOverloads
    fun toNBT(version: SupportedVersion = SupportedVersion.Latest): NBTCompound = NBT.Kompound {
        this["Y"] = NBT.Byte(y)
        if(blockLights.isNotEmpty()) {
            this["BlockLight"] = NBT.ByteArray(*blockLights)
        }
        if(skyLights.isNotEmpty()) {
            this["SkyLight"] = NBT.ByteArray(*skyLights)
        }
        if(!empty) {
            if(version < SupportedVersion.MC_1_18_PRE_4) {
                this["Palette"] = blockPalette!!.toNBT()
                this["BlockStates"] = NBT.LongArray(blockPalette!!.compactIDs(blockStates, version, 4))
            } else {
                this["block_states"] = NBT.Kompound {
                    this["palette"] = blockPalette!!.toNBT()
                    this["data"] = NBT.LongArray(blockPalette!!.compactIDs(blockStates, version, 4))
                }


                if(biomes != null) {
                    val biomePalette = BiomePalette()
                    for(b in biomes!!) {
                        biomePalette.increaseReference(b)
                    }
                    this["biomes"] = NBT.Kompound {
                        this["palette"] = biomePalette.toNBT()
                        this["data"] = NBT.LongArray(biomePalette.compactIDs(biomes!!, version))
                    }
                }
            }
        }
    }


}