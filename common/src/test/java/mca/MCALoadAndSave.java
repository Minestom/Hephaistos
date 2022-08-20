package mca;

import org.jglrxavpok.hephaistos.data.DataSource;
import org.jglrxavpok.hephaistos.data.GrowableSource;
import org.jglrxavpok.hephaistos.data.RandomAccessFileSource;
import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.jglrxavpok.hephaistos.mca.readers.ChunkReader;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.ByteArrayOutputStream;
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

    @Test
    public void saveAlmostFullChunk() throws IOException, AnvilException {
        Path tmpFile = Files.createTempFile("tmp_save_r", ".mca");
        RandomAccessFile file = new RandomAccessFile(tmpFile.toFile(), "rw");

        try (RegionFile r = new RegionFile(file, 0, 0)) {
            int bigChunkIndex = 0;
            int overlappedChunkIndex = 1;
            // allocate one sector for chunk which will have big data size
            ChunkColumn emptyBigChunk = r.getOrCreateChunk(bigChunkIndex, bigChunkIndex);
            r.writeColumn(emptyBigChunk);
            r.forget(emptyBigChunk);

            // allocate one sector for chunk which will be overlapped by another chunk
            ChunkColumn initialChunk = r.getOrCreateChunk(overlappedChunkIndex, overlappedChunkIndex);
            r.writeColumn(initialChunk);
            r.forget(initialChunk);

            // write some NBT data so that dataSize is in [4092, 4096] range (4094 in this case)
            NBTCompound chunkData = NBT.Compound(builder -> {
                int[] intData = new int[2230];
                for (int i = 0; i < intData.length; i++) {
                    intData[i] = i;
                }
                NBTIntArray data = NBT.IntArray(intData);
                builder.put("v", data);
            });
            ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
            try (NBTWriter writer = new NBTWriter(dataOut, CompressedProcesser.ZLIB)) {
                writer.writeNamed("", chunkData);
            }

            // free previous big chunk position (which is before overlapped chunk)
            r.writeColumnData(chunkData, bigChunkIndex, bigChunkIndex);
            // write big chunk before overlapped chunk
            r.writeColumnData(chunkData, bigChunkIndex, bigChunkIndex);

            // load chunk after possible overlapping (should not happen)
            ChunkColumn oldChunk = r.getChunk(overlappedChunkIndex, overlappedChunkIndex);
            assertNotNull(oldChunk);
            assertEquals(oldChunk.toNBT(), initialChunk.toNBT());
        } finally {
            Files.delete(tmpFile);
        }
    }
}