package org.jglrxavpok.hephaistos.mca

import kotlin.math.floor

fun Int.chunkToRegion() = floor(this / 32.0).toInt()
fun Int.regionToChunk() = this * 32
fun Int.blockToChunk() = floor(this / 16.0).toInt()
fun Int.chunkToBlock() = this shl 4
fun Int.chunkInsideRegion() = this and 31
fun Int.blockInsideChunk() = this and 15
fun Int.blockInsideSection() = this and 15

fun Int.blockToSection() = floor(this / 16.0).toInt().toByte()
fun Int.sectionToBlock() = this * 16