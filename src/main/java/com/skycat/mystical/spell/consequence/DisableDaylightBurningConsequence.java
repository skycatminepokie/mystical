package com.skycat.mystical.spell.consequence;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DisableDaylightBurningConsequence extends SpellConsequence { // TODO: Config
    public static Factory FACTORY = new Factory();

    protected DisableDaylightBurningConsequence() {
        super(DisableDaylightBurningConsequence.class, null);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<DisableDaylightBurningConsequence> {

        protected Factory() {
            super("disableDaylightBurning", "Disable Daylight Burning", "Mobs bought some sunglasses", "Said no this mob doesn't burn", DisableDaylightBurningConsequence.class);
        }

        @Override
        public @NotNull DisableDaylightBurningConsequence make(@NonNull Random random, double points) {
            return new DisableDaylightBurningConsequence();
        }

        @Override
        public double getWeight() {
            return 1; // TODO: Config
        }
    }
}
