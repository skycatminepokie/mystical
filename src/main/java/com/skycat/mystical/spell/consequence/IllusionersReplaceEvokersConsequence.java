package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class IllusionersReplaceEvokersConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    public IllusionersReplaceEvokersConsequence() {
        super(IllusionersReplaceEvokersConsequence.class, null, 25d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<IllusionersReplaceEvokersConsequence> {

        protected Factory() {
            super("illusionersReplaceEvokers",
                    "Illusioners Replace Evokers",
                    "But it was all an illusion!",
                    "Replaced an evoker with an illusioner.",
                    IllusionersReplaceEvokersConsequence.class,
                    Codec.unit(new IllusionersReplaceEvokersConsequence()));
        }

        @Override
        public @NotNull IllusionersReplaceEvokersConsequence make(@NonNull Random random, double points) {
            return new IllusionersReplaceEvokersConsequence();
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.illusionersReplaceEvokers.enabled() ? Mystical.CONFIG.illusionersReplaceEvokers.weight() : 0;
        }
    }
}
