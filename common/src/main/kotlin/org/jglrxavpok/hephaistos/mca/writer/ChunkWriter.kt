package org.jglrxavpok.hephaistos.mca.writer

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray
import org.jglrxavpok.hephaistos.mca.ChunkColumn
import org.jglrxavpok.hephaistos.mca.SupportedVersion
import org.jglrxavpok.hephaistos.mca.blockToSection
import org.jglrxavpok.hephaistos.mca.readers.*
import org.jglrxavpok.hephaistos.nbt.*
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound

/**
 * Writes chunk data to a NBTCompound.
 * Allows to save chunks without having to endure the overhead of ChunkColumn (& BlockState)
 */
class ChunkWriter(val version: SupportedVersion) {
    private val nbt = MutableNBTCompound()
    private var levelData: MutableNBTCompound
    private val heightMaps = MutableNBTCompound()
    private val carvingMasks = MutableNBTCompound()

    init {
        nbt.setInt("DataVersion", version.lowestDataVersion)
        levelData = if(version < SupportedVersion.MC_1_18_PRE_4) {
            MutableNBTCompound()
        } else {
            nbt
        }
    }

    /**
     * Writes the chunk coordinates to the NBT
     */
    fun setCoordinates(x: Int, z: Int) {
        levelData["xPos"] = NBT.Int(x)
        levelData["zPos"] = NBT.Int(z)
    }

    /**
     * Sets the chunk Y starting pos. Does nothing if version < MC 1.18
     */
    fun setYPos(minY: Int) {
        if(version >= SupportedVersion.MC_1_18_PRE_4) {
            levelData["yPos"] = NBT.Int(minY.blockToSection().toInt())
        }
    }

    fun setLastUpdate(lastUpdate: Long) {
        levelData["LastUpdate"] = NBT.Long(lastUpdate)
    }

    fun setInhabitedTime(inhabitedTime: Long) {
        levelData["InhabitedTime"] = NBT.Long(inhabitedTime)
    }

    fun setStatus(generationStatus: ChunkColumn.GenerationStatus) {
        levelData["Status"] = NBT.String(generationStatus.id)
    }

    fun setMotionBlockingHeightMap(compactedHeightmap: NBTLongArray) {
        heightMaps["MOTION_BLOCKING"] = compactedHeightmap
    }

    fun setMotionBlockingNoLeavesHeightMap(compactedHeightmap: NBTLongArray) {
        heightMaps["MOTION_BLOCKING_NO_LEAVES"] = compactedHeightmap
    }

    fun setOceanFloorHeightMap(compactedHeightmap: NBTLongArray) {
        heightMaps["OCEAN_FLOOR"] = compactedHeightmap
    }

    fun setOceanFloorWorldGenHeightMap(compactedHeightmap: NBTLongArray) {
        heightMaps["OCEAN_FLOOR_WG"] = compactedHeightmap
    }

    fun setWorldSurfaceHeightMap(compactedHeightmap: NBTLongArray) {
        heightMaps["WORLD_SURFACE"] = compactedHeightmap
    }

    fun setWorldSurfaceWorldGenHeightMap(compactedHeightmap: NBTLongArray) {
        heightMaps["WORLD_SURFACE_WG"] = compactedHeightmap
    }

    fun setPostProcessing(postProcessingData: NBTList<NBTList<NBTShort>>) {
        levelData["PostProcessing"] = postProcessingData
    }

    fun setLights(lights: NBTList<NBTList<NBTShort>>) {
        levelData["Lights"] = lights
    }

    fun setLightOn(lightOn: Boolean) {
        levelData["isLightOn"] = NBT.Boolean(lightOn)
    }

    @Deprecated("toBeTicked no longer exists in 1.18+ worlds", level = DeprecationLevel.WARNING)
    fun setOldToBeTicked(toBeTicked: NBTList<NBTList<NBTShort>>) {
        levelData["ToBeTicked"] = toBeTicked
    }

    @Deprecated("liquidsToBeTicked no longer exists in 1.18+ worlds", level = DeprecationLevel.WARNING)
    fun setOldLiquidsToBeTicked(toBeTicked: NBTList<NBTList<NBTShort>>) {
        levelData["LiquidsToBeTicked"] = toBeTicked
    }

    fun setStructures(structures: NBTCompound) {
        levelData[StructuresName(version)] = structures!!
    }

    fun setSectionsData(sections: NBTList<NBTCompound>) {
        levelData[SectionName(version)] = sections
    }

    /**
     * Entity data is now is a separate entities/ folder with different region files
     */
    fun setOldEntityData(entities: NBTList<NBTCompound>) {
        levelData[EntitiesName(version)] = entities
    }

    fun setBlockEntityData(blockEntities: NBTList<NBTCompound>) {
        levelData[BlockEntitiesName(version)] = blockEntities
    }

    fun setBlockTicks(blockTicks: NBTList<NBTCompound>) {
        levelData[BlockTicksName(version)] = blockTicks
    }

    fun setFluidTicks(liquidTicks: NBTList<NBTCompound>) {
        levelData[FluidTicksName(version)] = liquidTicks
    }

    fun setAirCarvingMask(mask: ImmutableByteArray) {
        carvingMasks["AIR"] = NBT.ByteArray(mask)
    }

    fun setLiquidCarvingMask(mask: ImmutableByteArray) {
        carvingMasks["LIQUID"] = NBT.ByteArray(mask)
    }

    /**
     * Biomes should now be stored per-section (since MC 1.18). Does nothing for 1.18+ worlds
     */
    @Deprecated("Biomes are stored per-section in 1.18+ worlds", level = DeprecationLevel.WARNING)
    fun setOldBiomes(biomes: ImmutableIntArray) {
        if(version < SupportedVersion.MC_1_18_PRE_4) {
            levelData["Biomes"] = NBT.IntArray(biomes)
        }
    }

    /**
     * Exports the data written to this writer to a NBT compound.
     * The returned compound is a deep copy of the data inside this object.
     */
    fun toNBT(): NBTCompound {
        return NBT.Kompound {
            if(version < SupportedVersion.MC_1_18_PRE_4) {
                this.setAll(nbt)
                this["Level"] = NBT.Kompound {
                    setAll(levelData)
                    set("Heightmaps", heightMaps.toCompound())

                    if(!carvingMasks.isEmpty()) {
                        set("CarvingMasks", carvingMasks.toCompound())
                    }
                }
            } else {
                setAll(nbt)
                set("Heightmaps", heightMaps.toCompound())

                if(!carvingMasks.isEmpty()) {
                    set("CarvingMasks", carvingMasks.toCompound())
                }
            }
        }
    }


}