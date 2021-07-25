package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.nbt.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 16x256x16 (XYZ) area of the world. Consists of 16 ChunkSections vertically stacked.
 *
 * @param x: chunk coordinate on X axis (world absolute)
 * @param z: chunk coordinate on Z axis (world absolute)
 */
class ChunkColumn @JvmOverloads constructor(val x: Int, val z: Int, val minY: Int = 0, val maxY: Int = 255) {

    companion object {
        const val UnknownBiome = -1
    }

    /**
     * Minecraft Version this chunk comes from. You can modify it, but you should make sure your data is compatible!
     */
    var version: SupportedVersion = SupportedVersion.Latest

    /**
     * Data version this chunk is in. You can modify it, but you should make sure your data is compatible!
     */
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
    var entities: NBTList<NBTCompound> = NBT.List(NBTTypes.TAG_Compound)
    var tileEntities: NBTList<NBTCompound> = NBT.List(NBTTypes.TAG_Compound)
    var tileTicks: NBTList<NBTCompound> = NBT.List(NBTTypes.TAG_Compound)
    var liquidTicks: NBTList<NBTCompound> = NBT.List(NBTTypes.TAG_Compound)
    var structures: NBTCompound? = null
    var lights: NBTList<NBTList<NBTShort>>? = null
    var liquidsToBeTicked: NBTList<NBTList<NBTShort>>? = null
    var toBeTicked: NBTList<NBTList<NBTShort>>? = null
    var postProcessing: NBTList<NBTList<NBTShort>>? = null

    /**
     * Chunk sections of this chunk. Empty sections are non-null but have their 'empty' field set to true.
     * @see ChunkSection
     */
    val sections = ConcurrentHashMap<Byte, ChunkSection>()
    var airCarvingMask: ImmutableByteArray? = null
    var liquidCarvingMask: ImmutableByteArray? = null

    val logicalHeight = maxY - minY +1

    private val biomeArraySize = logicalHeight.blockToSection() * 4*4*4

    @Throws(AnvilException::class)
    constructor(chunkData: NBTCompound, minY: Int = 0, maxY: Int = 255): this(
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("xPos") ?: missing("xPos"),
        (chunkData.getCompound("Level") ?: missing("Level")).getInt("zPos") ?: missing("zPos"),
        minY, maxY
    ) {
        if(minY > maxY)
            throw AnvilException("minY must be <= maxY")

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
        entities = level.getList("Entities") ?: NBT.List(NBTTypes.TAG_Compound)
        tileEntities = level.getList("TileEntities") ?: NBT.List(NBTTypes.TAG_Compound)
        tileTicks = level.getList("TileTicks") ?: NBT.List(NBTTypes.TAG_Compound)
        liquidTicks = level.getList("LiquidTicks") ?: NBT.List(NBTTypes.TAG_Compound)
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
            if(version < SupportedVersion.MC_1_17_0) {
                if(sectionY !in 0..15)
                    continue
            }
            sections[sectionY] = ChunkSection(nbt, version)
        }
    }

    /**
     * Gets the section at the given section Y (basically blockY / 16) value.
     * If no section is present in the column at this position, a new one is created and added
     */
    fun getSection(sectionY: Byte): ChunkSection {
        return sections.computeIfAbsent(sectionY, ::ChunkSection)
    }

    /**
     * Sets the block state at the given position in the chunk.
     * X,Y,Z must be in chunk coordinates (ie x&z in 0..15, y in minY..maxY)
     *
     * If y lands in an empty section, the section is created and considered to be filled with air
     */
    fun setBlockState(x: Int, y: Int, z: Int, state: BlockState) {
        checkBounds(x, y, z)
        val sectionY = y.blockToSection()
        val section = getSection(sectionY)
        section[x, y.blockInsideSection(), z] = state
    }

    /**
     * Returns the block state at the given position in the chunk.
     * X,Y,Z must be in chunk coordinates (ie x&z in 0..15, y in 0..255)
     */
    fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        checkBounds(x, y, z)
        val sectionY = y.blockToSection()
        val section = getSection(sectionY)
        if(section.empty) {
            return BlockState.AIR
        }
        return section[x, y.blockInsideSection(), z]
    }

    private fun checkBounds(x: Int, y: Int, z: Int) {
        if(x !in 0..15)
            throw IllegalArgumentException("x ($x) is not in 0..15")
        if(z !in 0..15)
            throw IllegalArgumentException("z ($z) is not in 0..15")
        if(y !in minY..maxY)
            throw IllegalArgumentException("y ($y) is not in 0..255")
    }

    /**
     * Sets the biome stored inside this column at the given position
     * If biome data did not exist before calling this method, the biome array is created then filled with UnknownBiome
     */
    fun setBiome(x: Int, y: Int, z: Int, biomeID: Int) {
        checkBounds(x, y, z)
        if(biomes == null) {
            biomes = ImmutableIntArray(biomeArraySize) { if (it == x/4+(z/4)*16+(y/4)*16) biomeID else UnknownBiome }
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
    fun toNBT(): NBTCompound = NBT.Kompound {
        this["DataVersion"] = NBT.Int(dataVersion)
        this["Level"] = NBT.Kompound {
            this["xPos"] = NBT.Int(x)
            this["zPos"] = NBT.Int(z)

            this["LastUpdate"] = NBT.Long(lastUpdate)
            this["InhabitedTime"] = NBT.Long(inhabitedTime)
            this["Status"] = NBT.String(generationStatus.id)

            if(biomes != null) {
                this["Biomes"] = NBT.IntArray(biomes!!)
            }

            this["Heightmaps"] = NBT.Kompound {
                this["MOTION_BLOCKING"] = NBT.LongArray(motionBlockingHeightMap.compact(version))
                motionBlockingNoLeavesHeightMap?.let { this["MOTION_BLOCKING_NO_LEAVES"] = NBT.LongArray(it.compact(version)) }
                oceanFloorHeightMap?.let { this["OCEAN_FLOOR"] = NBT.LongArray(it.compact(version)) }
                oceanFloorWorldGenHeightMap?.let { this["OCEAN_FLOOR_WG"] = NBT.LongArray(it.compact(version)) }
                this["WORLD_SURFACE"] = NBT.LongArray(worldSurfaceHeightMap.compact(version))
                worldSurfaceWorldGenHeightMap?.let { this["WORLD_SURFACE_WG"] = NBT.LongArray(it.compact(version)) }
            }
            val sections = NBT.List(
                NBTTypes.TAG_Compound,
                this@ChunkColumn.sections.values
                    .filter { !it.empty }
                    .map { it.toNBT(version) }
            )

            this["Sections"] = sections
            this["Entities"] = entities
            this["TileEntities"] = tileEntities
            this["TileTicks"] = tileTicks
            this["LiquidTicks"] = liquidTicks
            if(structures != null) {
                this["Structures"] = structures!!
            }
            if(airCarvingMask != null || liquidCarvingMask != null) {
                this["CarvingMasks"] = NBT.Kompound {
                    airCarvingMask?.let { this["AIR"] = NBT.ByteArray(it) }
                    liquidCarvingMask?.let { this["LIQUID"] = NBT.ByteArray(it) }
                }
            }
            if(lights != null) {
                this["Lights"] = lights!!
            }
            if(liquidsToBeTicked != null) {
                this["LiquidsToBeTicked"] = liquidsToBeTicked!!
            }
            if(toBeTicked != null) {
                this["ToBeTicked"] = toBeTicked!!
            }
            if(postProcessing != null) {
                this["PostProcessing"] = postProcessing!!
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