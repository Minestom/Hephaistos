package nbt;

import kotlin.Pair;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class NBTSaving {

    private ByteArrayOutputStream baos;

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveByte(boolean compressed) throws IOException, NBTException {
        NBTByte nbt = new NBTByte((byte)42);
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveShort(boolean compressed) throws IOException, NBTException {
        NBTShort nbt = new NBTShort((short)1);
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveInt(boolean compressed) throws IOException, NBTException {
        NBTInt nbt = new NBTInt(0x42);
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveLong(boolean compressed) throws IOException, NBTException {
        NBTLong nbt = new NBTLong(0xCAFEBABEL);
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveDouble(boolean compressed) throws IOException, NBTException {
        NBTDouble nbt = new NBTDouble(0.25);
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveFloat(boolean compressed) throws IOException, NBTException {
        NBTFloat nbt = new NBTFloat(0.5f);
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveString(boolean compressed) throws IOException, NBTException {
        NBTString nbt = new NBTString("AAA");
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveList(boolean compressed) throws IOException, NBTException {
        NBTList<NBTString> nbt = new NBTList<>(NBTTypes.TAG_String);
        nbt.add(new NBTString("A"));
        nbt.add(new NBTString("B"));
        nbt.add(new NBTString("C"));
        nbt.add(new NBTString("D"));
        test(nbt, compressed);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void saveCompound(boolean compressed) throws IOException, NBTException {
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
        test(nbt, compressed);
    }

    private <T extends NBT> void test(T nbt, boolean compressed) throws IOException, NBTException {
        T saved = saveAndRead(nbt, compressed);
        assertNotSame(saved, nbt); // don't cheat by returning the same object
        assertEquals(nbt, saved);
    }


    private <T extends NBT> T saveAndRead(NBT tag, boolean compressed) throws IOException, NBTException {
        NBTWriter output = output(compressed);
        output.writeNamed("a", tag);
        output.close();
        Pair<String, NBT> namedTag = input(compressed).readNamed();
        assertEquals("a", namedTag.getFirst());
        assertEquals(tag.getClass(), namedTag.getSecond().getClass());
        return (T) namedTag.getSecond();
    }


    private NBTReader input(boolean compressed) throws IOException {
        return new NBTReader(new ByteArrayInputStream(baos.toByteArray()), compressed);
    }

    private NBTWriter output(boolean compressed) {
        return new NBTWriter(baos, compressed);
    }

    @BeforeEach
    public void init() {
        baos = new ByteArrayOutputStream();
    }

    @AfterEach
    public void clean() {
        baos = null;
    }


}
