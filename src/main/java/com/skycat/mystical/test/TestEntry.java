package com.skycat.mystical.test;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.LogLevel;
import com.skycat.mystical.common.util.Utils;
import com.skycat.mystical.server.HavenManager;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

public class TestEntry implements FabricGameTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void checkHavenWorks(TestContext context) {
        setupPreconditions(context);
        HavenManager havenManager = Mystical.getHavenManager();
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        havenManager.setPower(player, Integer.MAX_VALUE);
        context.assertTrue(havenManager.hasPower(player, havenManager.getHavenCost(player.getChunkPos())), "We couldn't haven with Integer.MAX_VALUE power.");
        context.assertTrue(havenManager.tryHaven(player.getChunkPos(), player), "Player couldn't haven, despite having enough power.");
        context.assertTrue(havenManager.isInHaven(player.getChunkPos()), "Havening worked, but we're not in a haven.");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void checkHavenFails(TestContext context) {
        setupPreconditions(context);
        HavenManager havenManager = Mystical.getHavenManager();
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();

        havenManager.setPower(player, -1);
        context.assertFalse(havenManager.tryHaven(player.getChunkPos(), player), "We were able to haven with negative power???");

        havenManager.resetHavens();
        havenManager.setPower(player, 0);
        if (havenManager.tryHaven(player.getChunkPos(), player)) {
            Utils.log("We were able to haven with 0 power.", LogLevel.WARN);
        }
        context.complete();
    }

    public void setupPreconditions(TestContext context) {
        HavenManager havenManager = Mystical.getHavenManager();
        havenManager.resetHavens();
        havenManager.resetPower();
        Mystical.getSpellHandler().removeAllSpells();
    }
}
