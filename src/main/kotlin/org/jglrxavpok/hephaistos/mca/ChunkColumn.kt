package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTShort
import org.jglrxavpok.hephaistos.nbt.NBTTypes

/**
 * 16x256x16 (XYZ) area of the world. Consists of 16 ChunkSections vertically stacked.
 *
 * @param x: chunk coordinate on X axis (world absolute)
 * @param z: chunk coordinate on Z axis (world absolute)
 */
class ChunkColumn(val x: Int, val z: Int) {

    companion object {
        @JvmField
        val UnknownBiome = -1
    }

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
    var structures: NBTCompound? = null
    var lights: NBTList<NBTList<NBTShort>>? = null
    var liquidsToBeTicked: NBTList<NBTList<NBTShort>>? = null
    var toBeTicked: NBTList<NBTList<NBTShort>>? = null
    var postProcessing: NBTList<NBTList<NBTShort>>? = null

    /**
     * Chunk sections of this chunk. Empty sections are non-null but have their 'empty' field set to true.
     * @see ChunkSection
     */
    val sections = Array<ChunkSection>(16) { y -> ChunkSection(y.toByte()) }
    var airCarvingMask: ByteArray? = null
    var liquidCarvingMask: ByteArray? = null

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
        structures = level.getCompound("Structures")

        val carvingMasks = level.getCompound("CarvingMasks")
        if(carvingMasks != null) {
            airCarvingMask = carvingMasks.getByteArray("AIR")
            liquidCarvingMask = carvingMasks.getByteArray("LIQUID")
        }
        lights = level.getList("Lights")
        liquidsToBeTicked = level.getList("LiquidsToBeTicked")
        toBeTicked = level.getList("ToBeTicked")
        postProcessing = level.getList("PostProcessing")

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

    /**
     * Sets the block state at the given position in the chunk.
     * X,Y,Z must be in chunk coordinates (ie x&z in 0..15, y in 0..255)
     *
     * If y lands in an empty section, the section is created and considered to be filled with air
     */
    fun setBlockState(x: Int, y: Int, z: Int, state: BlockState) {
        checkBounds(x, y, z)
        val sectionY = y / 16
        val section = sections[sectionY]
        section[x, y % 16, z] = state
    }

    /**
     * Returns the block state at the given position in the chunk.
     * X,Y,Z must be in chunk coordinates (ie x&z in 0..15, y in 0..255)
     */
    fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        checkBounds(x, y, z)
        val sectionY = y / 16
        val section = sections[sectionY]
        if(section.empty) {
            return BlockState.Air
        }
        return section[x, y % 16, z]
    }

    private fun checkBounds(x: Int, y: Int, z: Int) {
        if(x !in 0..15)
            throw IllegalArgumentException("x ($x) is not in 0..15")
        if(z !in 0..15)
            throw IllegalArgumentException("z ($z) is not in 0..15")
        if(y !in 0..255)
            throw IllegalArgumentException("y ($y) is not in 0..255")
    }

    /**
     * Sets the biome stored inside this column at the given position
     * If biome data did not exist before calling this method, the biome array is created then filled with UnknownBiome
     */
    fun setBiome(x: Int, y: Int, z: Int, biomeID: Int) {
        checkBounds(x, y, z)
        if(biomes == null) {
            biomes = IntArray(1024) { UnknownBiome }
        }
        val index = x/4+(z/4)*16+(y/4)*16
        biomes!![index] = biomeID
    }

    /**
     * Returns the biome stored inside this column at the given position
     * Be aware that biome data may not be present inside this column, in that case, this method returns UnknownBiome
     */
    fun getBiome(x: Int, y:Int, z: Int): Int {
        checkBounds(x, y, z)
        if(biomes == null) {
            return UnknownBiome
        }
        val index = x/4+(z/4)*16+(y/4)*16
        return biomes!![index/2]
    }

    /**
     * Converts this ChunkColumn into its NBT representation
     */
    fun toNBT(): NBTCompound {
        return NBTCompound()
                .setInt("DataVersion", dataVersion)
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
                            set("Entities", entities)
                            set("TileEntities", tileEntities)
                            set("TileTicks", tileTicks)
                            set("LiquidTicks", liquidTicks)
                            if(structures != null) {
                                set("Structures", structures!!)
                            }
                            if(airCarvingMask != null || liquidCarvingMask != null) {
                                set("CarvingMasks", NBTCompound()
                                        .apply {
                                            if(airCarvingMask != null)
                                                setByteArray("AIR", airCarvingMask!!)
                                            if(liquidCarvingMask != null)
                                                setByteArray("LIQUID", liquidCarvingMask!!)
                                        }
                                )
                            }
                            if(lights != null) {
                                set("Lights", lights!!)
                            }
                            if(liquidsToBeTicked != null) {
                                set("LiquidsToBeTicked", liquidsToBeTicked!!)
                            }
                            if(toBeTicked != null) {
                                set("ToBeTicked", toBeTicked!!)
                            }
                            if(postProcessing != null) {
                                set("PostProcessing", postProcessing!!)
                            }
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