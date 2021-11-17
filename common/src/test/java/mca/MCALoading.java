package mca;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MCALoading {

    @ParameterizedTest
    @ArgumentsSource(PathProvider.class)
    public void loadAndSave(Path path) throws IOException, AnvilException {
        Path tmpFile = Files.createTempFile("r", ".mca");
        Files.copy(path, tmpFile, REPLACE_EXISTING);
        RandomAccessFile file = new RandomAccessFile(tmpFile.toFile(), "rw");
        RegionFile region = new RegionFile(file, 0, 0);
        ChunkColumn column0_0 = region.getChunk(0, 0);
        assertNotNull(column0_0);
        assertEquals(0, column0_0.getX());
        assertEquals(0, column0_0.getZ());
        assertEquals(ChunkColumn.GenerationStatus.Full, column0_0.getGenerationStatus());

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                assertEquals("minecraft:bedrock", column0_0.getBlockState(x, column0_0.getMinY(), z).getName());
            }
        }
    }
}
