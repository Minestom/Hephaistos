package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.nbt.*
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

/**
 * 16x256x16 (XYZ) area of the world. Consists of 16 ChunkSections vertically stacked.
 *
 */
class ChunkColumn {

    companion object {
        const val UnknownBiome = -1

        private fun SectionName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_3) "Sections" else "sections"
        private fun EntitiesName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_3) "Entities" else "entities"
        private fun BlockEntitiesName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_3) "TileEntities" else "block_entities"
        private fun StructuresName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_3) "Structures" else "structures"
    }

    /**
     * chunk coordinate on X axis (world absolute)
     */
    var x: Int = 0
        private set

    /**
     * chunk coordinate on Z axis (world absolute)
     */
    var z: Int = 0
        private set

    /**
     * Min Y value available in this chunk (block Y)
     */
    var minY: Int = 0
        private set

    /**
     * Max Y value available in this chunk (block Y)
     */
    var maxY: Int = 255
        private set

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
    var biomes: IntArray? = null
    var motionBlockingHeightMap = Heightmap()
    var worldSurfaceHeightMap = Heightmap()
    var motionBlockingNoLeavesHeightMap: Heightmap? = null
    var worldSurfaceWorldGenHeightMap: Heightmap? = null
    var oceanFloorHeightMap: Heightmap? = null
    var oceanFloorWorldGenHeightMap: Heightmap? = null
    var entities: NBTList<NBTCompound> = NBT.List(NBTType.TAG_Compound)
    var tileEntities: NBTList<NBTCompound> = NBT.List(NBTType.TAG_Compound)
    var tileTicks: NBTList<NBTCompound> = NBT.List(NBTType.TAG_Compound)
    var liquidTicks: NBTList<NBTCompound> = NBT.List(NBTType.TAG_Compound)
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

    var lightOn = true

    val logicalHeight get()= maxY - minY +1

    private val biomeArraySize get()= logicalHeight.blockToSection() * 4*4*4

    @JvmOverloads
    constructor(x: Int, z: Int, minY: Int = 0, maxY: Int = 255) {
        this.x = x
        this.z = z
        this.minY = minY
        this.maxY = maxY
    }

    /**
     * minY and maxY are ignored for 1.18+ worlds, as the information can be deduced from the NBT data
     */
    @Throws(AnvilException::class)
    @JvmOverloads
    constructor(chunkData: NBTCompound, minY: Int = 0, maxY: Int = 255) {
        dataVersion = chunkData.getInt("DataVersion") ?: missing("DataVersion")
        version = SupportedVersion.closest(dataVersion)

        val levelData =
            when {
                version < SupportedVersion.MC_1_18_PRE_3 -> {
                    chunkData.getCompound("Level") ?: missing("Level")
                }

                else -> {
                    chunkData
                }
            }
        this.x = levelData.getInt("xPos") ?: missing("xPos")
        this.z = levelData.getInt("zPos") ?: missing("zPos")
        if(version < SupportedVersion.MC_1_18_PRE_3) {
            if(version < SupportedVersion.MC_1_17_0) {
                if(minY != 0) {
                    error("Pre 1.17 worlds do not support minY != 0")
                }
                if(maxY != 255) {
                    error("Pre 1.17 worlds do not support maxY != 255")
                }
            }
            this.minY = minY
            this.maxY = maxY
        } else {
            this.minY = (levelData.getInt("yPos") ?: missing("yPos")).sectionToBlock()

        }

        if(minY > maxY)
            throw AnvilException("minY must be <= maxY")

        lastUpdate = levelData.getLong("LastUpdate") ?: missing("LastUpdate")
        inhabitedTime = levelData.getLong("InhabitedTime") ?: missing("InhabitedTime")
        generationStatus = GenerationStatus.fromID(levelData.getString("Status") ?: missing("Status"))

        if(generationStatus.ordinal >= GenerationStatus.Heightmaps.ordinal) {
            val heightmaps = levelData.getCompound("Heightmaps") ?: missing("Heightmaps")
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
        entities = levelData.getList(EntitiesName(version)) ?: NBT.List(NBTType.TAG_Compound)
        tileEntities = levelData.getList(BlockEntitiesName(version)) ?: NBT.List(NBTType.TAG_Compound)

        TODO("1.18 support")
        tileTicks = levelData.getList("TileTicks") ?: NBT.List(NBTType.TAG_Compound)
        liquidTicks = levelData.getList("LiquidTicks") ?: NBT.List(NBTType.TAG_Compound)

        structures = levelData.getCompound(StructuresName(version))

        val carvingMasks = levelData.getCompound("CarvingMasks")
        if(carvingMasks != null) {
            airCarvingMask = carvingMasks.getByteArray("AIR")
            liquidCarvingMask = carvingMasks.getByteArray("LIQUID")
        }
        lights = levelData.getList("Lights")

        if(version < SupportedVersion.MC_1_18_PRE_3) {
            liquidsToBeTicked = levelData.getList("LiquidsToBeTicked")
            toBeTicked = levelData.getList("ToBeTicked")
        } else {
            TODO("1.18 support")
            lightOn = levelData.getBoolean("isLightOn") ?: missing("isLightOn")
        }

        postProcessing = levelData.getList("PostProcessing")

        val sectionsNBT = levelData.getList<NBTCompound>(SectionName(version)) ?: missing(SectionName(version))
        for(nbt in sectionsNBT) {
            val sectionY = nbt.getByte("Y") ?: missing("Y")
            if(version < SupportedVersion.MC_1_17_0) {
                if(sectionY !in 0..15)
                    continue
            }
            sections[sectionY] = ChunkSection(nbt, version)
            if(version >= SupportedVersion.MC_1_18_PRE_3) {
                this.maxY = maxOf(this.maxY, sectionY.toInt().sectionToBlock())
            }
        }

        TODO("<1.17 support")
        if(version < SupportedVersion.MC_1_18_PRE_3) {
            val biomes = levelData.getIntArray("Biomes")
            if(biomes != null) {
                TODO("Load non-palette biomes")
            }
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
            biomes = IntArray(biomeArraySize) { UnknownBiome }
        }
        biomes?.set(x/4+(z/4)*4+(y/4)*16, biomeID)
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
        val index = x/4+(z/4)*4+(y/4)*16
        return biomes!![index]
    }

    /**
     * Converts this ChunkColumn into its NBT representation.
     * Not passing a SupportedVersion parameter will make the chunk write to its format version.
     * It is *NOT* allowed to give a version lower than this chunk's, because new data cannot be converted back to old formats
     */
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun toNBT(version: SupportedVersion = this.version): NBTCompound = NBT.Kompound {
        if(version < this@ChunkColumn.version) {
            throw IllegalArgumentException("Version ${ version.name } is lower than chunk version (${ this@ChunkColumn.version })!")
        }
        this["DataVersion"] = NBT.Int(version.lowestDataVersion)

        val writeLevelData = { mutableCompound: MutableNBTCompound ->
            mutableCompound.apply {
                this["xPos"] = NBT.Int(x)
                this["zPos"] = NBT.Int(z)

                if(version >= SupportedVersion.MC_1_18_PRE_3) {
                    this["yPos"] = NBT.Int(minY.blockToSection().toInt())
                }

                this["LastUpdate"] = NBT.Long(lastUpdate)
                this["InhabitedTime"] = NBT.Long(inhabitedTime)
                this["Status"] = NBT.String(generationStatus.id)

                TODO("1.18 support")
                if(biomes != null) {
                    this["Biomes"] = NBT.IntArray(*biomes!!)
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
                    NBTType.TAG_Compound,
                    this@ChunkColumn.sections.values
                        .filter { !it.empty }
                        .map { it.toNBT(version) }
                )

                this[SectionName(version)] = sections
                this[EntitiesName(version)] = entities
                this[BlockEntitiesName(version)] = tileEntities

                TODO("1.18 support")
                this["TileTicks"] = tileTicks
                this["LiquidTicks"] = liquidTicks

                if(structures != null) {
                    this[StructuresName(version)] = structures!!
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

                TODO("1.18 support")
                if(liquidsToBeTicked != null) {
                    this["LiquidsToBeTicked"] = liquidsToBeTicked!!
                }
                if(toBeTicked != null) {
                    this["ToBeTicked"] = toBeTicked!!
                }

                if(postProcessing != null) {
                    this["PostProcessing"] = postProcessing!!
                }

                TODO("isLightOn")
            }
        }

        if(version < SupportedVersion.MC_1_18_PRE_3) {
            this["Level"] = NBT.Kompound { writeLevelData(this) }
        } else {
            writeLevelData(this)
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