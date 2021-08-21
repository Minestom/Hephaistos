package nbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Misc {

    @Test
    public void snbtArrays() {
        NBT array = NBT.ByteArray(1, 2, 3);
        assertEquals("[B;1B,2B,3B]", array.toSNBT());

        array = NBT.IntArray(1, 2, 3);
        assertEquals("[I;1,2,3]", array.toSNBT());

        array = NBT.LongArray(1, 2, 3);
        assertEquals("[L;1L,2L,3L]", array.toSNBT());
    }

    @Test
    public void truthness() {
        NBTByte shouldBeFalse = NBT.Byte(0);
        assertFalse(shouldBeFalse.asBoolean());

        NBTByte shouldBeTrue = NBT.Byte(1);
        assertTrue(shouldBeTrue.asBoolean());

        NBTByte shouldBeTrue2 = NBT.Byte(-1);
        assertTrue(shouldBeTrue2.asBoolean());

        NBTByte shouldBeTrueToo = NBT.Byte(42);
        assertTrue(shouldBeTrueToo.asBoolean());
    }

    @Test
    public void equalityBetweenMutableAndNonMutable() {
        NBTCompound myCompound = NBT.Compound(n -> {
            n.setString("test", "aaa");
        });

        MutableNBTCompound mutableCopy = new MutableNBTCompound(myCompound);
        assertEquals(myCompound, mutableCopy);
        assertEquals(mutableCopy, myCompound);
        mutableCopy.setString("test", "bbb");
        assertNotEquals(myCompound, mutableCopy);
        assertNotEquals(mutableCopy, myCompound);
    }

    @Test
    public void mutableNBTConstructorShouldCopyNBTCompound() {
        NBTCompound myCompound = NBT.Compound(n -> {
            n.setString("test", "aaa");
        });

        MutableNBTCompound mutableCopy = new MutableNBTCompound(myCompound);
        assertEquals("aaa", mutableCopy.getString("test"));
        mutableCopy.setString("test", "bbb");
        assertEquals("bbb", mutableCopy.getString("test"));
    }
}
