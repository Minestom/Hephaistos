package mca;

import org.jglrxavpok.hephaistos.collections.ImmutableLongArray;
import org.jglrxavpok.hephaistos.mca.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PaletteTests {

    private BlockPalette palette;
    private static final BlockState STONE = new BlockState("minecraft:stone", new HashMap<>());

    @BeforeEach
    public void init() {
        palette = new BlockPalette();
    }

    @Test
    public void increaseReferenceOfNonExistentBlock() {
        assertEquals(0, palette.getElements().size());
        palette.increaseReference(BlockState.AIR);
        assertEquals(1, palette.getElements().size());
        palette.increaseReference(BlockState.AIR);
        assertEquals(1, palette.getElements().size());
    }

    @Test
    public void decreaseReferenceOfNonExistentBlock() {
        assertThrows(IllegalArgumentException.class, () ->
            palette.decreaseReference(BlockState.AIR)
        );

    }

    @Test
    public void decreaseReferenceOfExistentBlock() {
        assertEquals(0, palette.getElements().size());
        palette.increaseReference(BlockState.AIR);
        palette.increaseReference(STONE);
        assertEquals(2, palette.getElements().size());
        palette.decreaseReference(BlockState.AIR);
        assertEquals(1, palette.getElements().size());
        assertEquals(STONE, palette.getElements().get(0));
        palette.decreaseReference(STONE);
        assertEquals(0, palette.getElements().size());
    }

    @Test
    public void testSimpleCompression1_15() {
        palette.increaseReference(BlockState.AIR);
        palette.increaseReference(STONE);
        BlockState[] states = new BlockState[16];
        for (int i = 0; i < states.length; i++) {
            if(i % 3 == 0) {
                states[i] = BlockState.AIR;
            } else {
                states[i] = STONE;
            }
        }
        int[] ids = new int[states.length];
        for (int i = 0; i < states.length; i++) {
            // the id assigned to a state is dependent to the order in which the states have been added to the palette
            ids[i] = states[i] == BlockState.AIR ? 0 : 1;
        }

        // 64 bits / 16 entries = 4 bits required per entry.
        // But we only have 2 blocks, so a single bit is enough.
        // Finally, a long has 64 bits, which is enough to store 16 entries at 1 bit/entry
        ImmutableLongArray expected = LongCompactorKt.compress(ids, 1);
        assertEquals(expected, palette.compactIDs(states, SupportedVersion.MC_1_15));
    }

    @Test
    public void testSimpleCompression1_16() {
        palette.increaseReference(BlockState.AIR);
        palette.increaseReference(STONE);
        BlockState[] states = new BlockState[16];
        for (int i = 0; i < states.length; i++) {
            if(i % 3 == 0) {
                states[i] = BlockState.AIR;
            } else {
                states[i] = STONE;
            }
        }
        int[] ids = new int[states.length];
        for (int i = 0; i < states.length; i++) {
            // the id assigned to a state is dependent to the order in which the states have been added to the palette
            ids[i] = states[i] == BlockState.AIR ? 0 : 1;
        }

        // 64 bits / 16 entries = 4 bits required per entry.
        // But we only have 2 blocks, so a single bit is enough.
        // Finally, a long has 64 bits, which is enough to store 16 entries at 1 bit/entry
        ImmutableLongArray expected = LongCompactorKt.pack(ids, 1);
        assertEquals(expected, palette.compactIDs(states, SupportedVersion.MC_1_16));
    }

    @AfterEach
    public void clean() {
        palette = null;
    }
}
