package org.jglrxavpok.hephaistos.mca.readers

import org.jglrxavpok.hephaistos.mca.*
import org.jglrxavpok.hephaistos.nbt.*

/**
 * Extracts chunk data from a NBTCompound.
 * Does not cache nor convert the raw data.
 * Use when you want to read parts of a chunk without having the memory overhead of ChunkColumn.
 *
 * Only one information is cached with this object: the data version of the chunk, which is used to know how to extract the chunk data.
 *
 * Can throw AnvilException if the NBT data is not recognized or incomplete.
 *
 * For information on what the different fields extracted from the NBT are, see [Chunk Format on the Minecraft wiki](https://minecraft.fandom.com/wiki/Chunk_format)
 */
class ChunkReader @Throws(AnvilException::class) constructor(val chunkData: NBTCompound) {

    /**
     * Data version this chunk is in.
     */
    private val dataVersion: Int = chunkData.getInt("DataVersion") ?: AnvilException.missing("DataVersion")

    /**
     * Minimum Minecraft version this chunk might come from
     */
    private val minecraftVersion: SupportedVersion = SupportedVersion.closest(dataVersion)

    private val levelData get()=
        when {
            minecraftVersion < SupportedVersion.MC_1_18_PRE_4 -> {
                chunkData.getCompound("Level") ?: AnvilException.missing("Level")
            }

            else -> {
                chunkData
            }
        }

    /**
     * Returns the data version of the input chunk
     */
    fun getDataVersion(): Int {
        return dataVersion
    }

    /**
     * Returns a minimum Minecraft version this chunk might come from. Used to determine the actual format inside the NBT
     */
    fun getMinecraftVersion(): SupportedVersion {
        return minecraftVersion
    }

    fun getChunkX() = levelData.getInt("xPos") ?: AnvilException.missing("xPos")
    fun getChunkZ() = levelData.getInt("zPos") ?: AnvilException.missing("zPos")
    fun getSections() = levelData.getList<NBTCompound>(SectionName(minecraftVersion)) ?: AnvilException.missing(SectionName(minecraftVersion))

    fun getLastUpdate() = levelData.getLong("LastUpdate") ?: AnvilException.missing("LastUpdate")
    fun getInhabitedTime() = levelData.getLong("InhabitedTime") ?: AnvilException.missing("InhabitedTime")

    fun getGenerationStatus() = ChunkColumn.GenerationStatus.fromID(levelData.getString("Status") ?: AnvilException.missing("Status"))

    fun getYRange(): IntRange {
        var minY: Int
        var maxY: Int
        if(minecraftVersion < SupportedVersion.MC_1_17_0) {
            minY = 0
            maxY = 255
        } else if(minecraftVersion < SupportedVersion.MC_1_18_PRE_4) {
            var minSectionY = Byte.MAX_VALUE
            for(nbt in getSections()) {
                val sectionY = nbt.getByte("Y") ?: AnvilException.missing("Y")
                minSectionY = minOf(minSectionY, sectionY)
            }

            val biomes = levelData.getIntArray("Biomes") ?: throw AnvilException("Cannot guess minY-maxY of chunk without biome information for 1.17 worlds")

            minY = (minSectionY.toInt()+1).sectionToBlock()
            maxY = biomes.size / (4 * 4 * 4) + minY -1
        } else {
            minY = (levelData.getInt("yPos") ?: AnvilException.missing("yPos")).sectionToBlock()
            maxY = minY

            for(nbt in getSections()) {
                val sectionY = nbt.getByte("Y") ?: AnvilException.missing("Y")
                maxY = maxOf(maxY, sectionY.toInt().sectionToBlock()+15)
            }
        }

        return minY .. maxY
    }

    /**
     * Older chunks (pre 1.17) store entities inside the region/ *.mca files
     */
    fun getOldEntities() = levelData.getList(EntitiesName(minecraftVersion)) ?: NBT.List(NBTType.TAG_Compound)

    fun getTileEntities() = levelData.getList(BlockEntitiesName(minecraftVersion)) ?: NBT.List(NBTType.TAG_Compound)

    fun getTileTicks() = levelData.getList(BlockTicksName(minecraftVersion)) ?: NBT.List(NBTType.TAG_Compound)
    fun getLiquidTicks() = levelData.getList(FluidTicksName(minecraftVersion)) ?: NBT.List(NBTType.TAG_Compound)

    /**
     * Can return null if none found
     */
    fun getStructures() = levelData.getCompound(StructuresName(minecraftVersion))

    /**
     * Can return null if none found
     */
    fun getLights() = levelData.getList<NBTList<NBTShort>>("Lights")

    fun isLightOn() = levelData.getBoolean("isLightOn") ?: true

    /**
     * Can return null if none found
     */
    fun getPostProcessing() = levelData.getList<NBTList<NBTShort>>("PostProcessing")

    /**
     * Can return null if none found.
     * Represents the biomes from before 1.18. Starting from 1.18, biomes are stored per-section.
     */
    fun getOldBiomes() = levelData.getIntArray("Biomes")

    /**
     * Can return null if none found.
     * Obsolete since at least 1.18
     */
    fun getOldLiquidsToBeTicked() = levelData.getList<NBTList<NBTShort>>("LiquidsToBeTicked")

    /**
     * Can return null if none found.
     * Obsolete since at least 1.18
     */
    fun getOldToBeTicked() = levelData.getList<NBTList<NBTShort>>("ToBeTicked")

    /**
     * Can return null if none found.
     */
    fun getCarvingMasks() = levelData.getCompound("CarvingMasks")

    /**
     * Can return null if none found.
     */
    fun getAirCarvingMask() = getCarvingMasks()?.getByteArray("AIR")

    /**
     * Can return null if none found.
     */
    fun getLiquidCarvingMask() = getCarvingMasks()?.getByteArray("LIQUID")

    /**
     * Returns true iif this chunk has heightmap data
     */
    fun hasHeightmaps() = levelData.contains("Heightmaps")

    /**
     * Can return null if none found. Returns heightmap data for this chunk. Recommended using the getXHeightmap instead
     */
    fun getHeightmaps() = levelData.getCompound("Heightmaps")

    /**
     * Can return null if none found.
     */
    fun getMotionBlockingHeightmap() = getHeightmaps()?.getLongArray("MOTION_BLOCKING")

    /**
     * Can return null if none found.
     */
    fun getWorldSurfaceHeightmap() = getHeightmaps()?.getLongArray("WORLD_SURFACE")

    /**
     * Can return null if none found.
     */
    fun getMotionBlockingNoLeavesHeightmap() = getHeightmaps()?.getLongArray("MOTION_BLOCKING_NO_LEAVES")

    /**
     * Can return null if none found.
     */
    fun getWorldSurfaceWorldGenHeightmap() = getHeightmaps()?.getLongArray("WORLD_SURFACE_WG")

    /**
     * Can return null if none found.
     */
    fun getOceanFloorHeightmap() = getHeightmaps()?.getLongArray("OCEAN_FLOOR")

    /**
     * Can return null if none found.
     */
    fun getOceanFloorWorldGenHeightmap() = getHeightmaps()?.getLongArray("OCEAN_FLOOR_WG")

}