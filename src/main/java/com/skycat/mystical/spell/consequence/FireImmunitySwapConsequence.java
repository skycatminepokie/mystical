package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FireImmunitySwapConsequence extends SpellConsequence { // TODO: Tests, icon
    public static final Factory FACTORY = new Factory();
    public FireImmunitySwapConsequence() {
        super(FireImmunitySwapConsequence.class, null, -15d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<FireImmunitySwapConsequence> {

        protected Factory() {
            super("fireImmunitySwap",
                    "Swap fire immunity",
                    "!fire",
                    "Fire immunity swapped",
                    FireImmunitySwapConsequence.class,
                    MapCodec.unit(FireImmunitySwapConsequence::new));
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.fireImmunitySwap.enabled() ? Mystical.CONFIG.fireImmunitySwap.weight() : 0;
        }

        @Override
        public @NotNull FireImmunitySwapConsequence make(@NotNull Random random, double points) {
            return new FireImmunitySwapConsequence();
        }
    }
}
