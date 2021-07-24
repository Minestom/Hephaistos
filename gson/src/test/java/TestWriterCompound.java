import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jglrxavpok.hephaistos.json.NBTGsonWriter;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestWriterCompound {

    private NBTGsonWriter writer;

    @BeforeEach
    public void init() {
        writer = NBTGsonWriter.writer(new Gson());
    }

    @Test
    public void innerCompound() {
        NBTCompound nbt = NBT.Compound(root -> {
            root.put("timestamp", NBT.Long(1564864468L));
            root.put("458", NBT.Byte(0x0F));

            root.put("inside", NBT.Compound(inside -> {
                inside.put("doubledouble", NBT.Double(0.5));
            }));

            root.put("aaa", NBT.ByteArray(1, 2));
        });

        assertEquals(new Gson().fromJson("{\"timestamp\":1564864468,\"458\":15,\"inside\":{\"doubledouble\":0.5},\"aaa\":[1,2]}", JsonObject.class), writer.write(nbt));
    }

    @AfterEach
    public void clean() {
        writer = null;
    }

}
