package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FishingRodSwapConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    protected FishingRodSwapConsequence() {
        super(FishingRodSwapConsequence.class, null, -5d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<FishingRodSwapConsequence> {

        protected Factory() {
            super("fishingRodSwap",
                    "Fishing Rods Pull User",
                    "Fish fish fishers.",
                    "Fishing rod pulling user",
                    FishingRodSwapConsequence.class,
                    Codec.unit(new FishingRodSwapConsequence()));
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.fishingRodSwap.enabled() ? Mystical.CONFIG.fishingRodSwap.weight() : 0);
        }

        @Override
        public @NotNull FishingRodSwapConsequence make(@NonNull Random random, double points) {
            return new FishingRodSwapConsequence();
        }
    }
}
