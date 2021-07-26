package snbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.jglrxavpok.hephaistos.parser.SNBTParser;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class SNBTCoherencyTests {

    @Test
    public void testCoherence() throws NBTException {

        NBTList<NBTString> testList = NBT.List(NBTType.TAG_String, 10, i -> NBT.String("Test#" + i));

        NBTCompound c = NBT.Compound(root -> {
            root.put("inside", NBT.Compound(inside -> {
                inside.put("AAA", NBT.Byte(42));
                inside.put("BBB", NBT.Short(23));
                inside.put("CCC", NBT.Int(-50));
                inside.put("DDD", NBT.Long(123456789123456L));
                inside.put("myByteArray", NBT.ByteArray(1, 2, 3, -9));
                inside.put("myIntArray", NBT.IntArray(1, 2, 3, 9));
                inside.put("myLongArray", NBT.LongArray(10468463464L, 2489679874L, 3549876415L, 9489674L));
                inside.put("EEE", NBT.Double(0.25));
                inside.put("FFF", NBT.Float(0.125f));
                inside.put("some text", NBT.String("KSOKPDOK"));
            }));
            root.put("list", testList);
            root.put("timestamp", NBT.Long(-914312L));
        });

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
