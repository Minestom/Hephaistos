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

import static org.junit.Assert.assertEquals;

public class MCASaving {

    private RandomAccessFile file;

    @Before
    public void init() throws IOException {
        File target = new File("tmp_save_r.0.0.mca");
        target.createNewFile();
        file = new RandomAccessFile(target, "rw");
    }

    @Test
    public void test() throws AnvilException {
        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn newColumn = region.getOrCreateChunk(0, 0);
        assertEquals(ChunkColumn.GenerationStatus.Empty, newColumn.getGenerationStatus());
    }

    @After
    public void clean() throws IOException {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Files.delete(Paths.get("tmp_save_r.0.0.mca"));
    }
}
