import com.google.gson.JsonElement;
import org.jglrxavpok.hephaistos.json.NBTGsonWriter;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWriterPrimitives {

    private NBTGsonWriter writer;

    @BeforeEach
    public void init() {
        writer = new NBTGsonWriter();
    }

    @Test
    public void writeByte() {
        JsonElement element = writer.write(NBT.Byte((byte) 42));
        assertEquals("42", element.toString());
    }

    @Test
    public void writeShort() {
        JsonElement element = writer.write(new NBTShort((short) -40));
        assertEquals("-40", element.toString());
    }

    @Test
    public void writeLong() {
        JsonElement element = writer.write(new NBTLong(9876543210L));
        assertEquals("9876543210", element.toString());
    }

    @Test
    public void writeString() {
        JsonElement element = writer.write(new NBTString("Some text"));
        assertEquals("\"Some text\"", element.toString());
    }

    @Test
    public void writeFloat() {
        JsonElement element = writer.write(new NBTFloat(0.5f));
        assertEquals("0.5", element.toString());
    }

    @Test
    public void writeDouble() {
        JsonElement element = writer.write(new NBTDouble(-0.25));
        assertEquals("-0.25", element.toString());
    }

}
