package mca;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.BlockState;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MCACoordinates {

    private RegionFile region;
    private RegionFile region2;

    @Before
    public void init() throws IOException, AnvilException {
        File target = new File("tmp_coords_r.0.0.mca");
        File target2 = new File("tmp_coords_r2.0.0.mca");
        target.createNewFile();
        target2.createNewFile();
        RandomAccessFile file = new RandomAccessFile(target, "rw");
        RandomAccessFile file2 = new RandomAccessFile(target2, "rw");
        region = new RegionFile(file, 0, 0);
        region2 = new RegionFile(file2, 5, -2);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsX() throws IOException, AnvilException {
        region.getChunk(-1, 0);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsZ() throws IOException, AnvilException {
        region.getChunk(0, -1);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsX_2() throws IOException, AnvilException {
        region.getChunk(32, 0);
    }

    @Test(expected = AnvilException.class)
    public void throwOnInvalidCoordsZ_2() throws IOException, AnvilException {
        region.getChunk(0, 32);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwOnInvalidChunkLocalPositionXNegative() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(-1, 0, 0, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwOnInvalidChunkLocalPositionXOverMax() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(16, 0, 0, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test
    public void validChunkLocalPositionX() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(8, 0, 0, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test
    public void validChunkLocalPositionZ() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 0, 8, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwOnInvalidChunkLocalPositionZNegative() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 0, -1, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwOnInvalidChunkLocalPositionZOverMax() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 0, 16, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test
    public void validChunkLocalPositionY() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 8, 0, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwOnInvalidChunkLocalPositionYNegative() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, -1, 0, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwOnInvalidChunkLocalPositionYOverMax() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 256, 0, BlockState.Air);
        } catch (AnvilException e) {
            Assert.fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test
    public void setBlockStateInNon0_0Region() throws AnvilException, IOException {
        BlockState stone = new BlockState("minecraft:stone");
        region2.setBlockState((32*5)*16+16, 8, (-2*32)*16+16, stone);
        assertEquals(stone, region2.getBlockState((32*5)*16+16, 8, (-2*32)*16+16));
        ChunkColumn chunk = region2.getChunk(32*5+1, -2*32+1);
        assertNotNull(chunk);
        assertEquals(stone, chunk.getBlockState(0, 8, 0));
        assertEquals(BlockState.Air, chunk.getBlockState(0, 0, 0));
    }

    @After
    public void clean() throws IOException {
        region.close();
        region2.close();
        Files.delete(Paths.get("tmp_coords_r.0.0.mca"));
        Files.delete(Paths.get("tmp_coords_r2.0.0.mca"));
    }
}
