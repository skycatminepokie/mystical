package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MysteryEggsConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public MysteryEggsConsequence() {
        super(MysteryEggsConsequence.class, null, 1d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<MysteryEggsConsequence> {

        protected Factory() {
            super("mysteryEggs",
                    "Mystery Eggs",
                    "Which came first, the fox or the egg?",
                    "Egg spawned a random mob.",
                    MysteryEggsConsequence.class,
                    MapCodec.unit(MysteryEggsConsequence::new));
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.mysteryEggs.enabled() ? Mystical.CONFIG.mysteryEggs.weight() : 0);
        }

        @Override
        public @NotNull MysteryEggsConsequence make(@NotNull Random random, double points) {
            return new MysteryEggsConsequence();
        }
    }
}
