package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.mcdata.Biome
import org.jglrxavpok.hephaistos.nbt.*
import kotlin.math.ceil
import kotlin.math.log2

/**
 * Represents the palette of elements used in a chunk section. This palette allows to save space when saving to disk or transferring over network,
 * as it lowers the required number of bits used to represent an element, by remapping global IDs to local IDs, with fewer bits per entry.
 */
sealed class Palette<ElementType>(private val nbtType: NBTType<out NBT>, private val defaultValue: ElementType, private val writer: (ElementType) -> NBT) {

    val elements = mutableListOf<ElementType>()
    private val referenceCounts = HashMap<ElementType, Int>()

    internal fun loadReferences(states: Iterable<ElementType>) {
        for(state in states) {
            if(state !in elements) {
                throw IllegalArgumentException("Tried to add a reference counter to $state which is not in this palette")
            }
            val ref = referenceCounts.computeIfAbsent(state) {0}
            referenceCounts[state] = ref+1
        }
    }

    /**
     * Increases the reference count of the given element.
     * If the element was not referenced, it is added to this palette and its reference becomes 1.
     */
    fun increaseReference(block: ElementType) {
        if(referenceCounts.containsKey(block)) {
            referenceCounts[block] = referenceCounts[block]!!+1
        } else {
            referenceCounts[block] = 1
            elements.add(block)
        }
    }

    /**
     * Decreases the number of references to the given element.
     * If the reference becomes <= 0, the element is removed from this palette, and its reference becomes 0.
     * @throws IllegalArgumentException if the element was not referenced or was not in this palette
     */
    fun decreaseReference(block: ElementType) {
        if(referenceCounts.containsKey(block)) {
            referenceCounts[block] = referenceCounts[block]!!-1
            if(referenceCounts[block]!! <= 0) {
                elements.remove(block)
                referenceCounts.remove(block)
            }
        } else {
            throw IllegalArgumentException("Element $block was not in the palette when trying to decrease its reference count")
        }
    }

    /**
     * Converts this Palette into its NBT representation
     */
    fun toNBT(): NBTList<NBT> =
        NBT.List(nbtType, elements.map { writer(it) })

    /**
     * Index of the given element inside the palette. Returns -1 if none
     */
    fun getPaletteIndex(obj: ElementType): Int = elements.indexOf(obj)

    /**
     * Produces a long array with the compacted IDs based on this palette. The 'indices' array is supposed to be the states already paletted (ie a value inside the 'indices' array is an index into the palette already)
     * Bit length is selected on the size of this palette (`ceil(log2(size))`), ID correspond to the index inside this palette
     */
    @JvmOverloads
    fun compactPreProcessedIDs(indices: IntArray, version: SupportedVersion = SupportedVersion.Latest, minimumBitSize: Int = 1): ImmutableLongArray {
        if(minimumBitSize <= 0) {
            error("Minimum bit size cannot be 0 or negative")
        }

        // convert state list into uncompressed data
        val bitLength = ceil(log2(elements.size.toFloat())).toInt().coerceAtLeast(minimumBitSize) // at least one bit
        return when {
            version == SupportedVersion.MC_1_15 -> compress(indices, bitLength)
            version >= SupportedVersion.MC_1_16 -> pack(indices, bitLength)

            else -> throw AnvilException("Unsupported version for compacting palette: $version")
        }
    }

    /**
     * Produces a long array with the compacted IDs based on this palette.
     * Bit length is selected on the size of this palette (`ceil(log2(size))`), ID correspond to the index inside this palette
     */
    @JvmOverloads
    fun compactIDs(states: Array<ElementType>, version: SupportedVersion = SupportedVersion.Latest, minimumBitSize: Int = 1): ImmutableLongArray {
        if(minimumBitSize <= 0) {
            error("Minimum bit size cannot be 0 or negative")
        }

        // convert state list into uncompressed data
        val indices = states.map(elements::indexOf).toIntArray()
        val bitLength = ceil(log2(elements.size.toFloat())).toInt().coerceAtLeast(minimumBitSize) // at least one bit
        return when {
            version == SupportedVersion.MC_1_15 -> compress(indices, bitLength)
            version >= SupportedVersion.MC_1_16 -> pack(indices, bitLength)

            else -> throw AnvilException("Unsupported version for compacting palette: $version")
        }
    }

    /**
     * Returns true iif the only referenced block inside this palette is "minecraft:air"
     */
    fun isEmpty(): Boolean {
        return elements.size == 1 && elements[0] == defaultValue
    }

}

class BlockPalette(): Palette<BlockState>(NBTType.TAG_Compound, BlockState.AIR, BlockState::toNBT) {
    constructor(elements: NBTList<NBTCompound>): this() {
        for(b in elements) {
            this.elements += BlockState(b)
        }
    }
}
class BiomePalette(): Palette<String>(NBTType.TAG_String, Biome.UnknownBiome, { str -> NBT.String(str)}) {
    constructor(elements: NBTList<NBTString>): this() {
        elements.forEachIndexed { index, b ->
            this.elements += b.value
        }
    }
}