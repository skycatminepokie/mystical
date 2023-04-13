package com.skycat.mystical.common.spell.consequence;

import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BigCreeperExplosionConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    private BigCreeperExplosionConsequence() {
        super(BigCreeperExplosionConsequence.class, null);
    }

    @Override
    public @NotNull ConsequenceFactory<BigCreeperExplosionConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<BigCreeperExplosionConsequence> {
        public Factory() {
            super("bigCreeperExplosion", "Bigger Creeper Explosions", "Creepers go boom. But more.", "Made new creeper with multiplied explosion power.", BigCreeperExplosionConsequence.class);
        }

        @NotNull
        @Override
        public BigCreeperExplosionConsequence make(@NonNull Random random, double points) {
            return new BigCreeperExplosionConsequence();
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.bigCreeperExplosion.enabled() ? Mystical.CONFIG.bigCreeperExplosion.weight() : 0);
        }
    }
}
