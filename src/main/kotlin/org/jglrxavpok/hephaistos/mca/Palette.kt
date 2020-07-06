package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import java.lang.IllegalArgumentException
import kotlin.math.ceil
import kotlin.math.log2

/**
 * Represents the palette of blocks used in a chunk section. This palette allows to save space when saving to disk or transferring over network,
 * as it lowers the required number of bits used to represent a block state, by remapping global IDs to local IDs, with fewer bits per entry.
 */
class Palette() {

    val blocks = ArrayList<BlockState>()
    private val referenceCounts = HashMap<BlockState, Int>()

    internal constructor(blocks: NBTList<NBTCompound>): this() {
        for(b in blocks) {
            this.blocks += BlockState(b)
        }
    }

    internal fun loadReferences(states: Iterable<BlockState>) {
        for(state in states) {
            if(state !in blocks) {
                throw IllegalArgumentException("Tried to add a reference counter to $state which is not in this palette")
            }
            val ref = referenceCounts.computeIfAbsent(state) {0}
            referenceCounts[state] = ref+1
        }
    }

    /**
     * Increases the reference count of the given block state.
     * If the block state was not referenced, it is added to this palette and its reference becomes 1.
     */
    fun increaseReference(block: BlockState) {
        if(referenceCounts.containsKey(block)) {
            referenceCounts[block] = referenceCounts[block]!!+1
        } else {
            referenceCounts[block] = 1
            blocks.add(block)
        }
    }

    /**
     * Decreases the number of references to the given block state.
     * If the reference becomes <= 0, the block is removed from this palette, and its reference becomes 0.
     * @throws IllegalArgumentException if the block was not referenced or was not in this palette
     */
    fun decreaseReference(block: BlockState) {
        if(referenceCounts.containsKey(block)) {
            referenceCounts[block] = referenceCounts[block]!!-1
            if(referenceCounts[block]!! <= 0) {
                blocks.remove(block)
                referenceCounts.remove(block)
            }
        } else {
            throw IllegalArgumentException("Block state $block was not in the palette when trying to decrease its reference count")
        }
    }

    /**
     * Converts this Palette into its NBT representation
     */
    fun toNBT(): NBTList<NBTCompound> {
        val list = NBTList<NBTCompound>(NBTTypes.TAG_Compound)
        for(b in blocks) {
            list += b.toNBT()
        }
        return list
    }

    /**
     * Produces a long array with the compacted IDs based on this palette.
     * Bit length is selected on the size of this palette (`ceil(log2(size))`), ID correspond to the index inside this palette
     */
    fun compactIDs(states: Array<BlockState>): LongArray {
        // convert state list into uncompressed data
        val indices = states.map(blocks::indexOf).toIntArray()
        val bitLength = ceil(log2(blocks.size.toFloat())).toInt().coerceAtLeast(1) // at least one bit
        return compress(indices, bitLength)
    }

    /**
     * Returns true iif the only referenced block inside this palette is "minecraft:air"
     */
    fun isEmpty(): Boolean {
        return blocks.size == 1 && blocks[0] == BlockState.Air
    }

}
