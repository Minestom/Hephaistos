package regression;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTString;
import org.jglrxavpok.hephaistos.parser.SNBTParser;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue17 {
    @Test
    public void unescapeUnicode() throws NBTException {
        String tag = "'\\\" hi \\\" \\u00e9'";

        SNBTParser parser = new SNBTParser(new StringReader(tag));
        NBTString parsed = (NBTString) parser.parse();
        assertEquals("\" hi \" \u00e9", parsed.getValue());
    }
}
