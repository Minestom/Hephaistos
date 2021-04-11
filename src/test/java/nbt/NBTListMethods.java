package nbt;

import org.jglrxavpok.hephaistos.nbt.NBTList;
import org.jglrxavpok.hephaistos.nbt.NBTString;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NBTListMethods {

    private NBTList<NBTString> list;

    @Before
    public void init() {
        list = new NBTList<>(NBTType.TAG_String);
    }

    @Test
    public void indexOf() {
        list.add(new NBTString("Some value0"));
        list.add(new NBTString("Some value1"));
        list.add(new NBTString("Some value2"));

        for (int i = 0; i < list.getLength(); i++) {
            NBTString valueAtI = list.get(i);
            assertEquals(i, list.indexOf(valueAtI));
        }

        // indexOf uses equals
        assertEquals(0, list.indexOf(new NBTString("Some value0")));
        assertEquals(1, list.indexOf(new NBTString("Some value1")));
        assertEquals(2, list.indexOf(new NBTString("Some value2")));

        list.removeAt(0);
        assertEquals(1, list.indexOf(new NBTString("Some value2")));
    }

    @Test
    public void removeAt() {
        list.add(new NBTString("Some value0"));
        list.add(new NBTString("Some value1"));
        list.add(new NBTString("Some value2"));

        list.removeAt(0);
        assertEquals(2, list.getLength());
        assertEquals("Some value2", list.get(1).getValue());
    }

    @Test
    public void remove() {
        list.add(new NBTString("Some value0"));
        list.add(new NBTString("Some value1"));
        list.add(new NBTString("Some value2"));

        list.remove(new NBTString("Some value0"));
        assertEquals(2, list.getLength());
        assertEquals("Some value2", list.get(1).getValue());
    }

    @After
    public void clean() {
        list.clear();
        list = null;
    }

}
