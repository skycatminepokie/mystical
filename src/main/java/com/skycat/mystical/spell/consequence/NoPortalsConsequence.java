package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NoPortalsConsequence extends SpellConsequence { // TODO: Icon, tests
    public static final Factory FACTORY = new Factory();

    public NoPortalsConsequence() {
        super(NoPortalsConsequence.class, null, 100d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<NoPortalsConsequence> {

        protected Factory() {
            super("noPortals",
                    "Disable Portals",
                    "Shiny things are just walls",
                    "No, you may not use that portal",
                    NoPortalsConsequence.class,
                    MapCodec.unit(new NoPortalsConsequence()));
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.noPortals.enabled() ? Mystical.CONFIG.noPortals.weight() : 0);
        }

        @Override
        public @NotNull NoPortalsConsequence make(@NotNull Random random, double points) {
            return new NoPortalsConsequence();
        }
    }
}
