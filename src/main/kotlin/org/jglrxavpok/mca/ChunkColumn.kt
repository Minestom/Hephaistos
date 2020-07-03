package org.jglrxavpok.mca

import org.jglrxavpok.nbt.NBTCompound

// TODO: doc
class ChunkColumn(val x: Int, val z: Int) {

    var dataVersion = 0
    var generationStatus: GenerationStatus = GenerationStatus.Empty
    var lastUpdate: Long = 0L
    var inhabitedTime: Long = 0L

    /**
     * May not exist. If it does, it is 1024 ints of Biome IDs, for a 4x4x4 volume in the chunk (ie 1 number correspond
     * to the biome for a cube of 4x4x4 blocks).
     * Arranged by X, Z and then Y.
     */
    var biomes: IntArray? = null
    var motionBlockingHeightMap = Heightmap()
    var worldSurfaceHeightMap = Heightmap()
    var motionBlockingNoLeavesHeightMap: Heightmap? = null
    var worldSurfaceWorldGenHeightMap: Heightmap? = null
    var oceanFloorHeightMap: Heightmap? = null
    var oceanFloorWorldGenHeightMap: Heightmap? = null

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
        biomes = level.getIntArray("Biomes")
        val heightmaps = level.getCompound("Heightmaps") ?: missing("Heightmaps")
        motionBlockingHeightMap = Heightmap(heightmaps.getLongArray("MOTION_BLOCKING") ?: missing("MOTION_BLOCKING"))
        worldSurfaceHeightMap = Heightmap(heightmaps.getLongArray("WORLD_SURFACE") ?: missing("WORLD_SURFACE"))
        motionBlockingNoLeavesHeightMap = heightmaps.getLongArray("MOTION_BLOCKING_NO_LEAVES")?.let { Heightmap(it) }
        worldSurfaceWorldGenHeightMap = heightmaps.getLongArray("WORLD_SURFACE_WG")?.let { Heightmap(it) }
        oceanFloorHeightMap = heightmaps.getLongArray("OCEAN_FLOOR")?.let { Heightmap(it) }
        oceanFloorWorldGenHeightMap = heightmaps.getLongArray("OCEAN_FLOOR_WG")?.let { Heightmap(it) }
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