package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.test.TestUtils;
import lombok.NonNull;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AggressiveGolemsConsequence extends SpellConsequence { // TODO: Make work for snow golems
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
        public String getStructure() {
            return TestUtils.BORDERED_BARRIER_BOX;
        }

        @Override
        public int getTickLimit() {
            return 500;
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.aggressiveGolems.enabled() ? Mystical.CONFIG.aggressiveGolems.weight() : 0);
        }

        @Override
        public void test(TestContext context) { // TODO: Test the test
            TestUtils.resetMystical(context);
            IronGolemEntity golem = context.spawnMob(EntityType.IRON_GOLEM, 2, 3, 2);
            VillagerEntity villager = context.spawnMob(EntityType.VILLAGER, 2, 3, 2);
            context.setHealthLow(villager);
            context.waitAndRun(50, () -> {
                context.expectEntity(EntityType.VILLAGER);
                Mystical.getSpellHandler().activateNewSpellWithConsequence(AggressiveGolemsConsequence.FACTORY);
                context.waitAndRun(50, () -> {

                });
            });


            context.dontExpectEntity(EntityType.IRON_GOLEM);
            context.complete();
        }
    }
}
