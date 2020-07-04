package org.jglrxavpok.mca

import org.jglrxavpok.mca.AnvilException.Companion.missing
import org.jglrxavpok.nbt.NBTCompound
import org.jglrxavpok.nbt.NBTList
import org.jglrxavpok.nbt.NBTTypes

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
    var entities: NBTList<NBTCompound> = NBTList(NBTTypes.TAG_Compound)
    var tileEntities: NBTList<NBTCompound> = NBTList(NBTTypes.TAG_Compound)
    var tileTicks: NBTList<NBTCompound> = NBTList(NBTTypes.TAG_Compound)
    var liquidTicks: NBTList<NBTCompound> = NBTList(NBTTypes.TAG_Compound)

    /**
     * Chunk sections of this chunk. Empty sections are non-null but have their 'empty' field set to true.
     * @see ChunkSection
     */
    val sections = Array<ChunkSection>(16) { y -> ChunkSection(y.toByte()) }

    @Throws(AnvilException::class)
    constructor(chunkData: NBTCompound): this(
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("xPos") ?: missing("xPos"),
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("zPos") ?: missing("zPos")
    ) {
        dataVersion = chunkData.getInt("DataVersion") ?: missing("DataVersion")
        val level = chunkData.getCompound("Level") ?: missing("Level")
        lastUpdate = level.getLong("LastUpdate") ?: missing("LastUpdate")
        inhabitedTime = level.getLong("InhabitedTime") ?: missing("InhabitedTime")
        generationStatus = GenerationStatus.fromID(level.getString("Status") ?: missing("Status"))
        biomes = level.getIntArray("Biomes")
        val heightmaps = level.getCompound("Heightmaps") ?: missing("Heightmaps")
        motionBlockingHeightMap = Heightmap(heightmaps.getLongArray("MOTION_BLOCKING") ?: missing("MOTION_BLOCKING"))
        worldSurfaceHeightMap = Heightmap(heightmaps.getLongArray("WORLD_SURFACE") ?: missing("WORLD_SURFACE"))
        motionBlockingNoLeavesHeightMap = heightmaps.getLongArray("MOTION_BLOCKING_NO_LEAVES")?.let { Heightmap(it) }
        worldSurfaceWorldGenHeightMap = heightmaps.getLongArray("WORLD_SURFACE_WG")?.let { Heightmap(it) }
        oceanFloorHeightMap = heightmaps.getLongArray("OCEAN_FLOOR")?.let { Heightmap(it) }
        oceanFloorWorldGenHeightMap = heightmaps.getLongArray("OCEAN_FLOOR_WG")?.let { Heightmap(it) }

        // we allow empty lists for these
        entities = level.getList("Entities") ?: NBTList<NBTCompound>(NBTTypes.TAG_Compound)
        tileEntities = level.getList("TileEntities") ?: NBTList<NBTCompound>(NBTTypes.TAG_Compound)
        tileTicks = level.getList("TileTicks") ?: NBTList<NBTCompound>(NBTTypes.TAG_Compound)
        liquidTicks = level.getList("LiquidTicks") ?: NBTList<NBTCompound>(NBTTypes.TAG_Compound)

        // TODO: Lights, ToBeTicked, PostProcessing, Structures

        val sectionsNBT = level.getList<NBTCompound>("Sections") ?: missing("Sections")
        for(nbt in sectionsNBT) {
            val sectionY = nbt.getByte("Y") ?: missing("Y")
            if(sectionY == (-1).toByte()) { // mark that they are empty sections?
                continue
            }
            if(sectionY !in 0..15) {
                throw AnvilException("Invalid Y value for section: $sectionY. Must be in 0..15")
            }
            sections[sectionY.toInt()] = ChunkSection(nbt)
        }
    }

    fun setBlockState(x: Int, y: Int, z: Int, state: BlockState) {
        // TODO: check bounds
        val sectionY = y / 16
        val section = sections[sectionY]
        section[x, y % 16, z] = state
    }

    fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        // TODO: check bounds
        val sectionY = y / 16
        val section = sections[sectionY]
        if(section.empty) {
            return BlockState.Air
        }
        return section[x, y % 16, z]
    }

    fun toNBT(): NBTCompound {
        return NBTCompound()
                .setInt("dataVersion", dataVersion)
                .set("Level",
                        NBTCompound().apply {
                            setInt("xPos", x)
                            setInt("zPos", z)
                            setLong("LastUpdate", lastUpdate)
                            setLong("InhabitedTime", inhabitedTime)
                            setString("Status", generationStatus.id)
                            if(biomes != null) {
                                setIntArray("Biomes", biomes!!)
                            }
                            set("Heightmaps", NBTCompound().apply {
                                setLongArray("MOTION_BLOCKING", motionBlockingHeightMap.compact())
                                motionBlockingNoLeavesHeightMap?.let { setLongArray("MOTION_BLOCKING_NO_LEAVES", it.compact()) }
                                oceanFloorHeightMap?.let { setLongArray("OCEAN_FLOOR", it.compact()) }
                                oceanFloorWorldGenHeightMap?.let { setLongArray("OCEAN_FLOOR_WG", it.compact()) }
                                setLongArray("WORLD_SURFACE", worldSurfaceHeightMap.compact())
                                worldSurfaceWorldGenHeightMap?.let { setLongArray("WORLD_SURFACE_WG", it.compact()) }
                            })
                            val sections = NBTList<NBTCompound>(NBTTypes.TAG_Compound)
                            for (section in this@ChunkColumn.sections) {
                                if(!section.empty) {
                                    sections += section.toNBT()
                                }
                            }
                            set("Sections", sections)
                            // TODO: Carving masks
                            // TODO: Entities, TileEntities, TileTicks, LiquidTicks, Lights, LiquidsToBeTicked, ToBeTicked, PostProcessing
                            // TODO: Structures
                        }

                )
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