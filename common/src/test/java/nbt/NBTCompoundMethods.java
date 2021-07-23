package nbt;

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray;
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray;
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class NBTCompoundMethods {

    @Test
    public void setString() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.String("hi"));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTString);
        assertEquals("hi", ((NBTString)nbt.get("a")).getValue());
    }

    @Test
    public void setShort() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Short((short) 42));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTShort);
        assertEquals(42, ((NBTShort)nbt.get("a")).getValue());
    }

    @Test
    public void setByte() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Byte((byte) 0xCA));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTByte);
        assertEquals((byte)0xCA, ((NBTByte)nbt.get("a")).getValue());
    }

    @Test
    public void setInt() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Int(0xBABE));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTInt);
        assertEquals(0xBABE, ((NBTInt)nbt.get("a")).getValue());
    }

    @Test
    public void setLong() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Long(0xCAFEBABEL));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTLong);
        assertEquals(0xCAFEBABEL, ((NBTLong)nbt.get("a")).getValue());
    }

    @Test
    public void setFloat() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Float(0.5f));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTFloat);
        assertEquals(0.5f, ((NBTFloat)nbt.get("a")).getValue(), 10e-16);
    }

    @Test
    public void setDouble() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Double(.25));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTDouble);
        assertEquals(0.25, ((NBTDouble)nbt.get("a")).getValue(), 10e-16);
    }

    @Test
    public void setByteArray() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.ByteArray((byte) 1, (byte) 2, (byte) 3));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTByteArray);
        assertEquals(new ImmutableByteArray((byte) 1, (byte) 2, (byte) 3), ((NBTByteArray)nbt.get("a")).getValue());
    }

    @Test
    public void setIntArray() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.IntArray(1, 2, 3));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTIntArray);
        assertEquals(new ImmutableIntArray(1, 2, 3), ((NBTIntArray)nbt.get("a")).getValue());
    }

    @Test
    public void setLongArray() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.LongArray(1, 2, 3));
        });

        assertEquals(1, nbt.getSize());
        assertTrue(nbt.get("a") instanceof NBTLongArray);
        assertEquals(new ImmutableLongArray(1, 2, 3), ((NBTLongArray)nbt.get("a")).getValue());
    }

    @Test
    public void getAsLong() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Int(42));
        });

        assertEquals(1, nbt.getSize());
        assertNull(nbt.getLong("a"));
        assertEquals(42L, nbt.getAsLong("a").longValue());
    }

    @Test
    public void getAsByte() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Int(42));
        });

        assertEquals(1, nbt.getSize());
        assertNull(nbt.getByte("a"));
        assertEquals(42, nbt.getAsByte("a").byteValue());
    }

    @Test
    public void getAsDouble() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Int(42));
        });

        assertEquals(1, nbt.getSize());
        assertNull(nbt.getDouble("a"));
        assertEquals(42.0, nbt.getAsDouble("a").doubleValue(), 10e-6);
    }

    @Test
    public void getAsFloat() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Int(42));
        });

        assertEquals(1, nbt.getSize());
        assertNull(nbt.getFloat("a"));
        assertEquals(42f, nbt.getAsFloat("a").floatValue(), 10e-6f);
    }

    @Test
    public void getAsInt() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Long(42));
        });

        assertEquals(1, nbt.getSize());
        assertNull(nbt.getInt("a"));
        assertEquals(42, nbt.getAsInt("a").intValue());
    }

    @Test
    public void getAsShort() {

        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.Long(42));
        });

        assertEquals(1, nbt.getSize());
        assertNull(nbt.getShort("a"));
        assertEquals(42, nbt.getAsShort("a").shortValue());
    }

    @Test
    public void removeTag() {
        var nbt = NBTCompound.compound((map) -> {
            map.put("a", NBT.String("value"));
        });
        assertEquals(1, nbt.getSize());
        nbt.modify((map) -> {
            map.remove("a");
        });
        assertEquals(0, nbt.getSize());
        assertNull(nbt.get("a"));
    }
}
