import org.jglrxavpok.hephaistos.json.NBTGsonReader;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class TestReaderPrimitives {

    @Test
    public void readString() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("\"AAA\""))) {
            NBTString string = reader.read(NBTString.class);
            assertEquals("AAA", string.getValue());
        }
    }

    @Test
    public void guessString() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("\"AAA\""))) {
            NBT string = reader.readWithGuess();
            assertTrue("Must be a NBTString", string instanceof NBTString);
            assertEquals("AAA", ((NBTString)string).getValue());
        }
    }

    @Test
    public void readFloat() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("42.5"))) {
            NBTFloat element = reader.read(NBTFloat.class);
            assertEquals(42.5, element.getValue(), 10e-6);
        }
    }

    @Test
    public void readDouble() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("41.5"))) {
            NBTDouble element = reader.read(NBTDouble.class);
            assertEquals(41.5, element.getValue(), 10e-6);
        }
    }

    @Test
    public void guessDouble() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("42.5"))) {
            NBT element = reader.readWithGuess();
            assertTrue("Must be a NBTDouble", element instanceof NBTDouble);
            assertEquals(42.5, ((NBTDouble)element).getValue(), 10e-6);
        }
    }

    @Test
    public void readLong() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("425"))) {
            NBTLong element = reader.read(NBTLong.class);
            assertEquals(425, element.getValue());
        }
    }

    @Test
    public void readInt() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("425"))) {
            NBTInt element = reader.read(NBTInt.class);
            assertEquals(425, element.getValue());
        }
    }

    @Test
    public void readShort() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("425"))) {
            NBTShort element = reader.read(NBTShort.class);
            assertEquals(425, element.getValue());
        }
    }

    @Test
    public void readByte() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("42"))) {
            NBTByte element = reader.read(NBTByte.class);
            assertEquals(42, element.getValue());
        }
    }

    @Test
    public void guessLong() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("425"))) {
            NBT element = reader.readWithGuess();
            assertTrue("Must be a NBTLong", element instanceof NBTLong);
            assertEquals(425, ((NBTLong)element).getValue());
        }
    }
    @Test
    public void nullShouldBeGuessedAsEmptyString() {
        try(NBTGsonReader reader = new NBTGsonReader(new StringReader("null"))) {
            NBT element = reader.readWithGuess();
            assertTrue("Must be a NBTString", element instanceof NBTString);
            assertEquals("", ((NBTString)element).getValue());
        }
    }
}
