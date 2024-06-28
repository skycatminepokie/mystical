package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.test.TestUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ExtraFallDamageConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public ExtraFallDamageConsequence() {
        super(ExtraFallDamageConsequence.class, null, Mystical.CONFIG.extraFallDamage.multiplier() * 25);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<ExtraFallDamageConsequence> {

        protected Factory() {
            super("extraFallDamage",
                    "Extra fall damage",
                    "splat -> SPLAT",
                    "Splatting bigger",
                    ExtraFallDamageConsequence.class,
                    MapCodec.unit(ExtraFallDamageConsequence::new));
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.extraFallDamage.enabled() ? Mystical.CONFIG.extraFallDamage.weight() : 0;
        }

        @Override
        public @NotNull ExtraFallDamageConsequence make(@NotNull Random random, double points) {
            return new ExtraFallDamageConsequence();
        }

        private void setUpTest(TestContext context) {
            context.killAllEntities();
            context.spawnEntity(EntityType.ZOMBIFIED_PIGLIN, 0, 13, 0);
        }

        @GameTest(templateName = TestUtils.FALL_DAMAGE, batchId = TestUtils.HAVEN_ONLY_BATCH)
        public void testHaven(TestContext context) {
            setUpTest(context);
            TestUtils.resetSpells(context);
            TestUtils.havenAll(context);
            context.waitAndRun(20, () -> context.expectEntity(EntityType.ZOMBIFIED_PIGLIN, 0, 2, 0, 1));
            context.complete();
        }

        @GameTest(templateName = TestUtils.FALL_DAMAGE, batchId = TestUtils.VANILLA_BATCH)
        public void testVanilla(TestContext context) {
            setUpTest(context);
            TestUtils.resetMystical(context);
            context.waitAndRun(20, () -> context.expectEntity(EntityType.ZOMBIFIED_PIGLIN, 0, 2, 0, 1));
            context.complete();
        }

        @GameTest(templateName = TestUtils.FALL_DAMAGE)
        public void testSpell(TestContext context) {
            setUpTest(context);
            TestUtils.resetHavens(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(20, () -> context.dontExpectEntityAt(EntityType.ZOMBIFIED_PIGLIN, 0, 2, 0));
            context.complete();
        }

        @GameTest(templateName = TestUtils.FALL_DAMAGE)
        public void testHavenAndSpell(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(20, () -> context.expectEntity(EntityType.ZOMBIFIED_PIGLIN, 0, 2, 0, 1));
            context.complete();
        }

    }
}
