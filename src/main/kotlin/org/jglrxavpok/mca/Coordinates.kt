package org.jglrxavpok.mca

import kotlin.math.floor

fun Int.chunkToRegion() = floor(this / 32.0).toInt()
fun Int.regionToChunk() = this * 32
fun Int.blockToChunk() = floor(this / 32.0).toInt()
fun Int.chunkToBlock() = this shl 4
fun Int.chunkInsideRegion() = this and 31