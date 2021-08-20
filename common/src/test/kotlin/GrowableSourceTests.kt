
import org.jglrxavpok.hephaistos.data.GrowableSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GrowableSourceTests {

    @Test
    fun updateSize() {
        val source = GrowableSource()
        source.setLength(1000)
        assertEquals(1000, source.length())
    }

    @Test
    fun updateSizeAfterWrite() {
        val source = GrowableSource()
        source.writeByte(42)
        assertEquals(1, source.length())
    }

    @Test
    fun updateSizeAfterWrite2() {
        val source = GrowableSource()
        source.writeInt(-999)
        assertEquals(4, source.length())
    }
} 