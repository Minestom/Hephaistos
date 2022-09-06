package org.jglrxavpok.hephaistos.mca

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.mca.AnvilException.Companion.missing
import org.jglrxavpok.hephaistos.mca.readers.*
import org.jglrxavpok.hephaistos.mca.writer.ChunkWriter
import org.jglrxavpok.hephaistos.mcdata.*
import org.jglrxavpok.hephaistos.mcdata.Biome
import org.jglrxavpok.hephaistos.nbt.*

/**
 * 16x16 (XZ) vertical slice of the world. Consists of ChunkSections vertically stacked (each of height 16 blocks).
 *
 */
class ChunkColumn {

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
    val sections = Byte2ObjectOpenHashMap<ChunkSection>()
    var airCarvingMask: ImmutableByteArray? = null
    var liquidCarvingMask: ImmutableByteArray? = null

    var lightOn = true

    val logicalHeight get()= maxY - minY +1
    private val biomeArraySize get()= logicalHeight * 4 // (4x4x4 per *section*, so height x 4x4x4 / 16 = height x 4)

    @JvmOverloads
    constructor(x: Int, z: Int, minY: Int = VanillaMinY, maxY: Int = VanillaMaxY) {
        this.x = x
        this.z = z
        this.minY = minY
        this.maxY = maxY
    }

    @Suppress("UNUSED_PARAMETER")
    @Throws(AnvilException::class)
    @Deprecated(message = "MinY / MaxY is now auto-detected when loading chunks", replaceWith = ReplaceWith("Remove the MinY/MaxY arguments"))
    constructor(chunkData: NBTCompound, __minY: Int = VanillaMinY, __maxY: Int = VanillaMaxY): this(chunkData)

