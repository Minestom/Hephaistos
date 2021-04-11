import org.junit.Test

import org.jglrxavpok.hephaistos.nbt.*

class DSLTests {

    @Test
    fun test() {
        compound {
            "MySuperKey1" to 42
            "MySuperKey2" to 48
            "MySuperKey3" to "My Value"
            "MySuperKey4" to "My other value"

            this["key"] = 10
            this["list"] = list<NBTInt> {
                !42
                !50
            }

            "second list" to list<NBTByte> {
                !(0xFF.toByte())
            }
        }

        list<NBTString> {
            !"My first value"
            !"Second value"
        }
    }
}