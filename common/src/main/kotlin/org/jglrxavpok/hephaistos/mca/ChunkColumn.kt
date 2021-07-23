package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.nbt.*

/**
 * 16x256x16 (XYZ) area of the world. Consists of 16 ChunkSections vertically stacked.
 *
 * @param x: chunk coordinate on X axis (world absolute)
 * @param z: chunk coordinate on Z axis (world absolute)
 */
class ChunkColumn(val x: Int, val z: Int) {

    companion object {
        const val UnknownBiome = -1
    }

    var version: SupportedVersion = SupportedVersion.Latest
    var dataVersion = version.lowestDataVersion
    var generationStatus: GenerationStatus = GenerationStatus.Empty
    var lastUpdate: Long = 0L
    var inhabitedTime: Long = 0L

    /**
     * May not exist. If it does, it is 1024 ints of Biome IDs, for a 4x4x4 volume in the chunk (ie 1 number correspond
     * to the biome for a cube of 4x4x4 blocks).
     * Arranged by X, Z and then Y.
     */
    var biomes: ImmutableIntArray? = null
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
    val sections = Array(16) { y -> ChunkSection(y.toByte()) }
    var airCarvingMask: ImmutableByteArray? = null
    var liquidCarvingMask: ImmutableByteArray? = null

    @Throws(AnvilException::class)
    constructor(chunkData: NBTCompound): this(
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("xPos") ?: missing("xPos"),
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("zPos") ?: missing("zPos")
    ) {
        dataVersion = chunkData.getInt("DataVersion") ?: missing("DataVersion")
        version = SupportedVersion.closest(dataVersion)
        val level = chunkData.getCompound("Level") ?: missing("Level")
        lastUpdate = level.getLong("LastUpdate") ?: missing("LastUpdate")
        inhabitedTime = level.getLong("InhabitedTime") ?: missing("InhabitedTime")
        generationStatus = GenerationStatus.fromID(level.getString("Status") ?: missing("Status"))
        biomes = level.getIntArray("Biomes")
        if(generationStatus.ordinal >= GenerationStatus.Heightmaps.ordinal) {
            val heightmaps = level.getCompound("Heightmaps") ?: missing("Heightmaps")
            motionBlockingHeightMap = Heightmap(heightmaps.getLongArray("MOTION_BLOCKING") ?: missing("MOTION_BLOCKING"), version)
            worldSurfaceHeightMap = Heightmap(heightmaps.getLongArray("WORLD_SURFACE") ?: missing("WORLD_SURFACE"), version)
            motionBlockingNoLeavesHeightMap = heightmaps.getLongArray("MOTION_BLOCKING_NO_LEAVES")?.let { Heightmap(it, version) }
            worldSurfaceWorldGenHeightMap = heightmaps.getLongArray("WORLD_SURFACE_WG")?.let { Heightmap(it, version) }
            oceanFloorHeightMap = heightmaps.getLongArray("OCEAN_FLOOR")?.let { Heightmap(it, version) }
            oceanFloorWorldGenHeightMap = heightmaps.getLongArray("OCEAN_FLOOR_WG")?.let { Heightmap(it, version) }
        } else {
            // chunk is under construction, generate empty heightmaps
            motionBlockingHeightMap = Heightmap()
            worldSurfaceHeightMap = Heightmap()
        }

        // we allow empty lists for these
        entities = level.getList("Entities") ?: NBTList(NBTTypes.TAG_Compound)
        tileEntities = level.getList("TileEntities") ?: NBTList(NBTTypes.TAG_Compound)
        tileTicks = level.getList("TileTicks") ?: NBTList(NBTTypes.TAG_Compound)
        liquidTicks = level.getList("LiquidTicks") ?: NBTList(NBTTypes.TAG_Compound)
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
            if(sectionY in 0..15) { // otherwise invalid Y value for section, ignore others. valid mca files from vanilla Minecraft can have Y below 0 or above 15
                sections[sectionY.toInt()] = ChunkSection(nbt, version)
            }
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
            biomes = ImmutableIntArray(1024) { if (it == x/4+(z/4)*16+(y/4)*16) biomeID else UnknownBiome }
        }
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
    fun toNBT(): NBTCompound = NBT.Compound { rootMap ->
        rootMap["DataVersion"] = NBT.Int(dataVersion)
        rootMap["Level"] = NBT.Compound { levelMap ->
            levelMap["xPos"] = NBT.Int(x)
            levelMap["zPos"] = NBT.Int(z)

            levelMap["LastUpdate"] = NBT.Long(lastUpdate)
            levelMap["InhabitedTime"] = NBT.Long(inhabitedTime)
            levelMap["Status"] = NBT.String(generationStatus.id)

            if(biomes != null) {
                levelMap["Biomes"] = NBT.IntArray(biomes!!)
            }

            levelMap["Heightmaps"] = NBT.Compound { heightMapMap ->
                heightMapMap["MOTION_BLOCKING"] = NBT.LongArray(motionBlockingHeightMap.compact(version))
                motionBlockingNoLeavesHeightMap?.let { heightMapMap["MOTION_BLOCKING_NO_LEAVES"] = NBT.LongArray(it.compact(version)) }
                oceanFloorHeightMap?.let { heightMapMap["OCEAN_FLOOR"] = NBT.LongArray(it.compact(version)) }
                oceanFloorWorldGenHeightMap?.let { heightMapMap["OCEAN_FLOOR_WG"] = NBT.LongArray(it.compact(version)) }
                heightMapMap["WORLD_SURFACE"] = NBT.LongArray(worldSurfaceHeightMap.compact(version))
                worldSurfaceWorldGenHeightMap?.let { heightMapMap["WORLD_SURFACE_WG"] = NBT.LongArray(it.compact(version)) }
            }
            val sections = NBTList<NBTCompound>(NBTTypes.TAG_Compound)
            for (section in this@ChunkColumn.sections) {
                if(!section.empty) {
                    sections += section.toNBT(version)
                }
            }
            levelMap["Sections"] = sections
            levelMap["Entities"] = entities
            levelMap["TileEntities"] = tileEntities
            levelMap["TileTicks"] = tileTicks
            levelMap["LiquidTicks"] = liquidTicks
            if(structures != null) {
                levelMap["Structures"] = structures!!
            }
            if(airCarvingMask != null || liquidCarvingMask != null) {
                levelMap["CarvingMasks"] = NBT.Compound { carvingMaskMap ->
                    airCarvingMask?.let { carvingMaskMap["AIR"] = NBT.ByteArray(it) }
                    liquidCarvingMask?.let { carvingMaskMap["LIQUID"] = NBT.ByteArray(it) }
                }
            }
            if(lights != null) {
                levelMap["Lights"] = lights!!
            }
            if(liquidsToBeTicked != null) {
                levelMap["LiquidsToBeTicked"] = liquidsToBeTicked!!
            }
            if(toBeTicked != null) {
                levelMap["ToBeTicked"] = toBeTicked!!
            }
            if(postProcessing != null) {
                levelMap["PostProcessing"] = postProcessing!!
            }
        }
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
                return values().firstOrNull { it.id == id } ?: throw IllegalArgumentException("Invalid id: $id")
            }
        }
    }
}