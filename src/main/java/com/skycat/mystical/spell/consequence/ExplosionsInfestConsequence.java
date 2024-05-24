package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ExplosionsInfestConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    public ExplosionsInfestConsequence() {
        super(ExplosionsInfestConsequence.class, null, 30d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<ExplosionsInfestConsequence> {
        protected Factory() {
            super("explosionsInfest",
                    "Explosions Infest Blocks",
                    "ssss... delayed ssss!",
                    "Infested blocks",
                    ExplosionsInfestConsequence.class,
                    MapCodec.unit(new ExplosionsInfestConsequence()));
        }

        @Override
        public @NotNull ExplosionsInfestConsequence make(@NotNull Random random, double points) {
            return new ExplosionsInfestConsequence();
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.explosionsInfest.enabled() ? Mystical.CONFIG.explosionsInfest.weight() : 0;
        }
    }
}
