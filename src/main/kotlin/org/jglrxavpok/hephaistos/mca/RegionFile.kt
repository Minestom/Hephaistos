package org.jglrxavpok.hephaistos.mca

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTReader
import org.jglrxavpok.hephaistos.nbt.NBTWriter
import java.io.*
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream
import kotlin.math.ceil

/**
 * Constructs a Region linked to the given file. Will initialize file contents if the file is not big enough.
 * That means it is valid to pass a newly created and empty file to this constructor, and RegionFile will start filling it.
 *
 * The RandomAccessFile is ALWAYS sought to the beginning of the file during construction.
 *
 * [Code based on Mojang source code](https://www.mojang.com/2012/02/new-minecraft-map-format-anvil/)
 * [also based on the Minecraft Wiki "Region File format page"](https://minecraft.gamepedia.com/Region_file_format)
 */
class RegionFile @Throws(AnvilException::class, IOException::class) constructor(val file: RandomAccessFile, val regionX: Int, val regionZ: Int): Closeable {

    companion object {
        private val GZipCompression: Byte = 1
        private val ZlibCompression: Byte = 2
        private val MaxEntryCount = 1024
        private val SectorSize = 4096
        private val Sector1MB = 1024*1024 / SectorSize
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
        file.seek(0L)
        if(file.length() < HeaderLength) { // new file, fill in data
            // fill with 8kib of data
            repeat(HeaderLength) {
                file.writeByte(0)
            }
        }

        addPadding()

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
    @Throws(AnvilException::class, IOException::class)
    fun getChunk(x: Int, z: Int): ChunkColumn? {
        if(out(x, z)) throw AnvilException("Out of RegionFile: $x,$z (chunk)")

        if(!hasLoadedChunk(x, z)) return null

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
    @Throws(AnvilException::class, IOException::class)
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

    @Throws(AnvilException::class, IOException::class)
    private fun readColumn(x: Int, z: Int): ChunkColumn {
        val offset = fileOffset(x, z)
        val length = readInt(offset.toLong())
        val compressionType = readByte(offset+4L)
        val rawData = ByteArray(length-1)
        readBytes(offset+5L, rawData)

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
     * Writes a column to the file. The X,Z coordinates are based on the ones inside the given column.
     * The column does not have to be loaded from this file, but not doing so may generate inconsistencies in the world
     * (ie chunks that look out of place). That means that "handcrafting" a ChunkColumn from one
     * one instanced on their own, and passing it to writeColumn *is* valid.
     */
    @Throws(IOException::class)
    fun writeColumn(column: ChunkColumn) {
        val x = column.x
        val z = column.z
        if(out(x, z)) throw AnvilException("Out of RegionFile: $x,$z (chunk)")

        val nbt = column.toNBT()
        val dataOut = ByteArrayOutputStream()
        NBTWriter(DeflaterOutputStream(dataOut), compressed = false /* compressed by the DeflaterOutputStream */).use {
            it.writeNamed("", nbt)
        }
        val dataSize = dataOut.size()
        val sectorCount = ceil(dataSize.toDouble() / SectorSize).toInt()
        if(sectorCount >= Sector1MB) {
            throw AnvilException("Sorry, but your ChunkColumn totals over 1MB of data, impossible to save it inside a RegionFile.")
        }

        val previousSectorCount = sizeInSectors(locations[index(x, z)])
        val previousSectorStart = sectorOffset(locations[index(x, z)])
        // start by saving to free sectors, before cleaning up the old data
        var appendToEnd = false
        var position: Long
        var sectorStart: Int
        synchronized(freeSectors) {
            sectorStart = findAvailableSectors(sectorCount)
            if (sectorStart == -1) { // we need to allocate sectors
                val eof = file.length()
                position = eof
                sectorStart = (eof / SectorSize).toInt()
                // fill up sectors
                for (i in 0 until sectorCount) {
                    writeBytes(eof+i*SectorSize, ByteArray(SectorSize) {0})
                }
                appendToEnd = true
            } else {
                position = (sectorStart * SectorSize).toLong()
            }
            for (i in sectorStart until sectorStart + sectorCount) {
                if (i < freeSectors.size) {
                    freeSectors[i] = false
                } else {
                    freeSectors += false // increase size of freeSectors
                }
            }
        }

        val location = index(column.x, column.z)
        writeInt(position, dataSize)
        writeByte(position+4, ZlibCompression)
        writeBytes(position+5, dataOut.toByteArray())
        if(appendToEnd) { // we are at the EOF, we may have to add some padding
            addPadding()
        }
        locations[location] = buildLocation(sectorStart, sectorCount)
        writeLocation(column.x, column.z)
        timestamps[location] = System.currentTimeMillis().toInt()
        writeTimestamp(column.x, column.z)

        synchronized(freeSectors) {
            // the data has been written, now free previous storage
            for (i in previousSectorStart until previousSectorStart+previousSectorCount) {
                freeSectors[i] = true
            }
        }
    }

    private fun addPadding() {
        synchronized(file) {
            val missingPadding = file.length() % SectorSize
            // file is not a multiple of 4kib, add padding
            if(missingPadding > 0) {
                val pos = file.length()
                for(i in 0 until SectorSize-missingPadding) {
                    writeByte(pos+i, 0)
                }
            }
        }
    }

    /**
     * Writes the chunk data location for a given chunk inside the file
     */
    private fun writeLocation(x: Int, z: Int) {
        writeInt(index(x, z) * 4L, locations[index(x, z)])
    }

    private fun writeByte(pos: Long, b: Byte) {
        file.channel.write(ByteBuffer.allocateDirect(1).put(0, b), pos)
    }

    private fun writeBytes(pos: Long, bytes: ByteArray) {
        file.channel.write(ByteBuffer.wrap(bytes), pos)
    }

    private fun writeInt(pos: Long, int: Int) {
        file.channel.write(ByteBuffer.allocateDirect(4).putInt(0, int), pos)
    }

    private fun readBytes(pos: Long, destination: ByteArray) {
        val buf = ByteBuffer.allocateDirect(destination.size)
        file.channel.read(buf, pos)
        for (i in destination.indices) destination[i] = buf[i]
    }

    private fun readByte(pos: Long): Byte {
        val buf = ByteBuffer.allocateDirect(1)
        file.channel.read(buf, pos)
        return buf[0]
    }

    private fun readInt(pos: Long): Int {
        val buf = ByteBuffer.allocateDirect(4)
        file.channel.read(buf, pos)
        return buf.getInt(0)
    }

    /**
     * Writes the chunk timestamp for a given chunk inside the file
     */
    private fun writeTimestamp(x: Int, z: Int) {
        writeInt(index(x, z) * 4L + 4096, timestamps[index(x, z)])
    }

    /**
     * Finds the first location in freeSectors which has sectorCount consecutive free sectors. If none can be found in the file, returns -1. That means allocations will have to take place
     */
    private fun findAvailableSectors(sectorCount: Int): Int {
        for (start in 0 until freeSectors.size-sectorCount) {
            var found = true
            for(i in 0 until sectorCount) {
                if(!freeSectors[i+start]) {
                    found = false
                    break
                }
            }
            if(found)
                return start
        }
        return -1
    }

    /**
     * Does this file contain the given chunk column? If 'false' is returned, it is still possible that the column is in
     * memory but not on disk.
     * Coordinates are absolute.
     *
     * @see hasLoadedChunk
     */
    @Throws(AnvilException::class)
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
    @Throws(AnvilException::class)
    fun hasLoadedChunk(x: Int, z: Int): Boolean {
        if(hasChunk(x, z)) return true

        return columnCache.containsKey(index(x.chunkInsideRegion(), z.chunkInsideRegion()))
    }

    /**
     * Sets the block state at the given position.
     * Creates any necessary chunk or section.
     * Will NOT save the results to disk, use flushCachedChunks or writeColumn
     *
     * X,Y,Z are in absolute coordinates
     *
     * @throws IllegalArgumentException if x,y,z is not a valid position inside this region
     */
    @Throws(AnvilException::class, IllegalArgumentException::class)
    fun setBlockState(x: Int, y: Int, z: Int, blockState: BlockState) {
        if(out(x.blockToChunk(), z.blockToChunk())) throw IllegalArgumentException("Out of region $x;$z (block)")
        if(y !in 0..255) throw IllegalArgumentException("y ($y) must be in 0..255")

        val chunk = getOrCreateChunk(x.blockToChunk(), z.blockToChunk())
        chunk.setBlockState(x.blockInsideChunk(), y, z.blockInsideChunk(), blockState)
    }

    /**
     * Returns the block state present at the given position.
     *
     * Does not create any necessary chunk or section. Will throw AnvilException if the chunk does not exist (an empty section is considered full of air)
     *
     * X,Y,Z are in absolute coordinates
     *
     * @throws IllegalArgumentException if x,y,z is not a valid position inside this region
     * @throws AnvilException if the chunk corresponding to x,z does not exist in this region (ie not loaded by the game, nor created and waiting for saving with this lib)
     */
    @Throws(AnvilException::class, IllegalArgumentException::class)
    fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        if(out(x.blockToChunk(), z.blockToChunk())) throw IllegalArgumentException("Out of region $x;$z (block)")
        if(y !in 0..255) throw IllegalArgumentException("y ($y) must be in 0..255")

        val chunk = getChunk(x.blockToChunk(), z.blockToChunk()) ?: throw AnvilException("No chunk at $x,$y,$z")
        return chunk.getBlockState(x.blockInsideChunk(), y, z.blockInsideChunk())
    }

    /**
     * Sets the biome at the given position.
     * Creates any necessary chunk or section.
     * Will NOT save the results to disk, use flushCachedChunks or writeColumn
     *
     * X,Y,Z are in absolute coordinates
     *
     * @throws IllegalArgumentException if x,y,z is not a valid position inside this region
     */
    @Throws(AnvilException::class, IllegalArgumentException::class)
    fun setBiome(x: Int, y: Int, z: Int, biomeID: Int) {
        if(out(x.blockToChunk(), z.blockToChunk())) throw IllegalArgumentException("Out of region $x;$z (block)")
        if(y !in 0..255) throw IllegalArgumentException("y ($y) must be in 0..255")

        val chunk = getOrCreateChunk(x.blockToChunk(), z.blockToChunk())
        chunk.setBiome(x.blockInsideChunk(), y, z.blockInsideChunk(), biomeID)
    }

    /**
     * Returns the biome present at the given position.
     *
     * Does not create any necessary chunk or section. Will throw AnvilException if the chunk does not exist (an empty section is considered full of air)
     * As biome data might not be present inside the file, this method may return -1 for unknown biomes (ie biomes not in the file).
     *
     * X,Y,Z are in absolute coordinates
     *
     * @throws IllegalArgumentException if x,y,z is not a valid position inside this region
     * @throws AnvilException if the chunk corresponding to x,z does not exist in this region (ie not loaded by the game, nor created and waiting for saving with this lib)
     */
    @Throws(AnvilException::class, IllegalArgumentException::class)
    fun getBiome(x: Int, y: Int, z: Int): Int {
        if(out(x.blockToChunk(), z.blockToChunk())) throw IllegalArgumentException("Out of region $x;$z (block)")
        if(y !in 0..255) throw IllegalArgumentException("y ($y) must be in 0..255")

        val chunk = getChunk(x.blockToChunk(), z.blockToChunk()) ?: throw AnvilException("No chunk at $x,$y,$z")
        return chunk.getBiome(x.blockInsideChunk(), y, z.blockInsideChunk())
    }

    /**
     * Writes all cached chunks to the disk, empties the cache.
     *
     * Useful when writing data directly from RegionFile via setBlockState for instance
     */
    @Throws(IOException::class)
    fun flushCachedChunks() {
        synchronized(columnCache) {
            columnCache.values.parallelStream().forEach {
                writeColumn(it)
            }
            columnCache.clear()
        }
    }

    // even if inlining will not be that beneficial thanks to JIT, these functions would be called very frequently if
    // this lib is used to load chunks in real time.
    // better save off a few cycles when possible
    @Suppress("NOTHING_TO_INLINE") private inline fun out(x: Int, z: Int) = x.chunkToRegion() != regionX || z.chunkToRegion() != regionZ
    @Suppress("NOTHING_TO_INLINE") private inline fun sizeInSectors(location: Int) = (location and 0xFF)
    @Suppress("NOTHING_TO_INLINE") private inline fun sectorOffset(location: Int) = location shr 8
    @Suppress("NOTHING_TO_INLINE") private inline fun index(chunkX: Int, chunkZ: Int) = (chunkX.chunkInsideRegion() and 31) + (chunkZ.chunkInsideRegion() and 31) * 32
    @Suppress("NOTHING_TO_INLINE") private inline fun fileOffset(chunkX: Int, chunkZ: Int) = sectorOffset(locations[index(chunkX, chunkZ)]) * SectorSize
    @Suppress("NOTHING_TO_INLINE") private inline fun buildLocation(start: Int, length: Int) = ((start shl 8) or (length and 0xFF)) and 0xFFFFFFFF.toInt()

    /**
     * Closes the given RandomAccessFile, and clears the column cache at the same time.
     * This will NOT flush columns that are in the cache but not yet savec to disk. Use flushCachedChunks
     *
     * @see flushCachedChunks
     */
    @Throws(IOException::class)
    override fun close() {
        synchronized(columnCache) {
            columnCache.clear()
        }
        file.close()
    }

    /**
     * Unloads from memory the given column. Allows to relieve the JVM memory.
     * This DOES NOT save the chunk column.
     *
     * One should no longer use the column object.
     *
     * @throws IllegalArgumentException if the column was not previously inside this region
     */
    fun forget(column: ChunkColumn) {
        val index = index(column.x, column.z)
        if(columnCache[index] == column) {
            columnCache.remove(index)
        } else {
            throw IllegalArgumentException("Tried to remove column that is not inside the region")
        }
    }
}