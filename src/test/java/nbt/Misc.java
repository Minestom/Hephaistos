package nbt;

import org.jglrxavpok.nbt.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class Misc {

    @Test
    public void snbtArrays() {
        NBT array = new NBTByteArray(new byte[]{1, 2, 3});
        Assert.assertEquals("[B;1B,2B,3B]", array.toSNBT());

        array = new NBTIntArray(new int[]{1, 2, 3});
        Assert.assertEquals("[I;1,2,3]", array.toSNBT());

        array = new NBTLongArray(new long[]{1, 2, 3});
        Assert.assertEquals("[L;1L,2L,3L]", array.toSNBT());
    }
}
