package mca;

import org.jglrxavpok.mca.AnvilException;
import org.jglrxavpok.mca.BlockState;
import org.jglrxavpok.mca.ChunkColumn;
import org.jglrxavpok.mca.RegionFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;

/**
 * While these tests are far from perfect and can clearly report false positives, the sheer amount of chunks to try to save/load at the same time
 * amounts to such probabilities of corruption that a false positive is extremely unlikely.
 *
 * Run these tests multiple times if you are suspicious.
 *
 * These tests are very basic, and by no means guarantee 100% thread-safety
 */
public class ParallelizationTests {

    @Test
    public void parallelLoad() throws InterruptedException, IOException, AnvilException {
        Files.copy(Paths.get("src/test/resources/r.0.0.mca"), Paths.get("tmp_parallel_r.0.0.mca"), REPLACE_EXISTING);

        RegionFile region = new RegionFile(new RandomAccessFile(new File("tmp_parallel_r.0.0.mca"), "rw"), 0, 0);

        ExecutorService pool = Executors.newFixedThreadPool(4);
        Future<ChunkColumn>[] chunks = new Future[1024];
        for (int i = 0; i < 1024; i++) {
            final int chunkID = i;
            chunks[i] = pool.submit(() -> region.getChunk(chunkID % 32, chunkID / 32));
        }
        pool.shutdown();
        for (int i = 0; i < chunks.length; i++) {
            try {
                chunks[i].get();
            } catch (ExecutionException e) {
                throw new AssertionError("Chunk loading failed (cause may look weird if filepointer 'corruptions' happen)", e);
            }
        }
        // if parallel reading works and throws no exception, chunks should have loaded with no issue

        region.close();
        Files.deleteIfExists(Paths.get("tmp_parallel_r.0.0.mca"));
    }

    private void fill(ChunkColumn column, int id) {
        BlockState state = new BlockState(String.valueOf(id));
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 256; y++) {
                    column.setBlockState(x, y, z, state);
                }
            }
        }
    }

    private void verify(ChunkColumn column, int id) {
        BlockState state = new BlockState(String.valueOf(id));
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 256; y++) {
                    assertEquals(state, column.getBlockState(x, y, z));
                }
            }
        }
    }

    @Test
    public void parallelSave() throws InterruptedException, IOException, AnvilException {
        RegionFile region = new RegionFile(new RandomAccessFile(new File("tmp_parallel_empty_r.0.0.mca"), "rw"), 0, 0);

        ExecutorService pool = Executors.newFixedThreadPool(4);
        Future<ChunkColumn>[] chunks = new Future[1024];
        for (int i = 0; i < 1024; i++) {
            final int chunkID = i;
            RegionFile finalRegion = region;
            chunks[i] = pool.submit(() -> {
                ChunkColumn column = finalRegion.getOrCreateChunk(chunkID % 32, chunkID / 32);
                fill(column, chunkID);
                finalRegion.writeColumn(column);
                return column;
            });
        }
        pool.shutdown();
        for (int i = 0; i < chunks.length; i++) {
            try {
                chunks[i].get();
            } catch (ExecutionException e) {
                throw new AssertionError("Chunk loading failed (cause may look weird if filepointer 'corruptions' happen)", e);
            }
        }
        // if parallel writes works and throws no exception, chunks should have saved with no issue
        // now, we check their contents
        region.close();

        region = new RegionFile(new RandomAccessFile(new File("tmp_parallel_empty_r.0.0.mca"), "rw"), 0, 0);
        for (int i = 0; i < 1024; i++) {
            final int chunkID = i;
            ChunkColumn column = region.getOrCreateChunk(chunkID % 32, chunkID / 32);
            verify(column, chunkID);
        }

        region.close();
        Files.deleteIfExists(Paths.get("tmp_parallel_empty_r.0.0.mca"));
    }
}
