package nbt;

import kotlin.Pair;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class NBTSaving {

    private ByteArrayOutputStream byteArrayOutputStream;

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveByte(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTByte nbt = NBT.Byte(42);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveByteArray(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTByteArray nbt = NBT.ByteArray(42, 53, -56);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveIntArray(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTIntArray nbt = NBT.IntArray(-42, -53, 56, 0xFFC0C0C0);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveLongArray(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTLongArray nbt = NBT.LongArray(0xDEADBEEF, 0xCAFEBABE, 0xAAAAAAA);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveShort(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTShort nbt = NBT.Short(1);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveInt(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTInt nbt = NBT.Int(0x42);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveLong(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTLong nbt = NBT.Long(0xCAFEBABEL);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveDouble(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTDouble nbt = NBT.Double(0.25);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveFloat(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTFloat nbt = NBT.Float(0.5f);
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveString(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTString nbt = NBT.String("AAA");
        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveList(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTList<NBTString> nbt = NBT.List(
                NBTType.TAG_String,
                NBT.String("A"), NBT.String("B"), NBT.String("C"), NBT.String("D")
        );

        test(nbt, compressedProcesser);
    }

    @ParameterizedTest
    @ArgumentsSource(CompressedModeProvider.class)
    public void saveCompound(CompressedProcesser compressedProcesser) throws IOException, NBTException {
        var compound = NBT.Compound((root) -> {
            root.put("byteArray", NBT.ByteArray(1, 2, 3));
            root.put("byte", NBT.Byte(0x42));
            root.put("double", NBT.Double(0.5));
            root.put("string", NBT.String("ABC"));
            root.put("float", NBT.Float(0.25f));
            root.put("int", NBT.Int(4567));
            root.put("intarray", NBT.IntArray(42, 42, 25464, 454, -10));
            root.put("long", NBT.Long(30000000000L));
            root.put("longarray", NBT.LongArray(30000000000L, -30000000000L, 130000000000L));
            root.put("short", NBT.Short(-10));
        });

        test(compound, compressedProcesser);
    }

    private <T extends NBT> void test(T nbt, CompressedProcesser compressedProcesser) throws IOException, NBTException {
        T saved = saveAndRead(nbt, compressedProcesser);
        assertEquals(nbt, saved);
    }


    private <T extends NBT> T saveAndRead(NBT tag, CompressedProcesser compressedProcesser) throws IOException, NBTException {
        NBTWriter output = output(compressedProcesser);
        output.writeNamed("a", tag);
        output.close();
        Pair<String, NBT> namedTag = input(compressedProcesser).readNamed();
        assertEquals("a", namedTag.getFirst());
        assertEquals(tag.getClass(), namedTag.getSecond().getClass());
        return (T) namedTag.getSecond();
    }


    private NBTReader input(CompressedProcesser compressedProcesser) {
        return NBTReader.fromArray(byteArrayOutputStream.toByteArray(), compressedProcesser);
    }

    private NBTWriter output(CompressedProcesser compressedProcesser) {
        return new NBTWriter(byteArrayOutputStream, compressedProcesser);
    }

    @BeforeEach
    public void init() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @AfterEach
    public void clean() {
        byteArrayOutputStream = null;
    }


}
