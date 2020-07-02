package mca;

import org.jglrxavpok.mca.AnvilException;
import org.jglrxavpok.mca.ChunkColumn;
import org.jglrxavpok.mca.RegionFile;
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

public class MCALoading {

    private RandomAccessFile file;

    @Before
    public void init() throws IOException {
        Files.copy(Paths.get("src/test/resources/r.0.0.mca"), Paths.get("tmp_r.0.0.mca"), REPLACE_EXISTING);
        file = new RandomAccessFile(new File("tmp_r.0.0.mca"), "rw");
    }

    @Test
    public void read() throws AnvilException {
        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn column0_0 = region.getChunk(0, 0);
        assertEquals(0, column0_0.getX());
        assertEquals(0, column0_0.getZ());
        assertEquals(ChunkColumn.GenerationStatus.Full, column0_0.getGenerationStatus());
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
