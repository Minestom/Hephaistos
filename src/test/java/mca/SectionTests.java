package mca;

import org.jglrxavpok.mca.BlockState;
import org.jglrxavpok.mca.ChunkSection;
import org.jglrxavpok.nbt.NBTCompound;
import org.jglrxavpok.nbt.NBTList;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class SectionTests {

    @Test
    public void paletteAfterSetBlockState() {
        ChunkSection section = new ChunkSection((byte)0);
        section.set(0,0,0, new BlockState("minecraft:stone", new HashMap<>()));
        NBTCompound nbt = section.toNBT();
        NBTList<NBTCompound> palette = nbt.getList("Palette");
        assertEquals(2, palette.getLength());
        assertEquals(BlockState.Air.getName(), palette.get(0).getString("Name"));
        assertEquals("minecraft:stone", palette.get(1).getString("Name"));
    }
}
