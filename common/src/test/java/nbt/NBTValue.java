package nbt;

import org.jglrxavpok.hephaistos.collections.ImmutableByteArray;
import org.jglrxavpok.hephaistos.collections.ImmutableIntArray;
import org.jglrxavpok.hephaistos.collections.ImmutableLongArray;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NBTValue {

    @Test
    public void runTime() {
        assertType(Byte.class, NBT.Byte(0));
        assertType(Short.class, NBT.Short(0));
        assertType(Integer.class, NBT.Int(0));
        assertType(Long.class, NBT.Long(0));
        assertType(Float.class, NBT.Float(0));
        assertType(Double.class, NBT.Double(0));
        assertType(String.class, NBT.String(""));

        assertType(ImmutableByteArray.class, NBT.ByteArray());
        assertType(ImmutableIntArray.class, NBT.IntArray());
        assertType(ImmutableLongArray.class, NBT.LongArray());
    }

    @Test
    public void compileTime() {
        byte v1 = NBT.Byte(0).getValue();
        Byte v2 = NBT.Byte(0).getValue();

        short v3 = NBT.Short(0).getValue();
        Short v4 = NBT.Short(0).getValue();

        int v5 = NBT.Int(0).getValue();
        Integer v6 = NBT.Int(0).getValue();

        long v7 = NBT.Long(0).getValue();
        Long v8 = NBT.Long(0).getValue();

        float v9 = NBT.Float(0).getValue();
        Float v10 = NBT.Float(0).getValue();

        double v11 = NBT.Double(0).getValue();
        Double v12 = NBT.Double(0).getValue();

        String v13 = NBT.String("").getValue();

        ImmutableByteArray v14 = NBT.ByteArray().getValue();
        ImmutableIntArray v15 = NBT.IntArray().getValue();
        ImmutableLongArray v16 = NBT.LongArray().getValue();

        String v17 = NBT.String("").getValue();

        NBT nbt = NBT.Byte(0);
        Object v18 = nbt.getValue();
    }

    void assertType(Class<?> expected, NBT nbt) {
        Assertions.assertInstanceOf(expected, nbt.getValue());
    }
}
