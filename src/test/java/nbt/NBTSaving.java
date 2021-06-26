package nbt;

import kotlin.Pair;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class NBTSaving {

    private final boolean compressed;
    private ByteArrayOutputStream baos;

    @Parameterized.Parameters
    public static Collection<Boolean> params() {
        return Arrays.asList(false, true);
    }

    public NBTSaving(boolean compressed) {
        this.compressed = compressed;
    }

    @Test
    public void saveByte() throws IOException, NBTException {
        NBTByte nbt = new NBTByte((byte)42);
        test(nbt);
    }

    @Test
    public void saveShort() throws IOException, NBTException {
        NBTShort nbt = new NBTShort((short)1);
        test(nbt);
    }

    @Test
    public void saveInt() throws IOException, NBTException {
        NBTInt nbt = new NBTInt(0x42);
        test(nbt);
    }

    @Test
    public void saveLong() throws IOException, NBTException {
        NBTLong nbt = new NBTLong(0xCAFEBABEL);
        test(nbt);
    }

    @Test
    public void saveDouble() throws IOException, NBTException {
        NBTDouble nbt = new NBTDouble(0.25);
        test(nbt);
    }

    @Test
    public void saveFloat() throws IOException, NBTException {
        NBTFloat nbt = new NBTFloat(0.5f);
        test(nbt);
    }

    @Test
    public void saveString() throws IOException, NBTException {
        NBTString nbt = new NBTString("AAA");
        test(nbt);
    }

    @Test
    public void saveList() throws IOException, NBTException {
        NBTList<NBTString> nbt = new NBTList<>(NBTType.TAG_String);
        nbt.add(new NBTString("A"));
        nbt.add(new NBTString("B"));
        nbt.add(new NBTString("C"));
        nbt.add(new NBTString("D"));
        test(nbt);
    }

    @Test
    public void saveCompound() throws IOException, NBTException {
        NBTCompound nbt = new NBTCompound();
        nbt.setByteArray("bytearray", new byte[]{1,2,3});
        nbt.setByte("byte", (byte) 0x42);
        nbt.setDouble("double", 0.5);
        nbt.setString("string", "ABC");
        nbt.setFloat("float", 0.25f);
        nbt.setInt("int", 4657);
        nbt.setIntArray("intarray", new int[] { 42, 42, 25464, 454, -10 });
        nbt.setLong("long", 30000000000L);
        nbt.setLongArray("longarray", new long[]{30000000000L, -30000000000L, 130000000000L});
        nbt.setShort("short", (short) -10);
        test(nbt);
    }

    private <T extends MutableNBT> void test(T nbt) throws IOException, NBTException {
        T saved = saveAndRead(nbt);
        assertTrue(saved != nbt); // don't cheat by returning the same object
        assertEquals(nbt, saved);
    }

    private <T extends MutableNBT> T saveAndRead(MutableNBT tag) throws IOException, NBTException {
        NBTWriter output = output();
        output.writeNamed("a", tag);
        output.close();
        Pair<String, MutableNBT<?>> namedTag = input().readNamed();
        assertEquals("a", namedTag.getFirst());
        assertEquals(tag.getClass(), namedTag.getSecond().getClass());
        return (T) namedTag.getSecond();
    }

    private NBTReader input() throws IOException {
        return new NBTReader(new ByteArrayInputStream(baos.toByteArray()), compressed);
    }

    private NBTWriter output() {
        return new NBTWriter(baos, compressed);
    }

    @Before
    public void init() {
        baos = new ByteArrayOutputStream();
    }

    @After
    public void clean() {
        baos = null;
    }


}
