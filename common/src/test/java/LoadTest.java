import org.jglrxavpok.hephaistos.mca.RegionFile;

import java.io.RandomAccessFile;

/**
 * Used when Minecraft updates or Hephaistos has errors
 * On git because it's easier for me :)
 */
public class LoadTest {

    public static void main(String[] args) {
        try {
            RegionFile regionFile = new RegionFile(new RandomAccessFile("C:\\Users\\jglrxavpok\\AppData\\Roaming\\.minecraft\\saves\\Test for Hephaistos\\region\\r.0.0.mca", "r"), 0, 0);
            regionFile.getChunk(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
