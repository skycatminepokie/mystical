package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.test.TestUtils;
import lombok.NonNull;
import net.minecraft.entity.EntityType;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AggressiveGolemsConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    public AggressiveGolemsConsequence() {
        super(AggressiveGolemsConsequence.class, null, 100d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<AggressiveGolemsConsequence> {
        protected Factory() {
            super("aggressiveGolems",
                    "Aggressive golems",
                    "Iron + Pumpkin says \"Here, have pain!\"",
                    "Golem is aggressive",
                    AggressiveGolemsConsequence.class,
                    Codec.unit(AggressiveGolemsConsequence::new));
        }

        @Override
        public @NotNull AggressiveGolemsConsequence make(@NonNull Random random, double points) {
            return new AggressiveGolemsConsequence();
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.aggressiveGolems.enabled() ? Mystical.CONFIG.aggressiveGolems.weight() : 0);
        }

        private static void setUpTest(TestContext context) {
            TestUtils.resetMystical(context);
            context.killAllEntities();
            context.spawnMob(EntityType.IRON_GOLEM, 2, 2, 2);
            context.setHealthLow(context.spawnMob(EntityType.VILLAGER, 2, 2, 2));
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, tickLimit = 150)
        public void testHaven(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);
            context.waitAndRun(125, () -> {
                context.expectEntity(EntityType.VILLAGER);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, tickLimit = 150)
        public void testHavenAndSpell(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(this);
            context.waitAndRun(125, () -> {
                context.expectEntity(EntityType.VILLAGER);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, tickLimit = 150)
        public void testSpell(TestContext context) {
            setUpTest(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(this);
            context.waitAndRun(125, () -> {
                context.dontExpectEntity(EntityType.VILLAGER);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.BORDERED_BARRIER_BOX, tickLimit = 150)
        public void testVanilla(TestContext context) {
            setUpTest(context);
            context.waitAndRun(125, () -> {
                context.expectEntity(EntityType.VILLAGER);
                context.complete();
            });
        }
    }
}
