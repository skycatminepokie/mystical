package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SoundSwapConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public SoundSwapConsequence() {
        super(SoundSwapConsequence.class, null, 1d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<SoundSwapConsequence> {

        protected Factory() { // TODO: Change this up
            super("soundSwap", "Sound swapping", "Ears broken", "Swapped a sound", SoundSwapConsequence.class, Codec.unit(SoundSwapConsequence::new));
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.soundSwap.weight();
        }

        @Override
        public @NotNull SoundSwapConsequence make(@NonNull Random random, double points) {
            return new SoundSwapConsequence();
        }
    }
}
