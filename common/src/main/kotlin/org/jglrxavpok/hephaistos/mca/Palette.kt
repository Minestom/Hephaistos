package org.jglrxavpok.hephaistos.mca

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray
import org.jglrxavpok.hephaistos.mcdata.Biome
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTString
import org.jglrxavpok.hephaistos.nbt.NBTType
import kotlin.math.ceil
import kotlin.math.log2

private val breakForPerformance = System.getProperty("hephaistos.iReallyKnowWhatImDoingTrustMe")?.toBoolean() ?: false

/**
 * Represents the palette of elements used in a chunk section. This palette allows to save space when saving to disk or transferring over network,
 * as it lowers the required number of bits used to represent an element, by remapping global IDs to local IDs, with fewer bits per entry.
 */
sealed class Palette<ElementType>(private val nbtType: NBTType<out NBT>, private val defaultValue: ElementType, private val writer: (ElementType) -> NBT) {
    val elements = arrayListOf<ElementType>()

    internal val referenceCounts = Object2IntOpenHashMap<ElementType>()

    internal fun loadReferences(states: Array<ElementType>) {
        // Somehow this is faster
        states.groupBy { it }.forEach { (element, list) ->
            if(element !in elements) {
                throw IllegalArgumentException("Tried to add a reference counter to $element which is not in this palette")
            }

            referenceCounts[element] = list.size
        }
    }

    /**
     * Increases the reference count of the given element.
     * If the element was not referenced, it is added to this palette and its reference becomes 1.
     */
    fun increaseReference(block: ElementType) {
        if(referenceCounts.addTo(block, 1) == 0) {
            elements.add(block)
            referenceCounts[block] = 1
        }
    }

    /**
     * Decreases the number of references to the given element.
     * If the reference becomes <= 0, the element is removed from this palette, and its reference becomes 0.
     *
     * We optionally don't throw an exception if the reference doesn't exist anymore as that check
     * removes an expensive calculation which significantly slows down palette functionality.
     */
    fun decreaseReference(block: ElementType) {
        if(!breakForPerformance) {
            if(!referenceCounts.containsKey(block)) {
                throw IllegalArgumentException("Tried to remove a reference counter to $block which is not in this palette")
            }
        }

        val old = referenceCounts.addTo(block, -1)
        if(old - 1 <= 0) {
            elements.remove(block)
            referenceCounts.removeInt(block)
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
        check(minimumBitSize > 0) { "Minimum bit size cannot be 0 or negative" }

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
        check(minimumBitSize > 0) { "Minimum bit size cannot be 0 or negative" }

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
class BiomePalette(): Palette<String>(NBTType.TAG_String, Biome.UnknownBiome, { str -> NBT.String(str) }) {
    constructor(elements: NBTList<NBTString>): this() {
        elements.forEach { this.elements += it.value }
    }
}