package mca;

import org.jglrxavpok.hephaistos.data.DataSource;
import org.jglrxavpok.hephaistos.data.GrowableSource;
import org.jglrxavpok.hephaistos.data.RandomAccessFileSource;
import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MCALoadAndSave {

    @ParameterizedTest
    @ArgumentsSource(PathProvider.class)
    public void loadAndSave(Path path) throws IOException, AnvilException {
        Path tmpFile = Files.createTempFile("tmp_save_r", ".mca");
        Files.copy(path, tmpFile, REPLACE_EXISTING);
        RandomAccessFile file = new RandomAccessFile(tmpFile.toFile(), "rw");
        try(RegionFile r = new RegionFile(file, 0, 0)) {
            ChunkColumn chunk0_0 = r.getChunk(0,0);
            assertNotNull(chunk0_0);
            r.writeColumn(chunk0_0);
        }
        Files.delete(tmpFile);
    }
}