package mca;

import org.jglrxavpok.hephaistos.data.DataSource;
import org.jglrxavpok.hephaistos.data.GrowableSource;
import org.jglrxavpok.hephaistos.data.RandomAccessFileSource;
import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.jglrxavpok.hephaistos.mca.readers.ChunkReader;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MCALoadAndSave {

    @ParameterizedTest
    @ArgumentsSource(PathProvider.class)
    public void loadAndSaveNoExceptions(Path path) throws IOException, AnvilException {
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

    @ParameterizedTest
    @ArgumentsSource(PathProvider.class)
    public void loadAndSaveCheckEquality(Path path) throws IOException, AnvilException {
        Path tmpFile = Files.createTempFile("tmp_save_r", ".mca");
        Files.copy(path, tmpFile, REPLACE_EXISTING);
        RandomAccessFile file = new RandomAccessFile(tmpFile.toFile(), "rw");
        try(RegionFile r = new RegionFile(file, 0, 0)) {
            ChunkColumn c = r.getChunk(0, 0);
            r.forget(c); // clear from cache
            r.writeColumn(c, c.getVersion()); // save to region file again (without upgrading to latest version)
            ChunkColumn reloaded = r.getChunk(0, 0); // reload

            assertEquals(c.toNBT(), reloaded.toNBT());
        }
        Files.delete(tmpFile);
    }

    @ParameterizedTest
    @ArgumentsSource(PathProvider.class)
    public void loadAndSaveCheckBiomeEquality(Path path) throws IOException, AnvilException {
        Path tmpFile = Files.createTempFile("tmp_save_r", ".mca");
        Files.copy(path, tmpFile, REPLACE_EXISTING);
        RandomAccessFile file = new RandomAccessFile(tmpFile.toFile(), "rw");
        try(RegionFile r = new RegionFile(file, 0, 0)) {
            NBTCompound cData = r.getChunkData(0, 0);
            ChunkColumn c = new ChunkColumn(cData);
            NBTCompound reloadedNBT = c.toNBT(c.getVersion());

            ChunkReader readerOriginal = new ChunkReader(cData);
            ChunkReader readerReloaded = new ChunkReader(reloadedNBT);
            assertEquals(readerOriginal.getOldBiomes(), readerReloaded.getOldBiomes());
        }
        Files.delete(tmpFile);
    }
}