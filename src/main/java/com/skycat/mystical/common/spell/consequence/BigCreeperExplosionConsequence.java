package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.test.TestFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Function;

public class BigCreeperExplosionConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    private static final Function<Double, Double> DIFFICULTY_FUNCTION = (multiplier) -> ((multiplier - 1) * 30); // Double size = 30 difficulty

    @Deprecated
    private BigCreeperExplosionConsequence() {
        super(BigCreeperExplosionConsequence.class, null, 1.0);
    }

    private BigCreeperExplosionConsequence(double difficulty) {
        super(BigCreeperExplosionConsequence.class, null, difficulty);  // TODO: Scaling
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
            return new BigCreeperExplosionConsequence(DIFFICULTY_FUNCTION.apply(Mystical.CONFIG.bigCreeperExplosion.multiplier())); // TODO: Scaling, randomization
        }

        @Override
        public @Nullable TestFunction getTestFunction() {
            return null; // TODO
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.bigCreeperExplosion.enabled() ? Mystical.CONFIG.bigCreeperExplosion.weight() : 0);
        }
    }
}
