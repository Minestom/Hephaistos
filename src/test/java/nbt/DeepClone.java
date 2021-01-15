package nbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeepClone {

    @Test
    public void integer() {
        NBTInt nbt = new NBTInt(42);
        assertEquals(nbt.deepClone(), new NBTInt(42));
        assertNotSame(nbt, nbt.deepClone());
    }

    @Test
    public void floatingPoint() {
        NBTFloat nbt = new NBTFloat(0.1f);
        assertEquals(nbt.deepClone(), new NBTFloat(0.1f));
        assertNotSame(nbt, nbt.deepClone());
    }

    @Test
    public void doubleFloatingPoint() {
        NBTDouble nbt = new NBTDouble(0.25);
        assertEquals(nbt.deepClone(), new NBTDouble(0.25));
        assertNotSame(nbt, nbt.deepClone());
    }

    @Test
    public void byteNBT() {
        NBTByte nbt = new NBTByte((byte) 0x42);
        assertEquals(nbt.deepClone(), new NBTByte((byte) 0x42));
        assertNotSame(nbt, nbt.deepClone());
    }

    @Test
    public void shortNBT() {
        NBTShort nbt = new NBTShort((short) 256);
        assertEquals(nbt.deepClone(), new NBTShort((short) 256));
        assertNotSame(nbt, nbt.deepClone());
    }

    @Test
    public void string() {
        NBTString nbt = new NBTString("Hello there");
        assertEquals(nbt.deepClone(), new NBTString("Hello there"));
        assertNotSame(nbt, nbt.deepClone());
        assertSame(nbt.getValue(), nbt.deepClone().getValue());
    }

    @Test
    public void byteArray() {
        NBTByteArray nbt = new NBTByteArray(new byte[] {
                (byte) 0xCA, (byte) 0xFE, (byte) 0xBE, (byte) 0xEF
        });
        assertEquals(nbt.deepClone(), new NBTByteArray(new byte[] {
                (byte) 0xCA, (byte) 0xFE, (byte) 0xBE, (byte) 0xEF
        }));
        assertNotSame(nbt, nbt.deepClone());
        assertNotSame(nbt.getValue(), nbt.deepClone().getValue());
    }

    @Test
    public void intArray() {
        NBTIntArray nbt = new NBTIntArray(new int[] {
                0xFEFEFE, 0xCECECE, 0x08987564
        });
        assertEquals(nbt.deepClone(), new NBTIntArray(new int[] {
                0xFEFEFE, 0xCECECE, 0x08987564
        }));
        assertNotSame(nbt, nbt.deepClone());
        assertNotSame(nbt.getValue(), nbt.deepClone().getValue());
    }

    @Test
    public void longArray() {
        NBTLongArray nbt = new NBTLongArray(new long[] {
                0xFEFEFEL, 0xCECECEL, 0x08987564L
        });
        assertEquals(nbt.deepClone(), new NBTLongArray(new long[] {
                0xFEFEFEL, 0xCECECEL, 0x08987564L
        }));
        assertNotSame(nbt, nbt.deepClone());
        assertNotSame(nbt.getValue(), nbt.deepClone().getValue());
    }

    @Test
    public void list() {
        NBTList<NBTString> list = new NBTList<>(NBTTypes.TAG_String);
        list.add(new NBTString("Some text"));
        list.add(new NBTString("Some more text"));

        assertEquals(list, list.deepClone());
        assertNotSame(list, list.deepClone());
        assertNotSame(list.get(0), list.deepClone().get(0));
        assertNotSame(list.get(1), list.deepClone().get(1));
    }

    @Test
    public void compound() {
        NBTCompound nbt = new NBTCompound();
        NBTCompound innerNBT = new NBTCompound();

        innerNBT.setString("mykey", "My value");

        nbt.setString("text", "My text");
        nbt.setInt("key", 0x814C);
        nbt.set("inner", innerNBT);

        assertEquals(nbt, nbt.deepClone());
        assertNotSame(nbt, nbt.deepClone());

        assertNotSame(nbt.get("text"), nbt.deepClone().get("text"));
        assertEquals(nbt.get("text"), nbt.deepClone().get("text"));

        assertNotSame(nbt.get("key"), nbt.deepClone().get("key"));
        assertEquals(nbt.get("key"), nbt.deepClone().get("key"));

        assertNotSame(nbt.get("inner"), nbt.deepClone().get("inner"));
        assertEquals(nbt.get("inner"), nbt.deepClone().get("inner"));
    }
}
