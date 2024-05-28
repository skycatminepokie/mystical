package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NoFuseConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    public NoFuseConsequence() {
        super(NoFuseConsequence.class, null, 100d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<NoFuseConsequence> {
        protected Factory() {
            super("noFuse",
                    "No Fuses",
                    "Creepers don't hesitate anymore.",
                    "Destroyed a fuse",
                    NoFuseConsequence.class,
                    MapCodec.unit(NoFuseConsequence::new));
        }

        @Override
        public @NotNull NoFuseConsequence make(@NotNull Random random, double points) {
            return new NoFuseConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.noFuse.enabled() ? Mystical.CONFIG.noFuse.weight() : 0);
        }
    }
}
