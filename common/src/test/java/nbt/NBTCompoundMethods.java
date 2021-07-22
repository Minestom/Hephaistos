package nbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class NBTCompoundMethods {

    private NBTCompound nbt;

    @BeforeEach
    void init() {
        nbt = new NBTCompound();
    }

    @Test
    public void setString() {
        nbt.setString("a", "hi");
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTString);
        assertEquals("hi", ((NBTString)nbt.get("a")).getValue());
    }

    @Test
    public void getString() {
        nbt.set("a", new NBTString("hi"));
        assertEquals(1, nbt.getSize());
        assertEquals("hi", nbt.getString("a"));
    }

    @Test
    public void setShort() {
        nbt.setShort("a", (short) 42);
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTShort);
        assertEquals(42, ((NBTShort)nbt.get("a")).getValue());
    }

    @Test
    public void getShort() {
        nbt.set("a", new NBTShort((short) 42));
        assertEquals(1, nbt.getSize());
        assertEquals(42, nbt.getShort("a").shortValue());
    }

    @Test
    public void setByte() {
        nbt.setByte("a", (byte) 0xCA);
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTByte);
        assertEquals((byte)0xCA, ((NBTByte)nbt.get("a")).getValue());
    }

    @Test
    public void getByte() {
        nbt.set("a", new NBTByte((byte) 0xCA));
        assertEquals(1, nbt.getSize());
        assertEquals((byte)0xCA, nbt.getByte("a").byteValue());
    }

    @Test
    public void setInt() {
        nbt.setInt("a", 0xBABE);
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTInt);
        assertEquals(0xBABE, ((NBTInt)nbt.get("a")).getValue());
    }

    @Test
    public void getInt() {
        nbt.set("a", new NBTInt(0xBABE));
        assertEquals(1, nbt.getSize());
        assertEquals(0xBABE, nbt.getInt("a").intValue());
    }

    @Test
    public void setLong() {
        nbt.setLong("a", 0xCAFEBABEL);
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTLong);
        assertEquals(0xCAFEBABEL, ((NBTLong)nbt.get("a")).getValue());
    }

    @Test
    public void getLong() {
        nbt.set("a", new NBTLong(0xCAFEBABEL));
        assertEquals(1, nbt.getSize());
        assertEquals(0xCAFEBABEL, nbt.getLong("a").longValue());
    }

    @Test
    public void setFloat() {
        nbt.setFloat("a", 0.5f);
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTFloat);
        assertEquals(0.5f, ((NBTFloat)nbt.get("a")).getValue(), 10e-16);
    }

    @Test
    public void getFloat() {
        nbt.set("a", new NBTFloat(0.5f));
        assertEquals(1, nbt.getSize());
        assertEquals(0.5f, nbt.getFloat("a").floatValue(), 10e-16);
    }

    @Test
    public void setDouble() {
        nbt.setDouble("a", 0.25);
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTDouble);
        assertEquals(0.25, ((NBTDouble)nbt.get("a")).getValue(), 10e-16);
    }

    @Test
    public void getDouble() {
        nbt.set("a", new NBTDouble(0.25));
        assertEquals(1, nbt.getSize());
        assertEquals(0.25, nbt.getDouble("a").doubleValue(), 10e-16);
    }

    @Test
    public void setByteArray() {
        nbt.setByteArray("a", new byte[] { 1, 2, 3 });
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTByteArray);
        assertArrayEquals(new byte[] { 1, 2, 3 }, ((NBTByteArray)nbt.get("a")).getValue());
    }

    @Test
    public void getByteArray() {
        nbt.set("a", new NBTByteArray(new byte[] { 1, 2, 3 }));
        assertEquals(1, nbt.getSize());
        assertArrayEquals(new byte[] { 1, 2, 3 }, nbt.getByteArray("a"));
    }

    @Test
    public void setIntArray() {
        nbt.setIntArray("a", new int[] { 1, 2, 3 });
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTIntArray);
        assertArrayEquals(new int[] { 1, 2, 3 }, ((NBTIntArray)nbt.get("a")).getValue());
    }

    @Test
    public void getIntArray() {
        nbt.set("a", new NBTIntArray(new int[] { 1, 2, 3 }));
        assertEquals(1, nbt.getSize());
        assertArrayEquals(new int[] { 1, 2, 3 }, nbt.getIntArray("a"));
    }

    @Test
    public void setLongArray() {
        nbt.setLongArray("a", new long[] { 1, 2, 3 });
        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTLongArray);
        assertArrayEquals(new long[] { 1, 2, 3 }, ((NBTLongArray)nbt.get("a")).getValue());
    }

    @Test
    public void getLongArray() {
        nbt.set("a", new NBTLongArray(new long[] { 1, 2, 3 }));
        assertEquals(1, nbt.getSize());
        assertArrayEquals(new long[] { 1, 2, 3 }, nbt.getLongArray("a"));
    }

    @Test
    public void getAsLong() {
        nbt.set("a", new NBTInt(42));
        assertEquals(1, nbt.getSize());
        assertEquals(null, nbt.getLong("a"));
        assertEquals(42L, nbt.getAsLong("a").longValue());
    }

    @Test
    public void getAsByte() {
        nbt.set("a", new NBTInt(42));
        assertEquals(1, nbt.getSize());
        assertEquals(null, nbt.getByte("a"));
        assertEquals(42, nbt.getAsByte("a").byteValue());
    }

    @Test
    public void getAsDouble() {
        nbt.set("a", new NBTInt(42));
        assertEquals(1, nbt.getSize());
        assertEquals(null, nbt.getDouble("a"));
        assertEquals(42.0, nbt.getAsDouble("a").doubleValue(), 10e-6);
    }

    @Test
    public void getAsFloat() {
        nbt.set("a", new NBTInt(42));
        assertEquals(1, nbt.getSize());
        assertEquals(null, nbt.getFloat("a"));
        assertEquals(42f, nbt.getAsFloat("a").floatValue(), 10e-6f);
    }

    @Test
    public void getAsInt() {
        nbt.set("a", new NBTLong(42));
        assertEquals(1, nbt.getSize());
        assertEquals(null, nbt.getInt("a"));
        assertEquals(42, nbt.getAsInt("a").intValue());
    }

    @Test
    public void getAsShort() {
        nbt.set("a", new NBTLong(42));
        assertEquals(1, nbt.getSize());
        assertEquals(null, nbt.getShort("a"));
        assertEquals(42, nbt.getAsShort("a").shortValue());
    }

    @Test
    public void removeTag() {
        nbt.set("a", new NBTString("test value"));
        assertEquals(1, nbt.getSize());
        nbt.removeTag("a");
        assertEquals(0, nbt.getSize());
        assertNull(nbt.get("a"));
    }

    @AfterEach
    public void clean() {
        nbt.clear();
        nbt = null;
    }
}
