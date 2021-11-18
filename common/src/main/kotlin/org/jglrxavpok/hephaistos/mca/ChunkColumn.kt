package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.Options
import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.mcdata.Biome
import org.jglrxavpok.hephaistos.mcdata.*
import org.jglrxavpok.hephaistos.nbt.*
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound
import java.util.concurrent.ConcurrentHashMap

/**
 * 16x256x16 (XYZ) area of the world. Consists of 16 ChunkSections vertically stacked.
 *
 */
class ChunkColumn {

    companion object {
        private fun SectionName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "Sections" else "sections"
        private fun EntitiesName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "Entities" else "entities"
        private fun BlockEntitiesName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "TileEntities" else "block_entities"
        private fun StructuresName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "Structures" else "structures"
        private fun BlockTicksName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "TileTicks" else "block_ticks"
        private fun FluidTicksName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "LiquidTicks" else "fluid_ticks"
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
    var minY: Int = VanillaMinY
        private set

    /**
     * Max Y value available in this chunk (block Y)
     */
    var maxY: Int = VanillaMaxY
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

    @Deprecated("liquidsToBeTicked no longer exists in 1.18+ worlds", level = DeprecationLevel.WARNING)
    var liquidsToBeTicked: NBTList<NBTList<NBTShort>>? = null
    @Deprecated("toBeTicked no longer exists in 1.18+ worlds", level = DeprecationLevel.WARNING)
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
    val biomeArraySize get()= logicalHeight * 4 * 4 * 4

    @JvmOverloads
    constructor(x: Int, z: Int, minY: Int = VanillaMinY, maxY: Int = VanillaMaxY) {
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
    constructor(chunkData: NBTCompound, minY: Int = VanillaMinY, maxY: Int = VanillaMaxY) {
        dataVersion = chunkData.getInt("DataVersion") ?: missing("DataVersion")
        version = SupportedVersion.closest(dataVersion)

        val levelData =
            when {
                version < SupportedVersion.MC_1_18_PRE_4 -> {
                    chunkData.getCompound("Level") ?: missing("Level")
                }

                else -> {
                    chunkData
                }
            }
        this.x = levelData.getInt("xPos") ?: missing("xPos")
        this.z = levelData.getInt("zPos") ?: missing("zPos")
        if(version < SupportedVersion.MC_1_18_PRE_4) {
            if(Options.WarnOnPre1_17WorldsWithInvalidYRange.active && version < SupportedVersion.MC_1_17_0) {
                if(minY != 0) {
                    System.err.println("Pre 1.17 chunks do not support minY != 0")
                }
                if(maxY != 255) {
                    System.err.println("Pre 1.17 chunks do not support maxY != 255")
                }
            }
            this.minY = 0
            this.maxY = 255
        } else {
            this.minY = (levelData.getInt("yPos") ?: missing("yPos")).sectionToBlock()
            this.maxY = minY
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

        tileTicks = levelData.getList(BlockTicksName(version)) ?: NBT.List(NBTType.TAG_Compound)
        liquidTicks = levelData.getList(FluidTicksName(version)) ?: NBT.List(NBTType.TAG_Compound)

        structures = levelData.getCompound(StructuresName(version))

        val carvingMasks = levelData.getCompound("CarvingMasks")
        if(carvingMasks != null) {
            airCarvingMask = carvingMasks.getByteArray("AIR")
            liquidCarvingMask = carvingMasks.getByteArray("LIQUID")
        }
        lights = levelData.getList("Lights")

        if(version < SupportedVersion.MC_1_18_PRE_4) {
            liquidsToBeTicked = levelData.getList("LiquidsToBeTicked")
            toBeTicked = levelData.getList("ToBeTicked")
        } else {
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
            if(version >= SupportedVersion.MC_1_18_PRE_4) {
                this.maxY = maxOf(this.maxY, sectionY.toInt().sectionToBlock()+15)
            }
        }

        if(version < SupportedVersion.MC_1_18_PRE_4) {
            val biomes = levelData.getIntArray("Biomes")
            if(biomes != null) {
                val biomeNamespaces = biomes.map(Biome::numericalIDToNamespaceID).toTypedArray()
                for ((sectionY, section) in sections) {
                    val offset = sectionY * 4 * 4 * 4
                    section.biomes = Array<String>(4*4*4) { Biome.UnknownBiome }
                    biomeNamespaces.copyInto(section.biomes!!, startIndex = offset, endIndex = offset + 4 * 4 * 4)
                }
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
        section[x.blockInsideSection(), y.blockInsideSection(), z.blockInsideSection()] = state
    }

    /**
     * Returns the block state at the given position in the chunk.
     * X,Y,Z must be in chunk coordinates (ie x&z in 0..15, y in minY..maxY)
     */
    fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        checkBounds(x, y, z)
        val sectionY = y.blockToSection()
        val section = getSection(sectionY)
        if(section.empty) {
            return BlockState.AIR
        }
        return section[x.blockInsideSection(), y.blockInsideSection(), z.blockInsideSection()]
    }

    private fun checkBounds(x: Int, y: Int, z: Int) {
        if(x !in 0..15)
            throw IllegalArgumentException("x ($x) is not in 0..15")
        if(z !in 0..15)
            throw IllegalArgumentException("z ($z) is not in 0..15")
        if(y !in minY..maxY)
            throw IllegalArgumentException("y ($y) is not in $minY..$maxY")
    }

    /**
     * Sets the biome stored inside this column at the given position, looking into the corresponding section
     * If biome data did not exist before calling this method, the section's biome array is created then filled with UnknownBiome
     */
    fun setBiome(x: Int, y: Int, z: Int, biomeID: String) {
        checkBounds(x, y, z)
        val section = getSection(y.blockToSection())
        section.setBiome(x.blockInsideSection(), y.blockInsideSection(), z.blockInsideSection(), biomeID)
    }

    /**
     * Returns the biome stored inside this column at the given position, looking into the corresponding section
     * Be aware that biome data may not be present inside the section, in that case, this method returns UnknownBiome
     */
    fun getBiome(x: Int, y:Int, z: Int): String {
        checkBounds(x, y, z)
        val section = getSection(y.blockToSection())
        return section.getBiome(x.blockInsideSection(), y.blockInsideSection(), z.blockInsideSection())
    }

    /**
     * Converts this ChunkColumn into its NBT representation.
     * Not passing a SupportedVersion parameter will make the chunk write to its format version.
     * It is *NOT* allowed to give a version lower than this chunk's, because new data cannot be converted back to old formats
     */
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun toNBT(version: SupportedVersion = this.version): NBTCompound = NBT.Kompound {
        if(version < SupportedVersion.MC_1_17_0) {
            if(minY != 0 || maxY != 255)
                throw IllegalArgumentException("Versions prior to 1.17 do not support chunks with Y outside of 0-255 range. Current is $minY - ${maxY}")
        }
        this["DataVersion"] = NBT.Int(version.lowestDataVersion)

        val writeLevelData = { mutableCompound: MutableNBTCompound ->
            mutableCompound.apply {
                this["xPos"] = NBT.Int(x)
                this["zPos"] = NBT.Int(z)

                this["LastUpdate"] = NBT.Long(lastUpdate)
                this["InhabitedTime"] = NBT.Long(inhabitedTime)
                this["Status"] = NBT.String(generationStatus.id)

                if(version >= SupportedVersion.MC_1_18_PRE_4) {
                    this["yPos"] = NBT.Int(minY.blockToSection().toInt())
                    for (sectionY in minY.blockToSection() .. maxY.blockToSection()) {
                        getSection(sectionY.toByte()) // 1.18+ seems to always save all sections
                    }
                } else {
                    var biomes: IntArray? = null
                    for(section in sections.values) {
                        if(section.hasBiomeData()) {
                            if(biomes == null) {
                                biomes = IntArray(biomeArraySize)
                            }

                            val offset = section.y * 4 * 4 *4
                            section.biomes!!.forEachIndexed { index, id ->
                                val oldBiome = Biome.fromNamespaceID(id)
                                biomes[offset + index] = oldBiome.numericalID
                            }
                        }
                    }
                    if(biomes != null) {
                        this["Biomes"] = NBT.IntArray(*biomes)
                    }
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

                this[BlockTicksName(version)] = tileTicks
                this[FluidTicksName(version)] = liquidTicks

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

                if(version < SupportedVersion.MC_1_18_PRE_4) {
                    if(liquidsToBeTicked != null) {
                        this["LiquidsToBeTicked"] = liquidsToBeTicked!!
                    }
                    if(toBeTicked != null) {
                        this["ToBeTicked"] = toBeTicked!!
                    }
                } else {
                    this["isLightOn"] = NBT.Boolean(lightOn)
                }

                if(postProcessing != null) {
                    this["PostProcessing"] = postProcessing!!
                }
            }
        }

        if(version < SupportedVersion.MC_1_18_PRE_4) {
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