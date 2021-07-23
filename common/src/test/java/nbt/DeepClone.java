package nbt;

import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeepClone {

    @Test
    public void list() {
        NBTList<NBTString> list = new NBTList<>(NBTTypes.TAG_String);
        list.add(NBT.String("Some text"));
        list.add(NBT.String("Some more text"));

        assertEquals(list, list.deepClone());
        assertNotSame(list, list.deepClone());
    }
}
