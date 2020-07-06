package mca;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MCALoading {

    private RandomAccessFile file;

    @Before
    public void init() throws IOException {
        Files.copy(Paths.get("src/test/resources/r.0.0.mca"), Paths.get("tmp_r.0.0.mca"), REPLACE_EXISTING);
        file = new RandomAccessFile(new File("tmp_r.0.0.mca"), "rw");
    }

    @Test
    public void read() throws AnvilException, IOException {
        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn column0_0 = region.getChunk(0, 0);
        assertNotNull(column0_0);
        assertEquals(0, column0_0.getX());
        assertEquals(0, column0_0.getZ());
        assertEquals(ChunkColumn.GenerationStatus.Full, column0_0.getGenerationStatus());

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                assertEquals("minecraft:bedrock", column0_0.getBlockState(x, 0, z).getName());
            }
        }
    }

    @After
    public void clean() throws IOException {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Files.delete(Paths.get("tmp_r.0.0.mca"));
    }
}
