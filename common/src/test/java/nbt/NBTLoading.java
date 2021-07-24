package nbt;

import kotlin.Pair;
import org.jglrxavpok.hephaistos.collections.ImmutableByteArray;
import org.jglrxavpok.hephaistos.nbt.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class NBTLoading {

    @Test
    public void helloWorld() throws IOException, NBTException {
        try(NBTReader reader = new NBTReader(NBTLoading.class.getResourceAsStream("/hello_world.nbt"), CompressedMode.NONE)) {
            Pair<String, NBT> namedTag = reader.readNamed();
            assertEquals("hello world", namedTag.getFirst());
            NBT tag = namedTag.getSecond();
            if(tag instanceof NBTCompound) {
                NBTCompound compound = (NBTCompound)tag;
                assertEquals(1, compound.getSize());
                assertTrue(compound.containsKey("name"), "Must contain tag 'name'");
                NBT subTag = compound.get("name");
                assertNotNull(subTag);
                assertTrue(subTag instanceof NBTString);
                assertEquals("Bananrama", ((NBTString)subTag).getValue());
            } else {
                fail("Root tag is not TAG_Compound");
            }
        }
    }

    @Test
    public void bigtest() throws IOException, NBTException {
        try(NBTReader reader = new NBTReader(NBTLoading.class.getResourceAsStream("/bigtest.nbt"), CompressedMode.GZIP)) {
            Pair<String, NBT> namedTag = reader.readNamed();
            assertEquals("Level", namedTag.getFirst());
            NBT tag = namedTag.getSecond();
            assertTrue(tag instanceof NBTCompound, "root must be TAG_Compound");
            NBTCompound level = (NBTCompound) tag;
            assertEquals(11, level.getSize());

            NBTCompound nestedCompoundTest = Objects.requireNonNull(level.getCompound("nested compound test"));
            {
                assertEquals(2, nestedCompoundTest.getSize());
                NBTCompound egg = Objects.requireNonNull(nestedCompoundTest.getCompound("egg"));
                {
                    assertEquals(2, egg.getSize());
                    assertEquals("Eggbert", egg.getString("name"));
                    assertEquals(0.5, egg.getFloat("value"), 10e-16);
                }
                NBTCompound ham = Objects.requireNonNull(nestedCompoundTest.getCompound("ham"));
                {
                    assertEquals(2, ham.getSize());
                    assertEquals("Hampus", ham.getString("name"));
                    assertEquals(0.75, ham.getFloat("value"), 10e-16);
                }
            }

            assertEquals(2147483647, level.getInt("intTest").intValue());
            assertEquals(127, level.getByte("byteTest").byteValue());
            assertEquals(32767, level.getShort("shortTest").shortValue());
            assertEquals(0.49312871321823148, level.getDouble("doubleTest"), 10e-16);
            assertEquals(0.49823147058486938, level.getFloat("floatTest"), 10e-16);
            assertEquals(9223372036854775807L, level.getLong("longTest").longValue());

            // not actually testing the individuals non-ASCII characters, we'll let the standard library handle it
            assertTrue(level.getString("stringTest").startsWith("HELLO WORLD THIS IS A TEST STRING "));
            assertTrue(level.getString("stringTest").endsWith("!"));

            NBTList<NBTLong> listTestLong = level.getList("listTest (long)").asListOf();
            {
                assertEquals(5, listTestLong.getSize());
                assertEquals(11, listTestLong.get(0).getValue());
                assertEquals(12, listTestLong.get(1).getValue());
                assertEquals(13, listTestLong.get(2).getValue());
                assertEquals(14, listTestLong.get(3).getValue());
                assertEquals(15, listTestLong.get(4).getValue());
            }

            NBTList<NBTCompound> listTestCompound = level.getList("listTest (compound)").asListOf();
            {
                assertEquals(2, listTestCompound.getSize());
                NBTCompound compound0 = Objects.requireNonNull(listTestCompound.get(0));
                {
                    assertEquals(2, compound0.getSize());
                    assertEquals(1264099775885L, compound0.getLong("created-on").longValue());
                    assertEquals("Compound tag #0", compound0.getString("name"));
                }

                NBTCompound compound1 = Objects.requireNonNull(listTestCompound.get(1));
                {
                    assertEquals(2, compound1.getSize());
                    assertEquals(1264099775885L, compound1.getLong("created-on").longValue());
                    assertEquals("Compound tag #1", compound1.getString("name"));
                }
            }

            ImmutableByteArray bytes = Objects.requireNonNull(level.getByteArray("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))"));
            assertEquals(1000, bytes.getSize());
            for (int n = 0; n < 1000; n++) {
                assertEquals((n*n*255+n*7) % 100, bytes.get(n));
            }
        }
    }
}
