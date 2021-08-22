package regression;

import org.jglrxavpok.hephaistos.mca.AnvilException;
import org.jglrxavpok.hephaistos.mca.ChunkColumn;
import org.jglrxavpok.hephaistos.mca.RegionFile;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.parser.SNBTParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue8 {

    @Test
    public void escapeString() {
        String input = "{\"t\":\"h \\\" hihi \\\" i\"}";
        NBTCompound compound;
        try {
            compound = (NBTCompound) new SNBTParser(new StringReader(input)).parse();
        } catch (final NBTException e) {
            throw new RuntimeException(e);
        }

        assertEquals("h \" hihi \" i",
                compound.getString("t")
        );
    }

    /* Removed test. Reason: tag-order dependent, and redundant with test above
    @Test
    public void escapeString() throws IOException, AnvilException {
        String input = "{\"Unbreakable\":1B,\"CustomModelData\":22,\"item_id\":\"test_back\",\"display\":{\"Lore\":[\"{\\\"text\\\":\\\"This is a test backpack! Used for wearing and testing! Put it on your back slot!\\\"}\"],\"Name\":\"{\\\"text\\\":\\\"Test Backpack\\\"}\"}}";
        NBTCompound compound;
        try {
            compound = (NBTCompound) new SNBTParser(new StringReader(input)).parse();
        } catch (final NBTException e) {
            throw new RuntimeException(e);
        }

        assertEquals(
                "{\"Unbreakable\":1B,\"CustomModelData\":22,\"item_id\":\"test_back\",\"display\":{\"Lore\":[\"{\\\"text\\\":\\\"This is a test backpack! Used for wearing and testing! Put it on your back slot!\\\"}\"],\"Name\":\"{\\\"text\\\":\\\"Test Backpack\\\"}\"}}",
                compound.toString()
        );
    }
    */

}
