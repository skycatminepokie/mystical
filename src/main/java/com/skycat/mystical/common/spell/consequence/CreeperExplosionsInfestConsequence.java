package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CreeperExplosionsInfestConsequence extends SpellConsequence{
    public static final Factory FACTORY = new Factory();

    public CreeperExplosionsInfestConsequence() {
        super(CreeperExplosionsInfestConsequence.class, null, 30d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<CreeperExplosionsInfestConsequence> {
        protected Factory() {
            super("creeperExplosionsInfest",
                    "Creeper Explosions Infest Blocks",
                    "ssss... delayed ssss!",
                    "Infested blocks",
                    CreeperExplosionsInfestConsequence.class,
                    Codec.unit(new CreeperExplosionsInfestConsequence()));
        }

        @Override
        public @NotNull CreeperExplosionsInfestConsequence make(@NonNull Random random, double points) {
            return new CreeperExplosionsInfestConsequence();
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.creeperExplosionsInfest.enabled() ? Mystical.CONFIG.creeperExplosionsInfest.weight() : 0;
        }
    }
}
