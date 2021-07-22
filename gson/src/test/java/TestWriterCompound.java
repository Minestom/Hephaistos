import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jglrxavpok.hephaistos.json.NBTGsonWriter;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestWriterCompound {

    private NBTGsonWriter writer;

    @BeforeEach
    public void init() {
        writer = new NBTGsonWriter(new Gson());
    }

    @Test
    public void innerCompound() {
        NBTCompound nbt = new NBTCompound();
        nbt.setLong("timestamp", 1564864468L);
        nbt.setByte("458", (byte) 0x0F);

        NBTCompound inside = new NBTCompound();
        inside.setDouble("doubledouble", 0.5);
        nbt.set("inside", inside);
        nbt.setByteArray("aaa", new byte[] { 1, 2 });

        assertEquals(new Gson().fromJson("{\"timestamp\":1564864468,\"458\":15,\"inside\":{\"doubledouble\":0.5},\"aaa\":[1,2]}", JsonObject.class), writer.write(nbt));
    }

}
