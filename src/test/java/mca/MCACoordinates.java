package mca;

import org.jglrxavpok.mca.AnvilException;
import org.jglrxavpok.mca.RegionFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MCACoordinates {

    private RegionFile region;

    @Before
    public void init() throws IOException, AnvilException {
        File target = new File("tmp_coords_r.0.0.mca");
        target.createNewFile();
        RandomAccessFile file = new RandomAccessFile(target, "rw");
        region = new RegionFile(file, 0, 0);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsX() {
        region.getChunk(-1, 0);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsZ() {
        region.getChunk(0, -1);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsX_2() {
        region.getChunk(32, 0);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsZ_2() {
        region.getChunk(0, 32);
    }

    @After
    public void clean() throws IOException {
        region.close();
        Files.delete(Paths.get("tmp_coords_r.0.0.mca"));
    }
}
