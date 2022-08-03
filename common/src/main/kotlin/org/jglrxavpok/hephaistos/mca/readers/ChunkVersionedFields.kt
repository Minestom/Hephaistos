package org.jglrxavpok.hephaistos.mca.readers

import org.jglrxavpok.hephaistos.mca.SupportedVersion

fun SectionName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "Sections" else "sections"
fun EntitiesName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "Entities" else "entities"
fun BlockEntitiesName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "TileEntities" else "block_entities"
fun StructuresName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "Structures" else "structures"
fun BlockTicksName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "TileTicks" else "block_ticks"
fun FluidTicksName(version: SupportedVersion) = if(version < SupportedVersion.MC_1_18_PRE_4) "LiquidTicks" else "fluid_ticks"