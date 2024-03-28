package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Function;

public class BigCreeperExplosionConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    private static final Function<Double, Double> DIFFICULTY_FUNCTION = (multiplier) -> ((multiplier - 1) * 30); // Double size = 30 difficulty

    private BigCreeperExplosionConsequence(double difficulty) {
        super(BigCreeperExplosionConsequence.class, null, difficulty);
    }

    @Override
    public @NotNull ConsequenceFactory<BigCreeperExplosionConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<BigCreeperExplosionConsequence> {
        public Factory() {
            super("bigCreeperExplosion",
                    "Bigger Creeper Explosions",
                    "Creepers go boom. But more.",
                    "Creeper explosion multiplied.",
                    BigCreeperExplosionConsequence.class,
                    Codec.DOUBLE.xmap(BigCreeperExplosionConsequence::new, BigCreeperExplosionConsequence::getDifficulty));
        }

        @NotNull
        @Override
        public BigCreeperExplosionConsequence make(@NonNull Random random, double points) {
            return new BigCreeperExplosionConsequence(DIFFICULTY_FUNCTION.apply(Mystical.CONFIG.bigCreeperExplosion.multiplier()));
        }



        @Override
        public double getWeight() {
            return (Mystical.CONFIG.bigCreeperExplosion.enabled() ? Mystical.CONFIG.bigCreeperExplosion.weight() : 0);
        }
    }
}
