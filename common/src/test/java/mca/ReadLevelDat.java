package mca;

import org.jglrxavpok.hephaistos.nbt.CompressedProcesser;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReadLevelDat {

    @Test
    public void readLevelDatFiles() throws IOException, NBTException {
        String[] files = new String[] {
                "level.1.16.dat",
                "level.1.18.dat",
        };
        for(String f : files) {
            try(var reader = new NBTReader(ReadLevelDat.class.getResourceAsStream("/"+f), CompressedProcesser.GZIP)) {
                final NBTCompound tag = (NBTCompound) reader.read();
                assertTrue(tag.contains("Data"));
            } catch (IOException | NBTException e) {
                throw e;
            }
        }
    }
}
