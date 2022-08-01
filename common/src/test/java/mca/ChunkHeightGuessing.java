package mca;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.CoordinatesKt;
import org.jglrxavpok.hephaistos.mca.SupportedVersion;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Due to 1.17 custom dimensions, some chunks no longer have 0-255 bounds.
 * In 1.18 there is enough information inside chunks to get the information.
 * In <1.16, 0-255 is the only possible range
 *
 * In 1.17, Hephaistos uses the lowest sectionY to find minY, because chunks include an additional section below the limit (eg a -1 section when 0 is the minY)
 * In 1.17, Hephaistos will use the length of the 'Biomes' array to find the maxY, once minY has been computed.
 * This is a round-about way of doing this but this works without having the user add the information...
 */
public class ChunkHeightGuessing {

    private static void checkHeight(SupportedVersion version, NBTCompound data, int expectedMinY, int expectedMaxY) throws AnvilException {
        ChunkColumn chunk = new ChunkColumn(data);
        assertEquals(expectedMinY, chunk.getMinY());
        assertEquals(expectedMaxY, chunk.getMaxY());

        NBTCompound exported = chunk.toNBT(version);
        ChunkColumn reloaded = new ChunkColumn(exported);
        assertEquals(expectedMinY, reloaded.getMinY());
        assertEquals(expectedMaxY, reloaded.getMaxY());
    }

    @Test
    public void test1_16() throws AnvilException {
        NBTCompound data = NBT.Compound(nbt -> {
            nbt.setInt("DataVersion", SupportedVersion.MC_1_16.getLowestDataVersion());

            nbt.set("Level", NBT.Compound(level -> {
                level.setInt("xPos", 0);
                level.setInt("zPos", 0);
                level.set("Sections", NBT.List(NBTType.TAG_Compound, 0, i -> new NBTCompound()));
                level.setLong("LastUpdate", 0L);
                level.setLong("InhabitedTime", 0L);
                level.setString("Status", ChunkColumn.GenerationStatus.Spawn.getId()); // don't have to do the heightmaps
                level.setByte("isLightOn", (byte) 1);

                int[] biomes = new int[4*4*4*16];
                level.setIntArray("Biomes", biomes);
            }));
        });
        checkHeight(SupportedVersion.MC_1_16, data, 0, 255);
    }

    @FunctionalInterface
    private static interface Checker {
        void check(int minY, int maxY) throws AnvilException;
    }

    @Test
    public void test1_17() throws AnvilException {
        Checker checker = (int minY, int maxY) -> {
            final int worldHeight = maxY - minY +1;
            NBTCompound data = NBT.Compound(nbt -> {
                nbt.setInt("DataVersion", SupportedVersion.MC_1_17_0.getLowestDataVersion());

                nbt.set("Level", NBT.Compound(level -> {
                    level.setInt("xPos", 0);
                    level.setInt("zPos", 0);

                    List<NBTCompound> compounds = new ArrayList<>();
                    compounds.add(NBT.Compound(section -> {
                        section.setByte("Y", (byte) (CoordinatesKt.blockToSection(minY)-1));
                    }));

                    level.set("Sections", NBT.List(NBTType.TAG_Compound, compounds));
                    level.setLong("LastUpdate", 0L);
                    level.setLong("InhabitedTime", 0L);
                    level.setString("Status", ChunkColumn.GenerationStatus.Spawn.getId()); // don't have to do the heightmaps
                    level.setByte("isLightOn", (byte) 1);

                    int[] biomes = new int[4* worldHeight];
                    level.setIntArray("Biomes", biomes);
                }));
            });
            checkHeight(SupportedVersion.MC_1_17_0, data, minY, maxY);
        };

        checker.check(0, 255);
        checker.check(-64, 319);
        checker.check(-32, 127);
    }

    @Test
    public void test1_18() throws AnvilException {
        Checker checker = (int minY, int maxY) -> {
            final int worldHeight = maxY - minY +1;
            NBTCompound data = NBT.Compound(level -> {
                level.setInt("DataVersion", SupportedVersion.MC_1_18_PRE_4.getLowestDataVersion());

                int minSectionY = CoordinatesKt.blockToSection(minY);

                level.setInt("xPos", 0);
                level.setInt("yPos", minSectionY);
                level.setInt("zPos", 0);

                List<NBTCompound> compounds = new ArrayList<>();
                for (int sectionY = minSectionY; sectionY <= CoordinatesKt.blockToSection(maxY); sectionY++) {
                    int finalSectionY = sectionY;
                    compounds.add(NBT.Compound(section -> {
                        section.setByte("Y", (byte) finalSectionY);
                    }));
                }

                level.set("sections", NBT.List(NBTType.TAG_Compound, compounds));
                level.setLong("LastUpdate", 0L);
                level.setLong("InhabitedTime", 0L);
                level.setString("Status", ChunkColumn.GenerationStatus.Spawn.getId()); // don't have to do the heightmaps
                level.setByte("isLightOn", (byte) 1);
            });
            checkHeight(SupportedVersion.MC_1_18_PRE_4, data, minY, maxY);
        };

        checker.check(0, 255);
        checker.check(-64, 319);
        checker.check(-32, 127);
    }
}
