import org.jglrxavpok.hephaistos.json.NBTGsonReader;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class TestReaderLists {

    @Test
    public void readIntArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            NBTIntArray array = reader.read(NBTIntArray.class);
            assertEquals(4, array.getLength());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void readLongArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            NBTLongArray array = reader.read(NBTLongArray.class);
            assertEquals(4, array.getLength());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void readByteArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            NBTByteArray array = reader.read(NBTByteArray.class);
            assertEquals(4, array.getLength());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void readStringArray() {
        String json = "[\"0\",\"1\",\"2\",\"3\"]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            NBTList<NBTString> array = reader.read(NBTList.class);
            assertEquals(4, array.getLength());
            assertEquals(NBTType.TAG_String, array.getSubtagType());
            assertEquals("0", array.get(0).getValue());
            assertEquals("1", array.get(1).getValue());
            assertEquals("2", array.get(2).getValue());
            assertEquals("3", array.get(3).getValue());
        }
    }

    @Test
    public void integerListShouldBeGuessedAsLongArray() {
        String json = "[0,1,2,3]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            MutableNBT guessed = reader.readWithGuess();
            assertTrue(guessed instanceof NBTLongArray);
            NBTLongArray array = (NBTLongArray)guessed;
            assertEquals(4, array.getLength());
            assertEquals(0, array.get(0));
            assertEquals(1, array.get(1));
            assertEquals(2, array.get(2));
            assertEquals(3, array.get(3));
        }
    }

    @Test
    public void floatingPointShouldBeGuessedAsDoubleArray() {
        String json = "[0.0,1.0,2.0,3.0]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            MutableNBT guessed = reader.readWithGuess();
            assertTrue(guessed instanceof NBTList);
            NBTList<NBTDouble> array = (NBTList<NBTDouble>)guessed;
            assertEquals(NBTType.TAG_Double, array.getSubtagType());
            assertEquals(4, array.getLength());
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
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            MutableNBT guessed = reader.readWithGuess();
            assertTrue(guessed instanceof NBTList);
            NBTList<NBTCompound> array = (NBTList<NBTCompound>)guessed;
            assertEquals(NBTType.TAG_Compound, array.getSubtagType());

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
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            MutableNBT array = reader.readWithGuess();
            assertTrue("Guessed NBT must be a list", array instanceof NBTList);
            NBTList<NBTList<NBTString>> list = (NBTList<NBTList<NBTString>>) array;
            assertEquals("Guessed NBT must be a list of lists", list.getSubtagType(), NBTType.TAG_List);

            NBTList<NBTString> firstList = list.get(0);
            assertEquals(1, firstList.getLength());
            assertEquals("aaa", firstList.get(0).getValue());

            NBTList<NBTString> secondList = list.get(1);
            assertEquals(2, secondList.getLength());
            assertEquals("bbb", secondList.get(0).getValue());
            assertEquals("ccc", secondList.get(1).getValue());
        }
    }

    @Test(expected = NBTException.class)
    public void allElementsOfListsShouldBeOfSameType() {
        String json = "[[\"aaa\"], 0]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            MutableNBT n = reader.readWithGuess();
            fail("This list should not be read correctly because lists must have the same type for each sub-element, and this one does not");
        }
    }

    @Test
    public void emptyListShouldReturnStringList() {
        String json = "[]";
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader(json))) {
            MutableNBT array = reader.readWithGuess();
            assertTrue("Guessed NBT must be a list", array instanceof NBTList);
            NBTList<NBTString> list = (NBTList<NBTString>) array;
            assertEquals("Guessed NBT must be a list of strings", list.getSubtagType(), NBTType.TAG_String);
            assertEquals(0, list.getLength());
        }
    }

}
