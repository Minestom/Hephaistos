import com.google.gson.JsonObject;
import org.jglrxavpok.hephaistos.json.NBTGsonReader;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTType;
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

    @Test
    public void readCompoundFromGsonElement() {
        JsonObject object = new JsonObject();
        object.addProperty("a string", "hello");
        object.addProperty("a float", 1F);

        NBTCompound compound = NBTGsonReader.parse(NBTType.TAG_Compound, object);

        assertEquals(2, compound.getSize());
        assertEquals("hello", compound.getString("a string"));
        assertEquals(1f, compound.getAsFloat("a float"));
    }

    @Test
    public void readCompoundFromGsonElementWithInnerElement() {
        JsonObject object = new JsonObject();
        object.addProperty("a string", "hello");
        object.addProperty("a float", 1F);
        JsonObject inner = new JsonObject();
        inner.addProperty("inner", "yes");
        object.add("inner", inner);

        NBTCompound compound = NBTGsonReader.parse(NBTType.TAG_Compound, object);

        assertEquals(3, compound.getSize());
        assertEquals("hello", compound.getString("a string"));
        assertEquals(1f, compound.getAsFloat("a float"));

        NBTCompound innerCompound = compound.getCompound("inner");

        assertEquals("yes", innerCompound.getString("inner"));
    }
}
