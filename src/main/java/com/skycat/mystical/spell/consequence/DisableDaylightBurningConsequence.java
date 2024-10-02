package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.test.TestUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DisableDaylightBurningConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    protected DisableDaylightBurningConsequence() {
        super(DisableDaylightBurningConsequence.class, null, 15d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<DisableDaylightBurningConsequence> {

        protected Factory() {
            super("disableDaylightBurning",
                    "Disable Daylight Burning",
                    "Mobs bought some sunscreen",
                    "Said no this mob doesn't burn",
                    DisableDaylightBurningConsequence.class,
                    MapCodec.unit(DisableDaylightBurningConsequence::new));
        }

        @Override
        public @NotNull DisableDaylightBurningConsequence make(@NotNull Random random, double points) {
            return new DisableDaylightBurningConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.disableDaylightBurning.enabled() ? Mystical.CONFIG.disableDaylightBurning.weight() : 0);
        }

        private static void setUpTest(TestContext context) {
            context.killAllEntities();
            context.getWorld().getServer().setDifficulty(Difficulty.EASY, true);
            context.setTime(1000); // Make it day
            context.setHealthLow(context.spawnMob(EntityType.ZOMBIE, 2, 2, 2));
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, skyAccess = true, batchId = TestUtils.HAVEN_ONLY_BATCH, required = false)
        public void testHaven(TestContext context) {
            setUpTest(context);
            TestUtils.resetSpells(context);
            TestUtils.havenAll(context);
            context.waitAndRun(40, () -> {
                context.dontExpectEntity(EntityType.ZOMBIE);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, skyAccess = true, required = false)
        public void testHavenAndSpell(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(40, () -> {
                context.dontExpectEntity(EntityType.ZOMBIE);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, skyAccess = true, required = false)
        public void testSpell(TestContext context) {
            setUpTest(context);
            TestUtils.resetHavens(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(40, () -> {
                context.expectEntity(EntityType.ZOMBIE); // It shouldn't burn
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, skyAccess = true, batchId = TestUtils.VANILLA_BATCH, required = false)
        public void testVanilla(TestContext context) {
            setUpTest(context);
            TestUtils.resetMystical(context);
            context.waitAndRun(40, () -> {
                context.dontExpectEntity(EntityType.ZOMBIE);
                context.complete();
            });
        }
    }
}
