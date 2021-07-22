package mca;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.BlockState;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class MCACoordinates {

    private static RegionFile region;
    private static RegionFile region2;

    @BeforeAll
    static void init() throws IOException, AnvilException {
        Path target = Path.of("./tmp_coords_r.0.0.mca");
        Path target2 = Path.of("./tmp_coords_r2.0.0.mca");
        Files.createFile(target);
        Files.createFile(target2);
        RandomAccessFile file = new RandomAccessFile(target.toFile(), "rw");
        RandomAccessFile file2 = new RandomAccessFile(target2.toFile(), "rw");
        region = new RegionFile(file, 0, 0);
        region2 = new RegionFile(file2, 5, -2);
    }

    @Test
    public void throwOnInvalidCoordsX() {
        assertThrows(AnvilException.class, () ->
            region.getChunk(-1, 0)
        );
    }

    @Test
    public void throwOnInvalidCoordsZ() {
        assertThrows(AnvilException.class, () ->
            region.getChunk(0, -1)
        );
    }

    @Test
    public void throwOnInvalidCoordsX_2() {
        assertThrows(AnvilException.class, () ->
            region.getChunk(32, 0)
        );
    }

    @Test
    public void throwOnInvalidCoordsZ_2() {
        assertThrows(AnvilException.class, () ->
            region.getChunk(0, 32)
        );
    }

    @Test
    public void throwOnInvalidChunkLocalPositionXNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            ChunkColumn column;
            column = region2.getOrCreateChunk(5 * 32 + 5, -2 * 32 + 5);
            column.setBlockState(-1, 0, 0, BlockState.Air);
        }, "Chunk has a valid position inside the RegionFile, should not throw");
    }

    @Test
    public void throwOnInvalidChunkLocalPositionXOverMax() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            ChunkColumn column;
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(16, 0, 0, BlockState.Air);
        }, "Chunk has a valid position inside the RegionFile, should not throw");
    }

    @Test
    public void validChunkLocalPositionX() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(8, 0, 0, BlockState.Air);
        } catch (AnvilException e) {
            fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test
    public void validChunkLocalPositionZ() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 0, 8, BlockState.Air);
        } catch (AnvilException e) {
            fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test
    public void throwOnInvalidChunkLocalPositionZNegative() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            ChunkColumn column;
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 0, -1, BlockState.Air);
        });
    }

    @Test
    public void throwOnInvalidChunkLocalPositionZOverMax() {
        assertThrows(IllegalArgumentException.class, () -> {
            ChunkColumn column;
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 0, 16, BlockState.Air);
        }, "Chunk has a valid position inside the RegionFile, should not throw");
    }

    @Test
    public void validChunkLocalPositionY() throws IOException {
        ChunkColumn column;
        try {
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 8, 0, BlockState.Air);
        } catch (AnvilException e) {
            fail("Chunk has a valid position inside the RegionFile, should not throw");
        }
    }

    @Test
    public void throwOnInvalidChunkLocalPositionYNegative() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            ChunkColumn column;
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, -1, 0, BlockState.Air);
        });
    }


    @Test
    public void throwOnInvalidChunkLocalPositionYOverMax() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            ChunkColumn column;
            column = region2.getOrCreateChunk(5*32+5, -2*32+5);
            column.setBlockState(0, 256, 0, BlockState.Air);
        }, "Chunk has a valid position inside the RegionFile, should not throw");
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

    @AfterAll
    public void clean() throws IOException {
        region.close();
        region2.close();
        Files.delete(Paths.get("tmp_coords_r.0.0.mca"));
        Files.delete(Paths.get("tmp_coords_r2.0.0.mca"));
    }
}
