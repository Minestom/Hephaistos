package nbt;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultValues {

    @Test
    public void getOrDefault() {
        NBTCompound compound = new NBTCompound();
        int value = compound.getOrDefault("key", 1);
        assertEquals(1, value);

        float value2 = compound.getOrDefault("key", 0.1f);
        assertEquals(0.1f, value2, 10e-16);

        double value3 = compound.getOrDefault("key", 0.2);
        assertEquals(0.2, value3, 10e-16);

        String value4 = compound.getOrDefault("key", "my value");
        assertEquals("my value", value4);
    }
}
