package snbt;

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray;
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray;
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray;
import org.jglrxavpok.hephaistos.nbt.*;
import org.jglrxavpok.hephaistos.parser.SNBTParser;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class SNBTParserTests {

    @Test
    public void parseInt() throws NBTException {
        String snbt = "42";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTInt);
            assertEquals(42, ((NBTInt) element).getValue());
        }
    }

    @Test
    public void parseNegativeInt() throws NBTException {
        String snbt = "-99";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTInt);
            assertEquals(-99, ((NBTInt) element).getValue());
        }
    }

    @Test
    public void parseByte() throws NBTException {
        String snbt = "42b";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTByte);
            assertEquals(42, ((NBTByte) element).getValue());
        }
    }

    @Test
    public void parseNegativeByte() throws NBTException {
        String snbt = "-99B";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTByte);
            assertEquals(-99, ((NBTByte) element).getValue());
        }
    }

    @Test
    public void parseShort() throws NBTException {
        String snbt = "22000s";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTShort);
            assertEquals(22000, ((NBTShort) element).getValue());
        }
    }

    @Test
    public void parseNegativeShort() throws NBTException {
        String snbt = "-21999S";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTShort);
            assertEquals(-21999, ((NBTShort) element).getValue());
        }
    }

    @Test
    public void parseLong() throws NBTException {
        String snbt = "1234567890000000l";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTLong);
            assertEquals(1234567890000000L, ((NBTLong) element).getValue());
        }
    }

    @Test
    public void parseNegativeLong() throws NBTException {
        String snbt = "-9876543210000000L";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTLong);
            assertEquals(-9876543210000000L, ((NBTLong) element).getValue());
        }
    }

    @Test
    public void parseFloat() throws NBTException {
        String snbt = "3.14f";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTFloat);
            assertEquals(3.14f, ((NBTFloat) element).getValue(), 10e-6);
        }
    }

    @Test
    public void parseNegativeFloat() throws NBTException {
        String snbt = "-6.28F";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTFloat);
            assertEquals(-6.28f, ((NBTFloat) element).getValue(), 10e-6);
        }
    }

    @Test
    public void parseDoubleWithTerminal() throws NBTException {
        String snbt = "2.14d";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTDouble);
            assertEquals(2.14f, ((NBTDouble) element).getValue(), 10e-6);
        }
    }

    @Test
    public void parseNegativeDoubleWithTerminal() throws NBTException {
        String snbt = "-1.184d";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTDouble);
            assertEquals(-1.184D, ((NBTDouble) element).getValue(), 10e-6);
        }
    }

    @Test
    public void parseDoubleWithoutTerminal() throws NBTException {
        String snbt = "25.987";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTDouble);
            assertEquals(25.987, ((NBTDouble) element).getValue(), 10e-6);
        }
    }

    @Test
    public void parseNegativeDoubleWithoutTerminal() throws NBTException {
        String snbt = "-111.11";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTDouble);
            assertEquals(-111.11, ((NBTDouble) element).getValue(), 10e-6);
        }
    }

    @Test
    public void parseIntegerArray() throws NBTException {
        String snbt = "[I;456,987,10]";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTIntArray);
            assertEquals(new ImmutableIntArray(456, 987, 10), ((NBTIntArray) element).getValue());
        }
    }

    @Test
    public void parseByteArray() throws NBTException {
        String snbt = "[B;10b,-11B,127b]";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTByteArray);
            assertEquals(ImmutableByteArray.from(10, -11, 127), ((NBTByteArray) element).getValue());
        }
    }

    @Test
    public void parseLongArray() throws NBTException {
        String snbt = "[L;123456789l,-1563487L,16354658L]";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTLongArray);
            assertEquals(new ImmutableLongArray(123456789L, -1563487L, 16354658L), ((NBTLongArray) element).getValue());
        }
    }

    @Test
    public void parseBoolean() throws NBTException {
        try(SNBTParser parser = new SNBTParser(new StringReader("false"))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTByte);
            assertEquals(0, ((NBTByte) element).getValue());
        }

        try(SNBTParser parser = new SNBTParser(new StringReader("true"))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTByte);
            assertEquals(1, ((NBTByte) element).getValue());
        }
    }
}
