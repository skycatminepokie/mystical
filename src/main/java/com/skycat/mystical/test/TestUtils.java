package com.skycat.mystical.test;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.Utils;
import com.skycat.mystical.mixin.TestContextMixin;
import com.skycat.mystical.HavenManager;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;

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
     * A 13x17x13 box of barriers, outlined with tinted glass. Every fourth y-level starting with the bottom layer is sculk.<br>
     * In the center, just below the 3rd layer is a sculk shrieker (6, 6, 6). The one sculk above it is removed.
     */
    public static final String WARDEN_SUMMON_BOX = "mystical:warden_summon_box_13x17x13";
    /**
     * A 3x6x8 box of barriers, outlined with tinted glass. The bottom layer of air is replaced with lava, and the two ends of that replaced with bottom smooth stone slabs.
     */
    public static final String FISHING_ROD_DEATH_BOX = "mystical:fishing_rod_death_box_3x6x8";

    /**
     * Haven all chunks inside the bounding box of {@code context}.
     *
     * @param context The context of this test.
     */
    public static void havenAll(TestContext context) {
        // Find chunks corresponding to opposite corners
        Box box = ((TestContextMixin) context).mystical_getTestBox();
        Utils.log("Box: " + box);
        ChunkPos min = new ChunkPos(new BlockPos((int) box.minX, 0, (int) box.minZ));
        ChunkPos max = new ChunkPos(new BlockPos((int) box.maxX, 0, (int) box.maxZ));
        Utils.log("Chunk min: " + min);
        Utils.log("Chunk max: " + max);
        int x = min.x; // Starting at the min x
        int z = min.z; // Starting at the min z
        // Haven all chunks in that square
        while (x <= max.x) {
            while (z <= max.z) {
                Mystical.getHavenManager().addHaven(new ChunkPos(x, z));
                Utils.log("Havening " + x + ", " + z);
                z++;
            }
            x++;
            z = min.z;
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
