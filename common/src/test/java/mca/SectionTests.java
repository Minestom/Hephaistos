package mca;

import org.jglrxavpok.hephaistos.mca.BlockState;
import org.jglrxavpok.hephaistos.mca.ChunkSection;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SectionTests {

    @Test
    public void paletteAfterSetBlockState() {
        ChunkSection section = new ChunkSection((byte)0);
        section.set(0,0,0, new BlockState("minecraft:stone", new HashMap<>()));
        NBTCompound nbt = section.toNBT();
        NBTList<NBTCompound> palette = nbt.getList("Palette");
        assertEquals(2, palette.getSize());
        assertEquals(BlockState.AIR.getName(), palette.get(0).getString("Name"));
        assertEquals("minecraft:stone", palette.get(1).getString("Name"));
    }
}
