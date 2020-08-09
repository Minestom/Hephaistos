package mca;

import org.jglrxavpok.hephaistos.mca.Heightmap;
import org.jglrxavpok.hephaistos.mca.SupportedVersion;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HeightmapCompression {

    @Test
    public void loadAndSave1_15() {
        Heightmap map = new Heightmap();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                map.set(x, z, 255-x+z*16);
            }
        }
        long[] compacted = map.compact(SupportedVersion.MC_1_15);
        assertEquals(36, compacted.length);
        Heightmap loaded = new Heightmap(compacted, SupportedVersion.MC_1_15);
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int height = loaded.get(x, z);
                assertEquals(255-x+z*16, height);
            }
        }
    }

    @Test
    public void loadAndSave1_16() {
        Heightmap map = new Heightmap();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                map.set(x, z, 255-x+z*16);
            }
        }
        long[] compacted = map.compact(SupportedVersion.MC_1_16);
        assertEquals(37, compacted.length);
        Heightmap loaded = new Heightmap(compacted, SupportedVersion.MC_1_16);
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int height = loaded.get(x, z);
                assertEquals(255-x+z*16, height);
            }
        }
    }

}
