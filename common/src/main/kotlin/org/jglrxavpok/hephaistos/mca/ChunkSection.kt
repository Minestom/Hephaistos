package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import java.lang.IllegalArgumentException
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil

/**
 * 16x16x16 subchunk.
 */
class ChunkSection(val y: Byte) {

    /**
     * Palette used by this section, best not to touch if you don't know what you are doing
     */
    private var palette: Palette? = null
    val empty get()= palette == null
    private val blockStates: Array<BlockState> = Array(16*16*16) { BlockState.AIR }
    val blockLights = ByteArray(2048)
    val skyLights = ByteArray(2048)

    init {
        if(y !in 0..15)
            throw AnvilException("Invalid section Y: $y. Must be in 0..15")
    }

    /**
     * Constructs a ChunkSection from a TAG_Compound.
     * @throws AnvilException if the Compound in the argument is missing fields for loading
     */
    @Throws(AnvilException::class)
    @JvmOverloads
    constructor(nbt: NBTCompound, version: SupportedVersion = SupportedVersion.Latest): this(nbt.getByte("Y") ?: missing("Y")) {
        // empty palette can happen if the section exist but requires more than 8 bits to save the block IDs (in that case, the global ID is directly used)
        val paletteNBT = nbt.getList<NBTCompound>("Palette")

        // We consider that there are no blocks inside this section if the palette is null (because we cannot interpret IDs)
        palette = paletteNBT?.let { Palette(it) }

        if(palette == null && nbt.containsKey("BlockStates")) {
            System.err.println("[Hephaistos] Attempted to load a ChunkSection with no palette but block states. Because Hephaistos cannot interpret global IDs, block states will be skipped")
        } else if(palette != null) {
            val compactedBlockStates = nbt.getLongArray("BlockStates") ?: missing("BlockStates")
            val sizeInBits = compactedBlockStates.size*64 / 4096
            val ids: IntArray
            when(version) {
                SupportedVersion.MC_1_15 -> {
                    ids = decompress(compactedBlockStates, sizeInBits)
                    if(ids.size != 16*16*16) {
                        throw AnvilException("Invalid decompressed BlockStates length (${ids.size}). Must be 4096 (16x16x16)")
                    }
                }

                SupportedVersion.MC_1_16 -> {
                    val intPerLong = 64 / sizeInBits
                    val expectedCompressedLength = ceil(4096.0 / intPerLong).toInt()
                    if(compactedBlockStates.size != expectedCompressedLength) {
                        throw AnvilException("Invalid compressed BlockStates length (${compactedBlockStates.size}). At $sizeInBits bit per value, expected $expectedCompressedLength bytes")
                    }
                    ids = unpack(compactedBlockStates, sizeInBits).sliceArray(0 until 4096)
                }

                else -> throw AnvilException("Unsupported version for compressed block states: $version")
            }

            for((index, id) in ids.withIndex()) {
                blockStates[index] = palette!!.blocks[id]
            }

            palette!!.loadReferences(blockStates.asIterable())

            nbt.getByteArray("BlockLight")?.copyInto(blockLights)
            nbt.getByteArray("SkyLight")?.copyInto(skyLights)
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
        if(palette == null) {
            palette = Palette() // initialize new palette
            palette!!.blocks += BlockState.AIR
            palette!!.loadReferences(blockStates.asIterable()) // load as all air
            palette!!.increaseReference(block)
            palette!!.decreaseReference(BlockState.AIR)
            blockStates[index(x, y, z)] = block
        } else {
            val previous = this[x, y, z]
            palette!!.increaseReference(block)
            palette!!.decreaseReference(previous)
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
        val index = index(x,y,z)
        if(index % 2 == 0) {
            blockLights[index/2] = (blockLights[index/2] and 0xF0.toByte()) or (light and 0x0F)
        } else {
            blockLights[index/2] = (blockLights[index/2] and 0x0F.toByte()) or ((light.toInt() shl 4) and 0x0F).toByte()
        }
    }

    private fun fillInIfEmpty() {
        if(empty) {
            palette = Palette() // initialize new palette
            palette!!.blocks += BlockState.AIR
            palette!!.loadReferences(blockStates.asIterable()) // load as all air
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

    private fun index(x: Int, y: Int, z: Int) = y*16*16+z*16+x

    /**
     * Converts this ChunkSection into its NBT representation
     */
    @JvmOverloads
    fun toNBT(version: SupportedVersion = SupportedVersion.Latest): NBTCompound = NBT.Compound {
        it["Y"] = NBT.Byte(y)
        it["BlockLight"] = NBT.ByteArray(*blockLights)
        it["SkyLight"] = NBT.ByteArray(*skyLights)
        if(!empty) {
            it["Palette"] = palette!!.toNBT()
            it["BlockStates"] = NBT.LongArray(palette!!.compactIDs(blockStates, version))
        }
    }


}