    /**
     * minY and maxY are ignored for 1.18+ worlds, as the information can be deduced from the NBT data
     */
    @Throws(AnvilException::class)
    constructor(chunkData: NBTCompound) {
        val chunkReader = ChunkReader(chunkData)
        dataVersion = chunkReader.getDataVersion()
        version = chunkReader.getMinecraftVersion()

        this.x = chunkReader.getChunkX()
        this.z = chunkReader.getChunkZ()

        val sectionsNBT = chunkReader.getSections()
        val yRange = chunkReader.getYRange()
        minY = yRange.first
        maxY = yRange.last

        if(minY > maxY)
            throw AnvilException("minY must be <= maxY")

        lastUpdate = chunkReader.getLastUpdate()
        inhabitedTime = chunkReader.getInhabitedTime()
        generationStatus = chunkReader.getGenerationStatus()

        if(generationStatus.ordinal >= GenerationStatus.Heightmaps.ordinal) {
            if(chunkReader.hasHeightmaps()) {
                motionBlockingHeightMap = Heightmap(chunkReader.getMotionBlockingHeightmap() ?: missing("HeightMaps/MOTION_BLOCKING"), version)
                worldSurfaceHeightMap = Heightmap(chunkReader.getWorldSurfaceHeightmap() ?: missing("HeightMaps/WORLD_SURFACE"), version)
                motionBlockingNoLeavesHeightMap = chunkReader.getMotionBlockingNoLeavesHeightmap()?.let { Heightmap(it, version) }
                worldSurfaceWorldGenHeightMap = chunkReader.getWorldSurfaceWorldGenHeightmap()?.let { Heightmap(it, version) }
                oceanFloorHeightMap = chunkReader.getOceanFloorHeightmap()?.let { Heightmap(it, version) }
                oceanFloorWorldGenHeightMap = chunkReader.getOceanFloorWorldGenHeightmap()?.let { Heightmap(it, version) }
            }
        } else {
            // chunk is under construction, generate empty heightmaps
            motionBlockingHeightMap = Heightmap()
            worldSurfaceHeightMap = Heightmap()
        }

        // we allow empty lists for these
        entities = chunkReader.getOldEntities()
        tileEntities = chunkReader.getTileEntities()

        tileTicks = chunkReader.getTileTicks()
        liquidTicks = chunkReader.getLiquidTicks()

        structures = chunkReader.getStructures()

        airCarvingMask = chunkReader.getAirCarvingMask()
        liquidCarvingMask = chunkReader.getLiquidCarvingMask()
        lights = chunkReader.getLights()

        @Suppress("DEPRECATION")
        if(version < SupportedVersion.MC_1_18_PRE_4) {
            liquidsToBeTicked = chunkReader.getOldLiquidsToBeTicked()
            toBeTicked = chunkReader.getOldToBeTicked()
        }
        lightOn = chunkReader.isLightOn()

        postProcessing = chunkReader.getPostProcessing()

        for(nbt in sectionsNBT) {
            val sectionY = nbt.getByte("Y") ?: missing("Y")
            if(version < SupportedVersion.MC_1_17_0) {
                if(sectionY !in 0..15)
                    continue
            }
            sections[sectionY] = ChunkSection(nbt, version)
        }

        if(version < SupportedVersion.MC_1_18_PRE_4) {
            val biomes = chunkReader.getOldBiomes()
            if(biomes != null) {
                val biomeNamespaces = biomes.map(Biome::numericalIDToNamespaceID).toTypedArray()
                for (sectionY in minY.blockToSection() .. maxY.blockToSection()) {
                    val offset = (sectionY - minY.blockToSection()) * 4 * 4 * 4
                    val section = getSection(sectionY.toByte())
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
        return sections.getOrPut(sectionY) { ChunkSection(sectionY) }
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
    fun getBiome(x: Int, y: Int, z: Int): String {
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
    fun toNBT(version: SupportedVersion = this.version): NBTCompound = ChunkWriter(version).apply {
        if(version < SupportedVersion.MC_1_17_0) {
            if(minY != 0 || maxY != 255)
                throw IllegalArgumentException("Versions prior to 1.17 do not support chunks with Y outside of 0-255 range. Current is $minY - ${maxY}")
        }

        setCoordinates(x, z)
        setLastUpdate(lastUpdate)
        setInhabitedTime(inhabitedTime)
        setStatus(generationStatus)

        if(version >= SupportedVersion.MC_1_18_PRE_4) {
            setYPos(minY)
            for (sectionY in minY.blockToSection() .. maxY.blockToSection()) {
                getSection(sectionY.toByte()) // 1.18+ always saves all sections to know the chunk height
            }
        } else {
            var biomes: IntArray? = null
            for(section in sections.values) {
                if(section.hasBiomeData()) {
                    if(biomes == null) {
                        biomes = IntArray(biomeArraySize)
                    }

                    val offset = (section.y - minY.blockToSection()) * 4 * 4 *4
                    section.biomes!!.forEachIndexed { index, id ->
                        val oldBiome = Biome.fromNamespaceID(id)
                        biomes[offset + index] = oldBiome.numericalID
                    }
                }
            }
            if(biomes != null) {
                setOldBiomes(ImmutableIntArray(*biomes))
            } else {
                setOldBiomes(ImmutableIntArray(biomeArraySize) { Biome.TheVoid.numericalID })
            }
        }

        setMotionBlockingHeightMap(NBT.LongArray(motionBlockingHeightMap.compact(version)))
        motionBlockingNoLeavesHeightMap?.let { setMotionBlockingNoLeavesHeightMap(NBT.LongArray(it.compact(version))) }
        oceanFloorHeightMap?.let { setOceanFloorHeightMap(NBT.LongArray(it.compact(version))) }
        oceanFloorWorldGenHeightMap?.let { setOceanFloorWorldGenHeightMap(NBT.LongArray(it.compact(version))) }
        setWorldSurfaceHeightMap(NBT.LongArray(worldSurfaceHeightMap.compact(version)))
        worldSurfaceWorldGenHeightMap?.let { setWorldSurfaceWorldGenHeightMap(NBT.LongArray(it.compact(version))) }

        val allSections: MutableList<NBTCompound> = this@ChunkColumn.sections.values
            .filter { version >= SupportedVersion.MC_1_18_PRE_4 || !it.empty }
            .map { it.toNBT(version) }
            .toMutableList()

        if(version < SupportedVersion.MC_1_18_PRE_4) {
            allSections += ChunkSection((minY.blockToSection()-1).toByte()).toNBT(version)
        }
        val sections = NBT.List(
            NBTType.TAG_Compound,
            allSections
        )

        setSectionsData(sections)
        setOldEntityData(entities)
        setBlockEntityData(tileEntities)

        setBlockTicks(tileTicks)
        setFluidTicks(liquidTicks)

        if(structures != null) {
            setStructures(structures!!)
        }
        if(airCarvingMask != null) {
            setAirCarvingMask(airCarvingMask!!)
        }
        if(liquidCarvingMask != null) {
            setLiquidCarvingMask(liquidCarvingMask!!)
        }
        if(lights != null) {
            setLights(lights!!)
        }

        @Suppress("DEPRECATION")
        if(version < SupportedVersion.MC_1_18_PRE_4) {
            if(liquidsToBeTicked != null) {
                setOldLiquidsToBeTicked(liquidsToBeTicked!!)
            }
            if(toBeTicked != null) {
                setOldToBeTicked(toBeTicked!!)
            }
        } else {
            setLightOn(lightOn)
        }

        if(postProcessing != null) {
            setPostProcessing(postProcessing!!)
        }

    }.toNBT()

    /**
     * Updates this chunk version, both the 'version' field and the DataVersion
     */
    fun changeVersion(version: SupportedVersion) {
        this.version = version
        this.dataVersion = version.lowestDataVersion
    }

    /**
     * Changes the Y range of this chunk
     */
    fun setYRange(minY: Int, maxY: Int) {
        if(version < SupportedVersion.MC_1_17_0) {
            throw IllegalArgumentException("Versions prior to 1.17 do not support chunks with Y outside of 0-255 range.")
        }
        if(minY >= maxY) {
            throw IllegalArgumentException("minY ($minY) must be < maxY ($maxY)")
        }
        this.minY = minY
        this.maxY = maxY
        for (sectionY in minY.blockToSection() .. maxY.blockToSection()) {
            getSection(sectionY.toByte()) // make sure section exists
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