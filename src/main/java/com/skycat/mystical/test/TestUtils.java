package com.skycat.mystical.test;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.server.HavenManager;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.Box;

/**
 * Contains constants and functions for testing with Mystical.<br>
 * Dimensions are in the order x, y, z.
 */
public class TestUtils {
    /**
     * 7x5x7 box of barriers, with tinted glass replacing the edges.
     */
    public static final String BORDERED_BARRIER_BOX = "mystical:edged_barrier_box_7x5x7";
    /**
     * 32x1x32 air.
     */
    public static final String EMPTY = "mystical:empty_32x1x32";

    /**
     * Haven all chunks inside the bounding box of {@code context}.
     *
     * @param context The context of this test.
     */
    public static void havenAll(TestContext context) {
        Box box = context.getTestBox();
        double x = box.minX; // Starting at the min x
        double z = box.minZ; // Starting at the min z
        while (x <= box.maxX) { // While we haven't gone past the max x
            while (z <= box.maxZ) { // While we haven't gone past the max z
                Mystical.getHavenManager().havenChunk((int) x, (int) z);
                z += 16; // Size of the side of a chunk
            }
            z = box.minZ;
            x += 16;
        }
    }

    /**
     * Reset havens, power, and spells. Configs are not reset.
     */
    public static void resetMystical(TestContext context) {
        HavenManager havenManager = Mystical.getHavenManager();
        havenManager.resetHavens();
        havenManager.resetPower();
        Mystical.getSpellHandler().removeAllSpells();
    }
}
