import org.jglrxavpok.hephaistos.GrowableSource
import org.junit.Test

import org.jglrxavpok.hephaistos.nbt.*
import org.junit.Assert.assertEquals

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