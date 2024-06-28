package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.test.TestUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FireImmunitySwapConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    public FireImmunitySwapConsequence() {
        super(FireImmunitySwapConsequence.class, null, -15d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<FireImmunitySwapConsequence> {

        protected Factory() {
            super("fireImmunitySwap",
                    "Swap fire immunity",
                    "!fire",
                    "Fire immunity swapped",
                    FireImmunitySwapConsequence.class,
                    MapCodec.unit(FireImmunitySwapConsequence::new));
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.fireImmunitySwap.enabled() ? Mystical.CONFIG.fireImmunitySwap.weight() : 0;
        }

        @Override
        public @NotNull FireImmunitySwapConsequence make(@NotNull Random random, double points) {
            return new FireImmunitySwapConsequence();
        }

        private void setUpTest(TestContext context) {
            context.killAllEntities();
            context.setHealthLow(context.spawnEntity(EntityType.BLAZE, 1, 2, 1));
            context.setHealthLow(context.spawnEntity(EntityType.CREEPER, 2, 2, 2));
        }

        @GameTest(templateName = TestUtils.LAVA_PIT_BOX, batchId = TestUtils.HAVEN_ONLY_BATCH)
        public void testHaven(TestContext context) {
            setUpTest(context);
            TestUtils.resetSpells(context);
            TestUtils.havenAll(context);
            context.waitAndRun(10, () -> {
                context.expectEntity(EntityType.BLAZE);
                context.dontExpectEntity(EntityType.CREEPER);
            });
            context.complete();
        }

        @GameTest(templateName = TestUtils.LAVA_PIT_BOX)
        public void testHavenAndSpell(TestContext context) {
            setUpTest(context);
            TestUtils.resetSpells(context);
            TestUtils.havenAll(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(10, () -> {
                context.expectEntity(EntityType.BLAZE);
                context.dontExpectEntity(EntityType.CREEPER);
            });
            context.complete();
        }

        @GameTest(templateName = TestUtils.LAVA_PIT_BOX)
        public void testSpell(TestContext context) {
            setUpTest(context);
            TestUtils.resetMystical(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(10, () -> {
                context.dontExpectEntity(EntityType.BLAZE);
                context.expectEntity(EntityType.CREEPER);
            });
            context.complete();
        }

        @GameTest(templateName = TestUtils.LAVA_PIT_BOX, batchId = TestUtils.VANILLA_BATCH)
        public void testVanilla(TestContext context) {
            setUpTest(context);
            TestUtils.resetMystical(context);
            context.waitAndRun(10, () -> {
                context.expectEntity(EntityType.BLAZE);
                context.dontExpectEntity(EntityType.CREEPER);
            });
            context.complete();
        }
    }
}
