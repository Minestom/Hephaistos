package mca;

import org.jglrxavpok.hephaistos.data.DataSource;
import org.jglrxavpok.hephaistos.data.GrowableSource;
import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.BlockState;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(Parameterized.class)
public class MCASaving {

    private DataSource dataSource;
    private Supplier<DataSource> sourceSupplier;

    @Parameterized.Parameters(name = "{index}")
    public static Iterable<Object[]> data() {
        Supplier<DataSource> growable = GrowableSource::new;
        Supplier<DataSource> raf = () -> {
            File target = new File("tmp_save_r.0.0.mca");
            try {
                target.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return new RandomAccessFileSource(new RandomAccessFile(target, "rw"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        };
        return Arrays.asList(new Object[][]{
                        {
                                raf
                        },
                        {
                                growable
                        }
                }
        );
    }

    public MCASaving(Supplier<DataSource> sourceSupplier) {
        this.sourceSupplier = sourceSupplier;
    }

    @BeforeEach
    public void init() throws IOException {
        dataSource = sourceSupplier.get();
    }

    @Test
    public void verySimpleCreation() throws AnvilException, IOException {
        RegionFile region = new RegionFile(dataSource, 0, 0);
        ChunkColumn newColumn = region.getOrCreateChunk(0, 0);
        assertEquals(ChunkColumn.GenerationStatus.Empty, newColumn.getGenerationStatus());

        region.writeColumn(newColumn);
    }

    @Test
    public void reuseFreeSector() throws AnvilException, IOException {
        final int sectionTotal = 10; // how many sections we want to use
        // purposely use too many to force having 8 free sectors
        dataSource.setLength(4096 * (sectionTotal+2));
        dataSource.seek(0);
        // ensure file is full of zeroes
        for (long i = 0; i < dataSource.length(); i++) {
            dataSource.write(0);
        }
        dataSource.seek(0);

        RegionFile region = new RegionFile(dataSource, 0, 0);
        ChunkColumn column = new ChunkColumn(0,0);
        region.writeColumn(column);

        dataSource.seek(0);
        int location = dataSource.readInt();
        int start = (location >> 8) & 0xFFFFFF;
        int length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector

        region.writeColumn(column);

        // after a rewrite, should be moved to new sector
        dataSource.seek(0);
        location = dataSource.readInt();
        start = (location >> 8) & 0xFFFFFF;
        length = location & 0xFF;
        assertEquals(3, start); // 2nd free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector

        region.writeColumn(column);

        // after another rewrite, should be moved to the first sector, as it is now free
        dataSource.seek(0);
        location = dataSource.readInt();
        start = (location >> 8) & 0xFFFFFF;
        length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector
    }

    @Test
    public void useFreeSector() throws AnvilException, IOException {
        final int sectionTotal = 10; // how many sections we want to use
        // purposely use too many to force having 8 free sectors
        dataSource.setLength(4096 * (sectionTotal+2));
        dataSource.seek(0);
        // ensure file is full of zeroes
        for (long i = 0; i < dataSource.length(); i++) {
            dataSource.write(0);
        }
        dataSource.seek(0);

        RegionFile region = new RegionFile(dataSource, 0, 0);
        ChunkColumn column = new ChunkColumn(0,0);
        region.writeColumn(column);

        dataSource.seek(0);
        int location = dataSource.readInt();
        int start = (location >> 8) & 0xFFFFFF;
        int length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector
    }

    @Test
    public void allocateNewSector() throws AnvilException, IOException {
        dataSource.setLength(4096 * 2);
        dataSource.seek(0);
        // ensure file is full of zeroes
        for (long i = 0; i < dataSource.length(); i++) {
            dataSource.write(0);
        }
        assertEquals(4096* 2, dataSource.length()); // no free sectors
        dataSource.seek(0);

        RegionFile region = new RegionFile(dataSource, 0, 0);
        ChunkColumn column = new ChunkColumn(0,0);
        region.writeColumn(column);

        assertEquals(4096*3, dataSource.length()); // allocated one new sector

        dataSource.seek(0);
        int location = dataSource.readInt();
        int start = (location >> 8) & 0xFFFFFF;
        int length = location & 0xFF;
        assertEquals(2, start); // first free sector
        assertEquals(1, length); // chunk very light, should only use a single 4kib sector
    }

    // TODO: 1.17 TESTS

    @Test
    public void creationFromScratchViaRegionFile() throws AnvilException, IOException {
        {
            RegionFile region = new RegionFile(dataSource, 0, 0);
            BlockState stone = new BlockState("minecraft:stone");
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 256; y++) {
                        region.setBlockState(x, y, z, BlockState.AIR);
                        region.setBlockState(x + 16, y, z, stone);
                    }
                }
            }
            region.flushCachedChunks();
        }

        {
            RegionFile region = new RegionFile(dataSource, 0, 0);
            BlockState stone = new BlockState("minecraft:stone");
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 256; y++) {
                        assertEquals(BlockState.AIR, region.getBlockState(x, y, z));
                        assertEquals(stone, region.getBlockState(x+16, y, z));
                    }
                }
            }
        }
    }

    @Test
    public void creationFromScratchViaChunks() throws AnvilException, IOException {
        {
            RegionFile region = new RegionFile(dataSource, 0, 0);
            ChunkColumn chunk0 = region.getOrCreateChunk(0, 0);
            ChunkColumn chunk1 = region.getOrCreateChunk(1, 0);
            fillChunk(chunk0, BlockState.AIR, 0, 255);
            fillChunk(chunk1, new BlockState("minecraft:stone"), 0, 255);
            region.writeColumn(chunk0);
            region.writeColumn(chunk1);
        }

        {
            RegionFile reloaded = new RegionFile(dataSource, 0, 0);
            ChunkColumn reloadedChunk0 = reloaded.getChunk(0, 0);
            ChunkColumn reloadedChunk1 = reloaded.getChunk(1, 0);
            assertNotNull(reloadedChunk0);
            assertNotNull(reloadedChunk1);
            verifyChunk(reloadedChunk0, BlockState.AIR, 0, 255);
            verifyChunk(reloadedChunk1, new BlockState("minecraft:stone"), 0, 255);
        }
    }

    private void verifyChunk(ChunkColumn chunkColumn, BlockState state, int minY, int maxY) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y <= maxY; y++) {
                    assertEquals(state, chunkColumn.getBlockState(x, y, z));
                }
            }
        }
    }

    private void fillChunk(ChunkColumn chunkColumn, BlockState state, int minY, int maxY) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y <= maxY; y++) {
                    chunkColumn.setBlockState(x, y, z, state);
                }
            }
        }
    }

    @Test
    public void setBiomes() throws AnvilException, IOException {
        RegionFile region = new RegionFile(dataSource, 0, 0);
        region.getOrCreateChunk(0, 0); // force create chunk
        assertEquals(ChunkColumn.UnknownBiome, region.getBiome(0,0,0));
        region.setBiome(0,0,0, 1);
        assertEquals(1, region.getBiome(0,0,0));
    }

    @Test
    public void setBlockLight() throws AnvilException, IOException {
        RegionFile region = new RegionFile(dataSource, 0, 0);
        ChunkColumn c = region.getOrCreateChunk(0, 0);
        c.getSection((byte)0).setBlockLight(0,0,0, (byte) 15);
        c.getSection((byte)0).setBlockLight(0,5,0, (byte) 12);
        assertEquals((byte) 15, c.getSection((byte)0).getBlockLight(0,0,0));
        assertEquals((byte) 12, c.getSection((byte)0).getBlockLight(0,5,0));
    }

    @Test
    public void setSkyLight() throws AnvilException, IOException {
        RegionFile region = new RegionFile(dataSource, 0, 0);
        ChunkColumn c = region.getOrCreateChunk(0, 0);
        c.getSection((byte)0).setSkyLight(0,0,0, (byte) 15);
        c.getSection((byte)0).setSkyLight(0,5,0, (byte) 12);
        assertEquals((byte) 15, c.getSection((byte)0).getSkyLight(0,0,0));
        assertEquals((byte) 12, c.getSection((byte)0).getSkyLight(0,5,0));
    }

    // Start of 1.17 support ============================================

    @Test
    public void creationFromScratchViaChunks_1_17() throws AnvilException, IOException {
        int[] randomValues = {
                -2598, -267, -428, -392, 899,
                32, 64, 128, 0, 256, 31, 63, 127, 255,
                342, 341
        };

        long progress = 0;
        final long total = randomValues.length * randomValues.length;

        for (int minY : randomValues) {
            for (int maxY : randomValues) {
                boolean expectAnvilException = minY > maxY;
                try {
                    {
                        RegionFile region = new RegionFile(dataSource, 0, 0, minY, maxY);
                        ChunkColumn chunk0 = region.getOrCreateChunk(0, 0);
                        ChunkColumn chunk1 = region.getOrCreateChunk(1, 0);
                        fillChunk(chunk0, BlockState.AIR, minY, maxY);
                        fillChunk(chunk1, new BlockState("minecraft:stone"), minY, maxY);
                        region.writeColumn(chunk0);
                        region.writeColumn(chunk1);
                    }

                    {
                        RegionFile reloaded = new RegionFile(dataSource, 0, 0, minY, maxY);
                        ChunkColumn reloadedChunk0 = reloaded.getChunk(0, 0);
                        ChunkColumn reloadedChunk1 = reloaded.getChunk(1, 0);
                        assertNotNull(reloadedChunk0);
                        assertNotNull(reloadedChunk1);
                        verifyChunk(reloadedChunk0, BlockState.AIR, minY, maxY);
                        verifyChunk(reloadedChunk1, new BlockState("minecraft:stone"), minY, maxY);
                    }
                } catch (AnvilException e) {
                    if(!expectAnvilException || !e.getMessage().equals("minY must be <= maxY")) {
                        throw e;
                    }
                }

                if(progress % 10 == 0) {
                    System.out.println("In progress... " + progress + " / " + total + "");
                }
                progress++;
            }
        }

        System.out.println("Done! (" + progress + " / " + total + ")");
    }


    // End of 1.17 support ============================================

    @AfterEach
    public void clean() throws IOException {
        try {
            dataSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(dataSource instanceof RandomAccessFileSource)
            Files.delete(Paths.get("tmp_save_r.0.0.mca"));
    }
}