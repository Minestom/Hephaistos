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
import static org.junit.Assert.assertNotNull;

public class MCASaving {

    private RandomAccessFile file;

    @Before
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
