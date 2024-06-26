package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class EnderTypeChangeConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<EnderTypeChangeConsequence> getFactory() {
        return FACTORY;
    }

    protected EnderTypeChangeConsequence() {
        super(EnderTypeChangeConsequence.class, null, 5d);
    }

    public static class Factory extends ConsequenceFactory<EnderTypeChangeConsequence> {
        public Factory() {
            super("enderTypeChange",
                    "Ender Type Change",
                    "Of mites and men",
                    "Swapped ender entity type",
                    EnderTypeChangeConsequence.class,
                    MapCodec.unit(EnderTypeChangeConsequence::new));
        }

        @Override
        public @NotNull EnderTypeChangeConsequence make(@NotNull Random random, double points) {
            return new EnderTypeChangeConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.enderTypeChange.enabled() ? Mystical.CONFIG.enderTypeChange.weight() : 0);
        }
    }

}
