package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomEvokerSummonsConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    public RandomEvokerSummonsConsequence() {
        super(RandomEvokerSummonsConsequence.class, null, 30d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<RandomEvokerSummonsConsequence> {
        protected Factory() {
            super("randomEvokerSummons", "Evokers Summon Random Mobs", "Evokers found some new friends!", "Evoker summoned random mob", RandomEvokerSummonsConsequence.class, Codec.unit(new RandomEvokerSummonsConsequence()));
        }

        @Override
        public @NotNull RandomEvokerSummonsConsequence make(@NonNull Random random, double points) {
            return new RandomEvokerSummonsConsequence();
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.randomEvokerSummons.enabled() ? Mystical.CONFIG.randomEvokerSummons.weight() : 0;
        }
    }
}
