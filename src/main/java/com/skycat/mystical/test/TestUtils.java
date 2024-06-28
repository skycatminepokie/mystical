package com.skycat.mystical.test;

import com.skycat.mystical.HavenManager;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.mixin.TestContextMixin;
import com.skycat.mystical.spell.SpellHandler;
import com.skycat.mystical.util.Utils;
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
     * A 4x4x4 solid cube of barriers, with lava at 1,2,1 and 2,2,2 and one air block above the lava.
     */
    public static final String LAVA_PIT_BOX = "mystical:lava_pit_box_4x4x4";
    /**
     * A 1x2x1 with air on the top and an end portal on the bottom (0,1,0).
     */
    public static final String PORTAL = "mystical:portal_1x2x1";
    /**
     * A vertical area with a bottom bedrock block. To summon a two block high entity at the top, use pos 0,13,0.
     */
    public static final String FALL_DAMAGE = "mystical:fall_damage_1x14x1";
    /**
     * To be in this batch, a test must call {@link TestUtils#resetMystical(TestContext)} at the beginning,<br>
     * never haven anything, and never create a spell.
     */
    public static final String VANILLA_BATCH = "mystical.vanilla";
    /**
     * To be in this batch, a test must call {@link TestUtils#resetSpells(TestContext)} and {@link TestUtils#havenAll(TestContext)} at the beginning<br>and never unhaven anything.
     */
    public static final String HAVEN_ONLY_BATCH = "mystical.haven";

    /**
     * Haven all chunks inside the bounding box of {@code context}.
     *
     * @param context The context of this test.
     */
    public static void havenAll(TestContext context) {
        // Find chunks corresponding to opposite corners
        Box box = ((TestContextMixin) context).mystical_getTestBox();
        ChunkPos min = new ChunkPos(new BlockPos((int) box.minX, 0, (int) box.minZ));
        ChunkPos max = new ChunkPos(new BlockPos((int) box.maxX, 0, (int) box.maxZ));
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

    public static void resetHavens(TestContext context) {
        HavenManager havenManager = Mystical.getHavenManager();
        havenManager.resetHavens();
    }

    public static void resetPower(TestContext context) {
        HavenManager havenManager = Mystical.getHavenManager();
        havenManager.resetPower();
    }

    public static void resetSpells(TestContext context) {
        SpellHandler spellHandler = Mystical.getSpellHandler();
        spellHandler.removeAllSpells();
    }
}
