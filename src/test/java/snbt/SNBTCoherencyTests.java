package snbt;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.SNBTParser;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class SNBTCoherencyTests {

    @Test
    public void testCoherence() {
        NBTCompound c = new NBTCompound();

        NBTCompound inside = new NBTCompound();
        inside.setByte("AAA", (byte) 42);
        inside.setIntArray("myIntArray", new int[] { 1, 2, 3, 9});
        c.set("inside", inside);
        c.setLong("timestamp", -914312L);

        String snbt = c.toSNBT();
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            assertEquals(c, parser.parse());
        }
    }
}
