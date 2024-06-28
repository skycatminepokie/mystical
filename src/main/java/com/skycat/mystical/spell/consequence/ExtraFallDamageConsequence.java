package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ExtraFallDamageConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    public ExtraFallDamageConsequence() {
        super(ExtraFallDamageConsequence.class, null, Mystical.CONFIG.extraFallDamage.multiplier() * 25);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<ExtraFallDamageConsequence> {

        protected Factory() {
            super("extraFallDamage",
                    "Extra fall damage",
                    "splat -> SPLAT",
                    "Splatting bigger",
                    ExtraFallDamageConsequence.class,
                    MapCodec.unit(ExtraFallDamageConsequence::new));
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.extraFallDamage.enabled() ? Mystical.CONFIG.extraFallDamage.weight() : 0;
        }

        @Override
        public @NotNull ExtraFallDamageConsequence make(@NotNull Random random, double points) {
            return new ExtraFallDamageConsequence();
        }
    }
}
