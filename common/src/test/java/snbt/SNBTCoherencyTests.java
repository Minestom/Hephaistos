package snbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.jglrxavpok.hephaistos.parser.SNBTParser;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class SNBTCoherencyTests {

    @Test
    public void testCoherence() throws NBTException {
        NBTCompound c = new NBTCompound();

        NBTCompound inside = new NBTCompound();
        inside.setByte("AAA", (byte) 42);
        inside.setShort("BBB", (short) 23);
        inside.setInt("CCC", -50);
        inside.setLong("DDD", 123456789123456L);
        inside.setByteArray("myByteArray", new byte[] { 1, 2, 3, -9});
        inside.setIntArray("myIntArray", new int[] { 1, 2, 3, 9});
        inside.setLongArray("myLongArray", new long[] { 10468463464L, 2489679874L, 3549876415L, 9489674L});
        inside.setDouble("EEE", 0.25);
        inside.setFloat("FFF", 0.125f);
        inside.setString("some text", "KSOKPDOK");
        c.set("inside", inside);
        NBTList<NBTString> testList = new NBTList<>(NBTTypes.TAG_String);
        for (int i = 0; i < 10; i++) {
            testList.add(new NBTString("Test#"+i));
        }
        c.set("list", testList);
        c.setLong("timestamp", -914312L);

        String snbt = c.toSNBT();
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            assertEquals(c, parser.parse());
        }
    }

    @Test
    public void syntaxError() {
        String snbt = "{display:{Lore:[\"text here\"]}";
        assertThrows(NBTException.class, () -> {
            try (SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
                parser.parse();
            }
        }, "Missing bracket, should not parse");
    }
}
