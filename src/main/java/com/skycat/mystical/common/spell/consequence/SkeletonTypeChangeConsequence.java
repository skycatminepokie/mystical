package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SkeletonTypeChangeConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<SkeletonTypeChangeConsequence> getFactory() {
        return FACTORY;
    }

    protected SkeletonTypeChangeConsequence() {
        super(SkeletonTypeChangeConsequence.class, null, 50d); // TODO: Scaling
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
        public void test(TestContext context) { // TODO
            com.skycat.mystical.common.util.Utils.log("Test not implemented for " + shortName, com.skycat.mystical.common.LogLevel.WARN);
            context.complete();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.skeletonTypeChange.enabled() ? Mystical.CONFIG.skeletonTypeChange.weight() : 0);
        }
    }
}
