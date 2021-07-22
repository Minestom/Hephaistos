package nbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Misc {

    @Test
    public void snbtArrays() {
        NBT array = new NBTByteArray(new byte[]{1, 2, 3});
        assertEquals("[B;1B,2B,3B]", array.toSNBT());

        array = new NBTIntArray(new int[]{1, 2, 3});
        assertEquals("[I;1,2,3]", array.toSNBT());

        array = new NBTLongArray(new long[]{1, 2, 3});
        assertEquals("[L;1L,2L,3L]", array.toSNBT());
    }

    @Test
    public void ensureSubElementsOfListAreAllSameType() {
        NBTList<NBT> genericList = new NBTList<>(NBTTypes.TAG_String);
        genericList.add(new NBTString("Some value"));
        genericList.add(new NBTInt(42));
    }
}
