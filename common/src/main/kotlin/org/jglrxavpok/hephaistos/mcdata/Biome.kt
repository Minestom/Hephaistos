package org.jglrxavpok.hephaistos.mcdata

/**
 * Allows to convert pre 1.18 biomes to 1.18+ ids
 */
enum class Biome(val namespaceID: String, val numericalID: Int) {

    Ocean("minecraft:ocean", 0),
    Plains("minecraft:plains", 1),
    Desert("minecraft:desert", 2),
    Mountains("minecraft:mountains", 3),
    Forest("minecraft:forest", 4),
    Taiga("minecraft:taiga", 5),
    Swamp("minecraft:swamp", 6),
    River("minecraft:river", 7),
    NetherWastes("minecraft:nether_wastes", 8),
    TheEnd("minecraft:the_end", 9),
    FrozenOcean("minecraft:frozen_ocean", 10),
    FrozenRiver("minecraft:frozen_river", 11),
    SnowyTundra("minecraft:snowy_tundra", 12),
    SnowyMountains("minecraft:snowy_mountains", 13),
    MushroomFields("minecraft:mushroom_fields", 14),
    MushroomFieldShore("minecraft:mushroom_field_shore", 15),
    Beach("minecraft:beach", 16),
    DesertHills("minecraft:desert_hills", 17),
    WoodedHills("minecraft:wooded_hills", 18),
    TaigaHills("minecraft:taiga_hills", 19),
    MountainEdge("minecraft:mountain_edge", 20),
    Jungle("minecraft:jungle", 21),
    JungleHills("minecraft:jungle_hills", 22),
    JungleEdge("minecraft:jungle_edge", 23),
    DeepOcean("minecraft:deep_ocean", 24),
    StoneShore("minecraft:stone_shore", 25),
    SnowyBeach("minecraft:snowy_beach", 26),
    BirchForest("minecraft:birch_forest", 27),
    BirchForestHills("minecraft:birch_forest_hills", 28),
    DarkForest("minecraft:dark_forest", 29),
    SnowyTaiga("minecraft:snowy_taiga", 30),
    SnowyTaigaHills("minecraft:snowy_taiga_hills", 31),
    GiantTreeTaiga("minecraft:giant_tree_taiga", 32),
    GiantTreeTaigaHills("minecraft:giant_tree_taiga_hills", 33),
    WoodedMountains("minecraft:wooded_mountains", 34),
    Savanna("minecraft:savanna", 35),
    SavannaPlateau("minecraft:savanna_plateau", 36),
    Badlands("minecraft:badlands", 37),
    WoodedBadlandsPlateau("minecraft:wooded_badlands_plateau", 38),
    BadlandsPlateau("minecraft:badlands_plateau", 39),
    SmallEndIslands("minecraft:small_end_islands", 40),
    EndMidlands("minecraft:end_midlands", 41),
    EndHighlands("minecraft:end_highlands", 42),
    EndBarrens("minecraft:end_barrens", 43),
    WarmOcean("minecraft:warm_ocean", 44),
    LukewarmOcean("minecraft:lukewarm_ocean", 45),
    ColdOcean("minecraft:cold_ocean", 46),
    DeepWarmOcean("minecraft:deep_warm_ocean", 47),
    DeepLukewarmOcean("minecraft:deep_lukewarm_ocean", 48),
    DeepColdOcean("minecraft:deep_cold_ocean", 49),
    DeepFrozenOcean("minecraft:deep_frozen_ocean", 50),
    TheVoid("minecraft:the_void", 127),
    SunflowerPlains("minecraft:sunflower_plains", 129),
    DesertLakes("minecraft:desert_lakes", 130),
    GravellyMountains("minecraft:gravelly_mountains", 131),
    FlowerForest("minecraft:flower_forest", 132),
    TaigaMountains("minecraft:taiga_mountains", 133),
    SwampHills("minecraft:swamp_hills", 134),
    IceSpikes("minecraft:ice_spikes", 140),
    ModifiedJungle("minecraft:modified_jungle", 149),
    ModifiedJungleEdge("minecraft:modified_jungle_edge", 151),
    TallBirchForest("minecraft:tall_birch_forest", 155),
    TallBirchHills("minecraft:tall_birch_hills", 156),
    DarkForestHills("minecraft:dark_forest_hills", 157),
    SnowyTaigaMountains("minecraft:snowy_taiga_mountains", 158),
    GiantSpruceTaiga("minecraft:giant_spruce_taiga", 160),
    GiantSpruceTaigaHills("minecraft:giant_spruce_taiga_hills", 161),
    ModifiedGravellyMountains("minecraft:modified_gravelly_mountains", 162),
    ShatteredSavanna("minecraft:shattered_savanna", 163),
    ShatteredSavannaPlateau("minecraft:shattered_savanna_plateau", 164),
    ErodedBadlands("minecraft:eroded_badlands", 165),
    ModifiedWoodedBadlandsPlateau("minecraft:modified_wooded_badlands_plateau", 166),
    ModifiedBadlandsPlateau("minecraft:modified_badlands_plateau", 167),
    BambooJungle("minecraft:bamboo_jungle", 168),
    BambooJungleHills("minecraft:bamboo_jungle_hills", 169),
    SoulSandValley("minecraft:soul_sand_valley", 170),
    CrimsonForest("minecraft:crimson_forest", 171),
    WarpedForest("minecraft:warped_forest", 172),
    BasaltDeltas("minecraft:basalt_deltas", 173),
    DripstoneCaves("minecraft:dripstone_caves", 174),
    LushCaves("minecraft:lush_caves", 175);

    companion object {
        val UnknownBiome = TheVoid.namespaceID

        /**
         * Converts a pre 1.18 ID to its namespace ID. If the index is not recognized, "hephaistos:unknown_{index}" will be returned
         */
        fun numericalIDToNamespaceID(index: Int): String {
            return values().find { it.numericalID == index }?.namespaceID ?: "hephaistos:unknown_$index"
        }

        /**
         * Tries to get the biome corresponding to the given id
         * If none match, returns 'Void'
         */
        fun fromNamespaceID(id: String): Biome {
            return values().find { it.namespaceID == id } ?: TheVoid
        }
    }

}