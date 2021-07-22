package mca;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.BlockState;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MCASaving {

    private RandomAccessFile file;

    @BeforeEach
    public void init() throws IOException {
        File target = new File("tmp_save_r.0.0.mca");
        target.createNewFile();
        file = new RandomAccessFile(target, "rw");
    }

    @Test
    public void loadAndSave() throws IOException, AnvilException {
        file.close(); // don't need tmp file right now

        Files.copy(Paths.get("src/test/resources/r.0.0.mca"), Paths.get("tmp_save_r.0.0.mca"), REPLACE_EXISTING);
        file = new RandomAccessFile(new File("tmp_save_r.0.0.mca"), "rw");
        RegionFile r = new RegionFile(file, 0, 0);
        ChunkColumn chunk0_0 = r.getChunk(0,0);
        assertNotNull(chunk0_0);
        r.writeColumn(chunk0_0);
    }

    @Test
    public void verySimpleCreation() throws AnvilException, IOException {
        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn newColumn = region.getOrCreateChunk(0, 0);
        assertEquals(ChunkColumn.GenerationStatus.Empty, newColumn.getGenerationStatus());

        region.writeColumn(newColumn);
    }

    @Test
    public void reuseFreeSector() throws AnvilException, IOException {
        final int sectionTotal = 10; // how many sections we want to use
        // purposely use too many to force having 8 free sectors
        file.setLength(4096 * (sectionTotal+2));
        file.seek(0);
        // ensure file is full of zeroes
        for (long i = 0; i < file.length(); i++) {
            file.write(0);
        }
        file.seek(0);

        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn column = new ChunkColumn(0,0);
        region.writeColumn(column);

        file.seek(0);
        int location = file.readInt();
        int start = (location >> 8) & 0xFFFFFF;
        int length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector

        region.writeColumn(column);

        // after a rewrite, should be moved to new sector
        file.seek(0);
        location = file.readInt();
        start = (location >> 8) & 0xFFFFFF;
        length = location & 0xFF;
        assertEquals(3, start); // 2nd free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector

        region.writeColumn(column);

        // after another rewrite, should be moved to the first sector, as it is now free
        file.seek(0);
        location = file.readInt();
        start = (location >> 8) & 0xFFFFFF;
        length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector
    }

    @Test
    public void useFreeSector() throws AnvilException, IOException {
        final int sectionTotal = 10; // how many sections we want to use
        // purposely use too many to force having 8 free sectors
        file.setLength(4096 * (sectionTotal+2));
        file.seek(0);
        // ensure file is full of zeroes
        for (long i = 0; i < file.length(); i++) {
            file.write(0);
        }
        file.seek(0);

        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn column = new ChunkColumn(0,0);
        region.writeColumn(column);

        file.seek(0);
        int location = file.readInt();
        int start = (location >> 8) & 0xFFFFFF;
        int length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector
    }

    @Test
    public void allocateNewSector() throws AnvilException, IOException {
        file.setLength(4096 * 2);
        file.seek(0);
        // ensure file is full of zeroes
        for (long i = 0; i < file.length(); i++) {
            file.write(0);
        }
        assertEquals(4096* 2, file.length()); // no free sectors
        file.seek(0);

        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn column = new ChunkColumn(0,0);
        region.writeColumn(column);

        assertEquals(4096*3, file.length()); // allocated one new sector

        file.seek(0);
        int location = file.readInt();
        int start = (location >> 8) & 0xFFFFFF;
        int length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector
    }

    @Test
    public void creationFromScratchViaRegionFile() throws AnvilException, IOException {
        {
            RegionFile region = new RegionFile(file, 0, 0);
            BlockState stone = new BlockState("minecraft:stone");
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 256; y++) {
                        region.setBlockState(x, y, z, BlockState.Air);
                        region.setBlockState(x + 16, y, z, stone);
                    }
                }
            }
            region.flushCachedChunks();
        }

        {
            RegionFile region = new RegionFile(file, 0, 0);
            BlockState stone = new BlockState("minecraft:stone");
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 256; y++) {
                        assertEquals(BlockState.Air, region.getBlockState(x, y, z));
                        assertEquals(stone, region.getBlockState(x+16, y, z));
                    }
                }
            }
        }
    }

    @Test
    public void creationFromScratchViaChunks() throws AnvilException, IOException {
        {
            RegionFile region = new RegionFile(file, 0, 0);
            ChunkColumn chunk0 = region.getOrCreateChunk(0, 0);
            ChunkColumn chunk1 = region.getOrCreateChunk(1, 0);
            fillChunk(chunk0, BlockState.Air);
            fillChunk(chunk1, new BlockState("minecraft:stone"));
            region.writeColumn(chunk0);
            region.writeColumn(chunk1);
        }

        {
            RegionFile reloaded = new RegionFile(file, 0, 0);
            ChunkColumn reloadedChunk0 = reloaded.getChunk(0, 0);
            ChunkColumn reloadedChunk1 = reloaded.getChunk(1, 0);
            assertNotNull(reloadedChunk0);
            assertNotNull(reloadedChunk1);
            verifyChunk(reloadedChunk0, BlockState.Air);
            verifyChunk(reloadedChunk1, new BlockState("minecraft:stone"));
        }
    }

    private void verifyChunk(ChunkColumn chunkColumn, BlockState state) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    assertEquals(state, chunkColumn.getBlockState(x, y, z));
                }
            }
        }
    }

    private void fillChunk(ChunkColumn chunkColumn, BlockState state) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    chunkColumn.setBlockState(x, y, z, state);
                }
            }
        }
    }

    @Test
    public void setBiomes() throws AnvilException, IOException {
        RegionFile region = new RegionFile(file, 0, 0);
        region.getOrCreateChunk(0, 0); // force create chunk
        assertEquals(ChunkColumn.UnknownBiome, region.getBiome(0,0,0));
        region.setBiome(0,0,0, 1);
        assertEquals(1, region.getBiome(0,0,0));
    }

    @Test
    public void setBlockLight() throws AnvilException, IOException {
        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn c = region.getOrCreateChunk(0, 0);
        c.getSections()[0].setBlockLight(0,0,0, (byte) 15);
        c.getSections()[0].setBlockLight(0,5,0, (byte) 12);
        assertEquals((byte) 15, c.getSections()[0].getBlockLight(0,0,0));
        assertEquals((byte) 12, c.getSections()[0].getBlockLight(0,5,0));
    }

    @Test
    public void setSkyLight() throws AnvilException, IOException {
        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn c = region.getOrCreateChunk(0, 0);
        c.getSections()[0].setSkyLight(0,0,0, (byte) 15);
        c.getSections()[0].setSkyLight(0,5,0, (byte) 12);
        assertEquals((byte) 15, c.getSections()[0].getSkyLight(0,0,0));
        assertEquals((byte) 12, c.getSections()[0].getSkyLight(0,5,0));
    }

    @AfterEach
    public void clean() throws IOException {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Files.delete(Paths.get("tmp_save_r.0.0.mca"));
    }
}
