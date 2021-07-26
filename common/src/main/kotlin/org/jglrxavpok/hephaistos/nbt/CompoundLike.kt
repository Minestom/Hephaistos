package org.jglrxavpok.hephaistos.nbt

import org.jetbrains.annotations.Contract

interface CompoundLike {

    /**
     * Creates a NBTCompound. This will be immutable and copied,
     * regardless if the original object is immutable or mutable.
     */
    @Contract(pure = true)
    fun toCompound(): NBTCompound

}