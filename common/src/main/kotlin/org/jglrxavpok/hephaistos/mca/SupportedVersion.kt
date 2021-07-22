package org.jglrxavpok.hephaistos.mca

enum class SupportedVersion(val lowestDataVersion: Int) {

    MC_1_15(2225),
    MC_1_16(2504);

    companion object {

        val Latest: SupportedVersion = MC_1_16

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