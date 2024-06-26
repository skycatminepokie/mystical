package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomCreeperEffectCloudsConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    protected RandomCreeperEffectCloudsConsequence() {
        super(RandomCreeperEffectCloudsConsequence.class, null, 25);
    }
    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<RandomCreeperEffectCloudsConsequence> {
        protected Factory() {
            super("randomCreeperEffectClouds",
                    "Creepers leave random effect clouds",
                    "Hissss... Bubbles?",
                    "Random effect cloud made",
                    RandomCreeperEffectCloudsConsequence.class,
                    MapCodec.unit(RandomCreeperEffectCloudsConsequence::new));
        }

        @Override
        public @NotNull RandomCreeperEffectCloudsConsequence make(@NotNull Random random, double points) {
            return new RandomCreeperEffectCloudsConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.randomCreeperEffectClouds.enabled() ? Mystical.CONFIG.randomCreeperEffectClouds.weight() : 0);
        }
    }
}
