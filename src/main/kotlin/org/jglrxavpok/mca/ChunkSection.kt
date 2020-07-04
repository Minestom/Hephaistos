package org.jglrxavpok.mca

import org.jglrxavpok.mca.AnvilException.Companion.missing
import org.jglrxavpok.nbt.NBTCompound

// TODO: doc
class ChunkSection(val y: Byte) {

    private var palette: Palette? = null
    val empty get()= palette == null
    private val blockStates: Array<BlockState> = Array(16*16*16) { BlockState.Air }

    init {
        if(y !in 0..15)
            throw AnvilException("Invalid section Y: $y. Must be in 0..15")
    }

    @Throws(AnvilException::class)
    constructor(nbt: NBTCompound): this(nbt.getByte("Y") ?: missing("Y")) {
        // empty palette can happen if the section exist but requires more than 8 bits to save the block IDs (in that case, the global ID is directly used)
        val paletteNBT = nbt.getList<NBTCompound>("Palette")

        // We consider that there are no blocks inside this section if the palette is null (because we cannot interpret IDs)
        palette = paletteNBT?.let { Palette(it) }

        if(palette == null && nbt.containsKey("BlockStates")) {
            System.err.println("[Hephaistos] Attempted to load a ChunkSection with no palette but block states. Because Hephaistos cannot interpret global IDs, block states will be skipped");
        } else if(palette != null) {
            val compactedBlockStates = nbt.getLongArray("BlockStates") ?: missing("BlockStates")
            val sizeInBits = compactedBlockStates.size*64 / 4096
            val ids = decompress(compactedBlockStates, sizeInBits)
            if(ids.size != 16*16*16) {
                throw AnvilException("Invalid decompressed BlockStates length (${ids.size}). Must be 4096 (16x16x16)")
            }

            for((index, id) in ids.withIndex()) {
                blockStates[index] = palette!!.blocks[id]
            }

            palette!!.loadReferences(blockStates.asIterable())
        }
    }

    operator fun set(x: Int, y: Int, z: Int, block: BlockState) {
        if(palette == null) {
            palette = Palette() // initialize new palette
            palette!!.loadReferences(blockStates.asIterable())
        }
        val previous = this[x, y, z]
        palette!!.increaseReference(block)
        palette!!.decreaseReference(previous)
        blockStates[index(x, y, z)] = block
    }

    // TODO: block light
    // TODO: sky light

    operator fun get(x: Int, y: Int, z: Int): BlockState {
        if(empty)
            throw AnvilException("Trying to access empty section!")
        return blockStates[index(x,y,z)]
    }

    private fun index(x: Int, y: Int, z: Int) = y*16*16+x*16+z

    fun toNBT(): NBTCompound {
        return NBTCompound().apply {
            setByte("Y", y)
            // TODO setByteArray("BlockLight", blockLight)
            // TODO setByteArray("SkyLight", skyLight)
            if(!empty) {
                set("Palette", palette!!.toNBT())
                setLongArray("BlockStates", palette!!.compactIDs(blockStates))
            }
        }
    }

}