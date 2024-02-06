package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MobSpawnSwapConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    public MobSpawnSwapConsequence() {
        super(MobSpawnSwapConsequence.class, null, 75d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<MobSpawnSwapConsequence> {

        protected Factory() { // TODO: Localize Better firedMessage
            super("mobSpawnSwap",
                    "Swap Overworld Mobs With Nether Mobs",
                    "Hot guy cold, cold guy hot.",
                    "Nether mob <-> overworld mob",
                    MobSpawnSwapConsequence.class,
                    Codec.unit(MobSpawnSwapConsequence::new));
        }

        @Override
        public @NotNull MobSpawnSwapConsequence make(@NonNull Random random, double points) {
            return new MobSpawnSwapConsequence();
        }

        @Override
        public void test(TestContext context) { // TODO
            com.skycat.mystical.common.util.Utils.log("Test not implemented for " + shortName, com.skycat.mystical.common.LogLevel.WARN);
            context.complete();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.mobSpawnSwap.enabled() ? Mystical.CONFIG.mobSpawnSwap.weight() : 0);
        }
    }
}
