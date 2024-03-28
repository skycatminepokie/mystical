package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SkeletonTypeChangeConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<SkeletonTypeChangeConsequence> getFactory() {
        return FACTORY;
    }

    protected SkeletonTypeChangeConsequence() {
        super(SkeletonTypeChangeConsequence.class, null, 50d);
    }

    public static class Factory extends ConsequenceFactory<SkeletonTypeChangeConsequence> {
        public Factory() {
            super("skeletonTypeChange",
                    "Skeleton Type Change",
                    "Skeletons are having a wardrobe crisis too!",
                    "Skeleton type changed",
                    SkeletonTypeChangeConsequence.class,
                    Codec.unit(SkeletonTypeChangeConsequence::new));
        }

        @Override
        public @NotNull SkeletonTypeChangeConsequence make(@NonNull Random random, double points) {
            return new SkeletonTypeChangeConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.skeletonTypeChange.enabled() ? Mystical.CONFIG.skeletonTypeChange.weight() : 0);
        }
    }
}
