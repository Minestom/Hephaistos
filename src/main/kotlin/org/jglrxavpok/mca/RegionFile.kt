package org.jglrxavpok.mca

import org.jglrxavpok.nbt.NBTCompound
import org.jglrxavpok.nbt.NBTReader
import java.io.*
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream

/**
 * Constructs a Region linked to the given file. Will initialize file contents if the file is not big enough.
 * That means it is valid to pass a newly created and empty file to this constructor, and RegionFile will start filling it.
 *
 * [Code based on Mojang source code](https://www.mojang.com/2012/02/new-minecraft-map-format-anvil/)
 * [also based on the Minecraft Wiki "Region File format page"](https://minecraft.gamepedia.com/Region_file_format)
 */
class RegionFile @Throws(AnvilException::class) constructor(val file: RandomAccessFile, val regionX: Int, val regionZ: Int): Closeable {

    companion object {
        private val GZipCompression: Byte = 1
        private val ZlibCompression: Byte = 2
        private val MaxEntryCount = 1024
        private val SectorSize = 4096
        private val HeaderLength = MaxEntryCount*2 * 4 // 2 4-byte field per entry

        fun createFileName(regionX: Int, regionZ: Int): String {
            return "r.$regionX.$regionZ.mca"
        }
    }

    private val locations = IntArray(MaxEntryCount)
    private val timestamps = IntArray(MaxEntryCount)
    private val freeSectors: ArrayList<Boolean>
    private val columnCache = ConcurrentHashMap<Int, ChunkColumn>()

    init {
        if(file.length() < HeaderLength) { // new file, fill in data
            // fill with 8kib of data
            repeat(HeaderLength) {
                file.writeByte(0)
            }
        }

        val missingPadding = file.length() % SectorSize
        // file is not a multiple of 4kib, add padding
        for(i in 0 until missingPadding) {
            file.write(0)
        }

        // prepare sectors
        val availableSectors = file.length() / SectorSize
        freeSectors = ArrayList(availableSectors.toInt())
        for (i in 0 until availableSectors) {
            freeSectors += true
        }
        freeSectors[0] = false // chunk offset table
        freeSectors[1] = false // timestamp table

        file.seek(0)
        // read chunk locations
        for(i in 0 until MaxEntryCount) {
            val location = file.readInt()
            locations[i] = location

            // mark already allocated sectors as taken.
            // location 0 means the chunk is *not* stored in the file
            if(location != 0 && sectorOffset(location) + sizeInSectors(location) <= freeSectors.size) {
                for (sectorIndex in 0 until sizeInSectors(location)) {
                    freeSectors[sectorIndex + sectorOffset(location)] = false
                }
            }
        }
        // read chunk timestamps
        for(i in 0 until MaxEntryCount) {
            timestamps[i] = file.readInt()
        }
    }

    /**
     * Gets the chunk inside this RegionFile. Coordinates are absolute.
     * The returned ChunkColumn is a cached column extracted from this region file.
     * Ie two calls with the same argument will return the same ChunkColumn object
     *
     * @see RegionFile.getOrCreateChunk
     * @throws AnvilException if the given coordinates are not inside the region
     * @return Can return null if the requested chunk is not present in the file
     */
    fun getChunk(x: Int, z: Int): ChunkColumn? {
        if(out(x, z)) throw AnvilException("Out of RegionFile: $x,$z (chunk)")

        if(!hasChunk(x, z)) return null

        return columnCache.computeIfAbsent(index(x.chunkInsideRegion(), z.chunkInsideRegion())) { readColumn(x.chunkInsideRegion(), z.chunkInsideRegion()) }
    }

    /**
     * Gets the chunk inside this RegionFile. Coordinates are absolute.
     * The returned ChunkColumn is a cached column extracted from this region file.
     * (If no such column exists, a new one is created, cached, and returned)
     *
     * Ie two calls with the same argument will return the same ChunkColumn object
     *
     * @see RegionFile.getChunk
     * @throws AnvilException if the given coordinates are not inside the region
     */
    fun getOrCreateChunk(x: Int, z: Int): ChunkColumn {
        if(out(x, z)) throw AnvilException("Out of RegionFile: $x,$z (chunk)")

        // already in file
        if(hasChunk(x, z)) return getChunk(x, z)!!
        // not in file, but already in memory
        val index = index(x.chunkInsideRegion(), z.chunkInsideRegion())
        if(columnCache.containsKey(index)) {
            return columnCache[index]!!
        }

        // neither in file nor memory, create a new column
        val column = ChunkColumn(x, z)
        columnCache[index(x, z)] = column
        return column
    }

    private fun readColumn(x: Int, z: Int): ChunkColumn {
        val offset = fileOffset(x, z)
        file.seek(offset.toLong())
        val length = file.readInt()
        val compressionType = file.readByte()
        val rawData = ByteArray(length-1)
        file.read(rawData)

        val reader = NBTReader(when(compressionType) {
            GZipCompression -> BufferedInputStream(GZIPInputStream(ByteArrayInputStream(rawData)))
            ZlibCompression -> BufferedInputStream(InflaterInputStream(ByteArrayInputStream(rawData)))
            else -> throw AnvilException("Invalid compression type: $compressionType (only 1 and 2 known)")
        }, false /* decompression performed by GZIPInputStream or InflaterInputStream */)

        val chunkData = reader.read()
        reader.close()
        if(chunkData !is NBTCompound) {
            throw AnvilException("Chunk root tag must be TAG_Compound")
        }

        return ChunkColumn(chunkData)
    }

    /**
     * Does this file contain the given chunk column? If 'false' is returned, it is still possible that the column is in
     * memory but not on disk.
     * Coordinates are absolute.
     *
     * @see hasLoadedChunk
     */
    fun hasChunk(x: Int, z: Int): Boolean {
        if(out(x, z)) throw AnvilException("Out of RegionFile: $x,$z (chunk)")

        return locations[index(x.chunkInsideRegion(), z.chunkInsideRegion())] != 0
    }

    /**
     * Does this region contain the given chunk column? Contrary to `hasChunk`, this method also checks cached chunks in
     * memory (created by `getOrCreateChunk`)
     *
     * @see hasChunk
     * @see getOrCreateChunk
     */
    fun hasLoadedChunk(x: Int, z: Int): Boolean {
        if(hasChunk(x, z)) return true

        return columnCache.containsKey(index(x.chunkInsideRegion(), z.chunkInsideRegion()))
    }

    // even if inlining will not be that beneficial thanks to JIT, these functions would be called very frequently if
    // this lib is used to load chunks in real time.
    // better save off a few cycles when possible
    @Suppress("NOTHING_TO_INLINE") private inline fun out(x: Int, z: Int) = x.chunkToRegion() != regionX || z.chunkToRegion() != regionZ
    @Suppress("NOTHING_TO_INLINE") private inline fun sizeInSectors(location: Int) = (location and 0xFF)
    @Suppress("NOTHING_TO_INLINE") private inline fun sectorOffset(location: Int) = location shr 8
    @Suppress("NOTHING_TO_INLINE") private inline fun index(chunkX: Int, chunkZ: Int) = (chunkX and 31) + (chunkZ and 31) * 32
    @Suppress("NOTHING_TO_INLINE") private inline fun fileOffset(chunkX: Int, chunkZ: Int) = sectorOffset(locations[index(chunkX, chunkZ)]) * SectorSize

    @Throws(IOException::class)
    override fun close() {
        file.close()
    }
}