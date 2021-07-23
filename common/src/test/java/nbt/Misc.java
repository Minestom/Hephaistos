package nbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
