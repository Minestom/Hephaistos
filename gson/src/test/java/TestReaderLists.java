import org.jglrxavpok.hephaistos.json.NBTGsonReader;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class TestReaderLists {

    @Test
    public void readIntArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBTIntArray array = reader.read(NBTIntArray.class);
            assertEquals(4, array.getSize());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void readLongArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBTLongArray array = reader.read(NBTLongArray.class);
            assertEquals(4, array.getSize());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void readByteArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBTByteArray array = reader.read(NBTByteArray.class);
            assertEquals(4, array.getSize());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void readStringArray() {
        String json = "[\"0\",\"1\",\"2\",\"3\"]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBTList<NBTString> array = reader.read(NBTList.class);
            assertEquals(4, array.getSize());
            assertEquals(NBTTypes.TAG_String, array.getSubtagType());
            assertEquals("0", array.get(0).getValue());
            assertEquals("1", array.get(1).getValue());
            assertEquals("2", array.get(2).getValue());
            assertEquals("3", array.get(3).getValue());
        }
    }

    @Test
    public void integerListShouldBeGuessedAsLongArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBT guessed = reader.readWithGuess();
            assertTrue(guessed instanceof NBTLongArray);
            NBTLongArray array = (NBTLongArray)guessed;
            assertEquals(4, array.getSize());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void floatingPointShouldBeGuessedAsDoubleArray() {
        String json = "[0.0,1.0,2.0,3.0]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBT guessed = reader.readWithGuess();
            assertTrue(guessed instanceof NBTList);
            NBTList<NBTDouble> array = (NBTList<NBTDouble>)guessed;
            assertEquals(NBTTypes.TAG_Double, array.getSubtagType());
            assertEquals(4, array.getSize());
            assertEquals(0.0, array.get(0).getValue(), 10e-6);
            assertEquals(1.0, array.get(1).getValue(), 10e-6);
            assertEquals(2.0, array.get(2).getValue(), 10e-6);
            assertEquals(3.0, array.get(3).getValue(), 10e-6);
        }
    }

    @Test
    public void readCompoundList() {
        String json = "[\n" +
                "{\n" +
                "\t\"a\": 0\n" +
                "},\n" +
                "{\n" +
                "\t\"b\": 1,\n" +
                "\t\"aaaaa\": \"text\"\n" +
                "}\n" +
                "]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBT guessed = reader.readWithGuess();
            assertTrue(guessed instanceof NBTList);
            NBTList<NBTCompound> array = (NBTList<NBTCompound>)guessed;
            assertEquals(NBTTypes.TAG_Compound, array.getSubtagType());

            NBTCompound firstObject = array.get(0);
            assertEquals(1, firstObject.getSize());
            assertEquals(0, firstObject.getAsInt("a").intValue());

            NBTCompound secondObject = array.get(1);
            assertEquals(2, secondObject.getSize());
            assertEquals(1, secondObject.getAsInt("b").intValue());
            assertEquals("text", secondObject.getString("aaaaa"));
        }
    }

    @Test
    public void guessListOfList() {
        String json = "[[\"aaa\"], [\"bbb\", \"ccc\"]]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBT array = reader.readWithGuess();
            assertTrue(array instanceof NBTList, "Guessed NBT must be a list");
            NBTList<NBTList<NBTString>> list = (NBTList<NBTList<NBTString>>) array;
            assertEquals(list.getSubtagType(), NBTTypes.TAG_List, "Guessed NBT must be a list of lists");

            NBTList<NBTString> firstList = list.get(0);
            assertEquals(1, firstList.getSize());
            assertEquals("aaa", firstList.get(0).getValue());

            NBTList<NBTString> secondList = list.get(1);
            assertEquals(2, secondList.getSize());
            assertEquals("bbb", secondList.get(0).getValue());
            assertEquals("ccc", secondList.get(1).getValue());
        }
    }

    @Test
    public void allElementsOfListsShouldBeOfSameType() {
        String json = "[[\"aaa\"], 0]";
        assertThrows(NBTException.class, () -> {
            try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
                NBT n = reader.readWithGuess();
                fail("This list should not be read correctly because lists must have the same type for each sub-element, and this one does not");
            }
        });
    }

    @Test
    public void emptyListShouldReturnStringList() {
        String json = "[]";
        try(NBTGsonReader reader = NBTGsonReader.reader(new StringReader(json))) {
            NBT array = reader.readWithGuess();
            assertTrue(array instanceof NBTList, "Guessed NBT must be a list");
            NBTList<NBTString> list = (NBTList<NBTString>) array;
            assertEquals(list.getSubtagType(), NBTTypes.TAG_String, "Guessed NBT must be a list of strings");
            assertEquals(0, list.getSize());
        }
    }

}
