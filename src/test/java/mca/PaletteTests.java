package mca;

import org.jglrxavpok.hephaistos.mca.BlockState;
import org.jglrxavpok.hephaistos.mca.LongCompactorKt;
import org.jglrxavpok.hephaistos.mca.Palette;
import org.jglrxavpok.hephaistos.mca.SupportedVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PaletteTests {

    private Palette palette;
    private static final BlockState STONE = new BlockState("minecraft:stone", new HashMap<>());

    @Before
    public void init() {
        palette = new Palette();
    }

    @Test
    public void increaseReferenceOfNonExistentBlock() {
        assertEquals(0, palette.getBlocks().size());
        palette.increaseReference(BlockState.Air);
        assertEquals(1, palette.getBlocks().size());
        palette.increaseReference(BlockState.Air);
        assertEquals(1, palette.getBlocks().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void decreaseReferenceOfNonExistentBlock() {
        palette.decreaseReference(BlockState.Air);
    }

    @Test
    public void decreaseReferenceOfExistentBlock() {
        assertEquals(0, palette.getBlocks().size());
        palette.increaseReference(BlockState.Air);
        palette.increaseReference(STONE);
        assertEquals(2, palette.getBlocks().size());
        palette.decreaseReference(BlockState.Air);
        assertEquals(1, palette.getBlocks().size());
        assertEquals(STONE, palette.getBlocks().get(0));
        palette.decreaseReference(STONE);
        assertEquals(0, palette.getBlocks().size());
    }

    @Test
    public void testSimpleCompression1_15() {
        palette.increaseReference(BlockState.Air);
        palette.increaseReference(STONE);
        BlockState[] states = new BlockState[16];
        for (int i = 0; i < states.length; i++) {
            if(i % 3 == 0) {
                states[i] = BlockState.Air;
            } else {
                states[i] = STONE;
            }
        }
        int[] ids = new int[states.length];
        for (int i = 0; i < states.length; i++) {
            // the id assigned to a state is dependent to the order in which the states have been added to the palette
            ids[i] = states[i] == BlockState.Air ? 0 : 1;
        }

        // 64 bits / 16 entries = 4 bits required per entry.
        // But we only have 2 blocks, so a single bit is enough.
        // Finally, a long has 64 bits, which is enough to store 16 entries at 1 bit/entry
        long[] expected = LongCompactorKt.compress(ids, 1);
        assertArrayEquals(expected, palette.compactIDs(states, SupportedVersion.MC_1_15));
    }

    @Test
    public void testSimpleCompression1_16() {
        palette.increaseReference(BlockState.Air);
        palette.increaseReference(STONE);
        BlockState[] states = new BlockState[16];
        for (int i = 0; i < states.length; i++) {
            if(i % 3 == 0) {
                states[i] = BlockState.Air;
            } else {
                states[i] = STONE;
            }
        }
        int[] ids = new int[states.length];
        for (int i = 0; i < states.length; i++) {
            // the id assigned to a state is dependent to the order in which the states have been added to the palette
            ids[i] = states[i] == BlockState.Air ? 0 : 1;
        }

        // 64 bits / 16 entries = 4 bits required per entry.
        // But we only have 2 blocks, so a single bit is enough.
        // Finally, a long has 64 bits, which is enough to store 16 entries at 1 bit/entry
        long[] expected = LongCompactorKt.pack(ids, 1);
        assertArrayEquals(expected, palette.compactIDs(states, SupportedVersion.MC_1_16));
    }

    @After
    public void clean() {
        palette = null;
    }
}
