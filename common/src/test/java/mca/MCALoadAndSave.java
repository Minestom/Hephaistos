package mca;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MCALoadAndSave {

    @Test
    public void loadAndSave() throws IOException, AnvilException {
        Files.copy(Paths.get("src/test/resources/r.0.0.mca"), Paths.get("tmp_save_r.0.0.mca"), REPLACE_EXISTING);
        RandomAccessFile file = new RandomAccessFile(new File("tmp_save_r.0.0.mca"), "rw");
        RegionFile r = new RegionFile(file, 0, 0);
        ChunkColumn chunk0_0 = r.getChunk(0,0);
        assertNotNull(chunk0_0);
        r.writeColumn(chunk0_0);
    }
}