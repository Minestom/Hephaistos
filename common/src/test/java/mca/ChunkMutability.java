package mca;

import org.jglrxavpok.hephaistos.data.DataSource;
import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.CoordinatesKt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ChunkMutability {

    @Test
    public void biomesAreMutable() {
        ChunkColumn chunk = new ChunkColumn(0, 0);
        final int biomeGranularity = 4;
        for (int y = 0; y < 256; y += biomeGranularity) {
            for (int x = 0; x < 16; x += biomeGranularity) {
                for (int z = 0; z < 16; z += biomeGranularity) {
                    int biomeID = x + y * 16 + z * 16 * 16;
                    chunk.setBiome(x, y, z, biomeID);
                }
            }
        }


        for (int y = 0; y < 256; y += biomeGranularity) {
            for (int x = 0; x < 16; x += biomeGranularity) {
                for (int z = 0; z < 16; z += biomeGranularity) {
                    int expectedBiomeID = x + y * 16 + z * 16 * 16;
                    int actualBiomeID = chunk.getBiome(x, y, z);
                    Assertions.assertEquals(expectedBiomeID, actualBiomeID);
                }
            }
        }
    }

}