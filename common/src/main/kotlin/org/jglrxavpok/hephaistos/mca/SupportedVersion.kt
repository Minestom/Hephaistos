package org.jglrxavpok.hephaistos.mca

enum class SupportedVersion(val lowestDataVersion: Int) {

    MC_1_15(2225),
    MC_1_16(2504),
    MC_1_17_0(2724),
    MC_1_18_PRE_4(2850),
    MC_1_20(3463),
    ;

    companion object {

        val Latest: SupportedVersion = MC_1_20

        /**
         * Returns the highest version known for the given data version
         * Because Hephaistos does not know about version below MC 1.14 (at the time of writing this comment), it will default to
         * MC_1_15 if the data version is lower than the MC_1_15 data version
         */
        fun closest(dataVersion: Int): SupportedVersion {
            var closestFound = MC_1_15 // default
            for (v in values()) {
                if(v.lowestDataVersion <= dataVersion) {
                    closestFound = v
                }
            }
            return closestFound
        }
    }
}