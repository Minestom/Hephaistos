import org.jglrxavpok.hephaistos.json.NBTGsonReader;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestReaderCompound {

    @Test
    public void readSimpleCompound() {
        String json = "{\n" +
                "\t\"anInt\": 45,\n" +
                "\t\"a float\": 45.5,\n" +
                "\t\"text\": \"hello it is me, example text!\"\n" +
                "}";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            NBTCompound compound = reader.read(NBTCompound.class);
            assertEquals(3, compound.getSize());
            assertEquals(45, compound.getAsInt("anInt").intValue());
            assertEquals(45.5f, compound.getAsFloat("a float"), 10e-6);
            assertEquals("hello it is me, example text!", compound.getString("text"));
        }
    }
    @Test
    public void readCompoundWithInnerCompound() {
        String json = "{\n" +
                "\t\"anInt\": 45,\n" +
                "\t\"a float\": 45.5,\n" +
                "\t\"text\": \"hello it is me, example text!\",\n" +
                "\t\"testObject\": {\n" +
                "\t\t\"a\": 0,\n" +
                "\t\t\"b\": 1,\n" +
                "\t\t\"c\": 2\n" +
                "\t}\n" +
                "}";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            NBTCompound compound = reader.read(NBTCompound.class);
            assertEquals(4, compound.getSize());
            assertEquals(45, compound.getAsInt("anInt"));
            assertEquals(45.5f, compound.getAsFloat("a float"));
            assertEquals("hello it is me, example text!", compound.getString("text"));

            NBTCompound innerObject = compound.getCompound("testObject");
            assertEquals(3, innerObject.getSize());
            assertEquals(0, innerObject.getAsInt("a"));
            assertEquals(1, innerObject.getAsInt("b"));
            assertEquals(2, innerObject.getAsInt("c"));
        }
    }
}
