package snbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class SNBTParserTests {

    @Test
    public void parseInt() {
        String snbt = "42";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTInt);
            assertEquals(42, ((NBTInt) element).getValue());
        }
    }

    @Test
    public void parseNegativeInt() {
        String snbt = "-99";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTInt);
            assertEquals(-99, ((NBTInt) element).getValue());
        }
    }

    @Test
    public void parseByte() {
        String snbt = "42b";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTByte);
            assertEquals(42, ((NBTByte) element).getValue());
        }
    }

    @Test
    public void parseNegativeByte() {
        String snbt = "-99B";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTByte);
            assertEquals(-99, ((NBTByte) element).getValue());
        }
    }

    @Test
    public void parseShort() {
        String snbt = "22000s";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTShort);
            assertEquals(22000, ((NBTShort) element).getValue());
        }
    }

    @Test
    public void parseNegativeShort() {
        String snbt = "-21999S";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTShort);
            assertEquals(-21999, ((NBTShort) element).getValue());
        }
    }

    @Test
    public void parseLong() {
        String snbt = "1234567890000000l";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTLong);
            assertEquals(1234567890000000L, ((NBTLong) element).getValue());
        }
    }

    @Test
    public void parseNegativeLong() {
        String snbt = "-9876543210000000L";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTLong);
            assertEquals(-9876543210000000L, ((NBTLong) element).getValue());
        }
    }

    @Test
    public void parseFloat() {
        String snbt = "3.14f";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTFloat);
            assertEquals(3.14f, ((NBTFloat) element).getValue(), 10e-6);
        }
    }

    @Test
    public void parseNegativeFloat() {
        String snbt = "-6.28F";
        try(SNBTParser parser = new SNBTParser(new StringReader(snbt))) {
            NBT element = parser.parse();
            assertTrue(element instanceof NBTFloat);
            assertEquals(-6.28f, ((NBTFloat) element).getValue(), 10e-6);
        }
    }

}
