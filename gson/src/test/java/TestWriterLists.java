import com.google.gson.JsonElement;
import org.jglrxavpok.hephaistos.json.NBTGsonWriter;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWriterLists {

    private NBTGsonWriter writer;

    @BeforeEach
    public void init() {
        writer = new NBTGsonWriter();
    }

    @Test
    public void writeByteArray() {
        JsonElement element = writer.write(NBT.ByteArray(0xCA, 0xFE, 0xBA, 0xBE));
        assertEquals("[-54,-2,-70,-66]", element.toString());
    }

    @Test
    public void writeIntArray() {
        JsonElement element = writer.write(NBT.IntArray(0xDEAD, 0xBEEF));
        assertEquals("[57005,48879]", element.toString());
    }

    @Test
    public void writeLongArray() {
        JsonElement element = writer.write(NBT.LongArray(132456789L, 987654321L, 14785239L, 9871236540L, -1379468250549L));
        assertEquals("[132456789,987654321,14785239,9871236540,-1379468250549]", element.toString());
    }

    @Test
    public void writeList() {
        NBTList<NBTString> list = new NBTList<>(NBTTypes.TAG_String);
        list.add(new NBTString("My"));
        list.add(new NBTString("Super"));
        list.add(new NBTString("Awesome"));
        list.add(new NBTString("List"));
        JsonElement element = writer.write(list);
        assertEquals("[\"My\",\"Super\",\"Awesome\",\"List\"]", element.toString());
    }

}
