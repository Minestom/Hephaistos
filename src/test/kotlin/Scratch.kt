import org.jglrxavpok.hephaistos.mca.RegionFile
import java.io.File
import java.io.RandomAccessFile

// Temporary Tests

fun main() {
    var target = File("src/test/resources/1.17_support/testregion.mca")
    val file = RandomAccessFile(target, "rw")
    val region = RegionFile(file, 0, -1)

    val nbt = region.getChunkData(0,-16)
    println(nbt)
    println(region.getChunk(0, -16)!!.toNBT())
}
