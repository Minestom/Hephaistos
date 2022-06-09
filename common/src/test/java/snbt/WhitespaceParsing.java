package snbt;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.jglrxavpok.hephaistos.parser.SNBTParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class WhitespaceParsing {

    @Test
    public void readChatRegistry() throws NBTException {
        // From Minestom
        NBTCompound chatRegistry = (NBTCompound) new SNBTParser(new StringReader("{\n" +
                "    \"type\": \"minecraft:chat_type\",\n" +
                "    \"value\": [\n" +
                "      {\n" +
                "        \"name\": \"minecraft:system\",\n" +
                "        \"id\": 0,\n" +
                "        \"element\": {\n" +
                "          \"chat\": {},\n" +
                "          \"narration\": {\n" +
                "            \"priority\": \"system\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"minecraft:game_info\",\n" +
                "        \"id\": 1,\n" +
                "        \"element\": {\n" +
                "          \"overlay\": {}\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "}\n")).parse();

        // whitespace in from of id values are NOT part of the value
        Assertions.assertEquals(NBTType.TAG_Int, chatRegistry.<NBTCompound>getList("value").get(0).get("id").getID());
        Assertions.assertEquals(0, chatRegistry.<NBTCompound>getList("value").get(0).getAsInt("id"));
        Assertions.assertEquals(1, chatRegistry.<NBTCompound>getList("value").get(1).getAsInt("id"));
    }
}
