package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MobSpawnSwapConsequence extends SpellConsequence {  // TODO: Tests
    public static final Factory FACTORY = new Factory();

    public MobSpawnSwapConsequence() {
        super(MobSpawnSwapConsequence.class, null, 75d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<MobSpawnSwapConsequence> {

        protected Factory() {
            super("mobSpawnSwap",
                    "Swap Overworld Mobs With Nether Mobs",
                    "Hot guy cold, cold guy hot.",
                    "Nether mob <-> overworld mob",
                    MobSpawnSwapConsequence.class,
                    MapCodec.unit(MobSpawnSwapConsequence::new));
        }

        @Override
        public @NotNull MobSpawnSwapConsequence make(@NotNull Random random, double points) {
            return new MobSpawnSwapConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.mobSpawnSwap.enabled() ? Mystical.CONFIG.mobSpawnSwap.weight() : 0);
        }
    }
}
