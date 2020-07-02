package org.jglrxavpok.mca

import org.jglrxavpok.nbt.NBTCompound
import java.lang.IllegalArgumentException

// TODO: doc
class ChunkColumn(val x: Int, val z: Int) {

    var dataVersion = 0
    var generationStatus: GenerationStatus = GenerationStatus.Empty

    private companion object {
        private fun missing(name: String): Nothing = throw AnvilException("Missing field named '$name' (or of wrong type)")
    }

    @Throws(AnvilException::class)
    constructor(chunkData: NBTCompound): this(
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("xPos") ?: missing("xPos"),
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("zPos") ?: missing("zPos")
    ) {
        dataVersion = chunkData.getInt("DataVersion") ?: missing("DataVersion")
        val level = chunkData.getCompound("Level") ?: missing("Level")
        generationStatus = GenerationStatus.fromID(level.getString("Status") ?: missing("Status"))
    }

    enum class GenerationStatus(val id: String) {
        Empty("empty"),
        StructureStarts("structure_starts"),
        StructureReferences("structure_references"),
        Biomes("biomes"),
        Noise("noise"),
        Surface("surface"),
        Carvers("carvers"),
        LiquidCarvers("liquid_carvers"),
        Features("features"),
        Light("light"),
        Spawn("spawn"),
        Heightmaps("heightmaps"),
        Full("full");

        companion object {
            @JvmStatic
            fun fromID(id: String): GenerationStatus {
                return values().firstOrNull() { it.id == id } ?: throw IllegalArgumentException("Invalid id: $id")
            }
        }
    }
}