package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class EvilBeesConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public EvilBeesConsequence() {
        super(EvilBeesConsequence.class, null, 10f); // TODO Difficulty
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<EvilBeesConsequence> {
        protected Factory() {
            super("evilBees",
                    "Evil Bees",
                    "Beevil.",
                    "Made bee beevil.",
                    EvilBeesConsequence.class,
                    Codec.unit(EvilBeesConsequence::new));
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.evilBees.enabled() ? Mystical.CONFIG.evilBees.weight() : 0;
        }

        @Override
        public @NotNull EvilBeesConsequence make(@NonNull Random random, double points) {
            return new EvilBeesConsequence();
        }
    }
}
