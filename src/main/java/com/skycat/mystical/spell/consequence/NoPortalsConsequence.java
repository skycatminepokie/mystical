package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.test.TestUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NoPortalsConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    public NoPortalsConsequence() {
        super(NoPortalsConsequence.class, null, 100d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<NoPortalsConsequence> {

        protected Factory() {
            super("noPortals",
                    "Disable Portals",
                    "Shiny things are just walls",
                    "No, you may not use that portal",
                    NoPortalsConsequence.class,
                    MapCodec.unit(new NoPortalsConsequence()));
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.noPortals.enabled() ? Mystical.CONFIG.noPortals.weight() : 0);
        }

        @Override
        public @NotNull NoPortalsConsequence make(@NotNull Random random, double points) {
            return new NoPortalsConsequence();
        }

        private void setUpTest(TestContext context) {
            TestUtils.resetMystical(context);
            context.killAllEntities();
            context.spawnEntity(EntityType.CREEPER, 0, 1, 0);
        }

        @GameTest(templateName = TestUtils.PORTAL, batchId = TestUtils.VANILLA_BATCH)
        public void testVanilla(TestContext context) {
            setUpTest(context);
            context.waitAndRun(2, () -> context.dontExpectEntity(EntityType.CREEPER));
            context.complete();
        }

        @GameTest(templateName = TestUtils.PORTAL, batchId = TestUtils.HAVEN_ONLY_BATCH)
        public void testHaven(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);
            context.waitAndRun(2, () -> context.dontExpectEntity(EntityType.CREEPER));
            context.complete();
        }

        @GameTest(templateName = TestUtils.PORTAL)
        public void testHavenAndSpell(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(2, () -> context.dontExpectEntity(EntityType.CREEPER));
            context.complete();
        }

        @GameTest(templateName = TestUtils.PORTAL)
        public void testSpell(TestContext context) {
            setUpTest(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(context.getWorld().getServer(), this);
            context.waitAndRun(2, () -> context.expectEntity(EntityType.CREEPER));
            context.complete();
        }
    }
}
