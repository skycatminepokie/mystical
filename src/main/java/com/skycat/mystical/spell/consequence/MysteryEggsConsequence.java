package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MysteryEggsConsequence extends SpellConsequence { // TODO: Icon
    public static final Factory FACTORY = new Factory();
    public MysteryEggsConsequence() {
        super(MysteryEggsConsequence.class, null, 1d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<MysteryEggsConsequence> {

        protected Factory() {
            super("mysteryEggs",
                    "Mystery Eggs",
                    "Which came first, the fox or the egg?",
                    "Egg spawned a random mob.",
                    MysteryEggsConsequence.class,
                    Codec.unit(MysteryEggsConsequence::new));
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.mysteryEggs.enabled() ? Mystical.CONFIG.mysteryEggs.weight() : 0);
        }

        @Override
        public @NotNull MysteryEggsConsequence make(@NonNull Random random, double points) {
            return new MysteryEggsConsequence();
        }
    }
}
