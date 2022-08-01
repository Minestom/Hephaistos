package regression;

import org.jglrxavpok.hephaistos.nbt.*;
import org.jglrxavpok.hephaistos.parser.SNBTParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue29 {
    @Test
    public void bigFloat() throws NBTException {
        NBTFloat myFloat = new NBTFloat(10000000F);

        String floatSNBT = myFloat.toSNBT();

        NBT result = new SNBTParser(new StringReader(floatSNBT)).parse();
        Assertions.assertEquals(myFloat, result);

        result = new SNBTParser(new StringReader("1e7F")).parse();
        Assertions.assertEquals(myFloat, result);
    }

    @Test
    public void bigDouble() throws NBTException {
        NBTDouble myDouble = new NBTDouble(10000000.0);

        String doubleSNBT = myDouble.toSNBT();

        NBT result = new SNBTParser(new StringReader(doubleSNBT)).parse();
        Assertions.assertEquals(myDouble, result);

        result = new SNBTParser(new StringReader("1e7D")).parse();
        Assertions.assertEquals(myDouble, result);
    }
}
