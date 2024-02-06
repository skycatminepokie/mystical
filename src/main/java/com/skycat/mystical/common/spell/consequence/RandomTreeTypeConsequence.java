package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Makes saplings grow into random trees.
 */
public class RandomTreeTypeConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<RandomTreeTypeConsequence> getFactory() {
        return FACTORY;
    }

    private RandomTreeTypeConsequence() {
        super(RandomTreeTypeConsequence.class, null, 15d);
    }

    public static class Factory extends ConsequenceFactory<RandomTreeTypeConsequence> {
        public Factory() {
            super("randomTreeType",
                    "Random Tree Types",
                    "The saplings are spies!",
                    "Random tree generated",
                    RandomTreeTypeConsequence.class,
                    Codec.unit(RandomTreeTypeConsequence::new));
        }

        @Override
        public @NotNull RandomTreeTypeConsequence make(@NonNull Random random, double points) {
            return new RandomTreeTypeConsequence();
        }

        @Override
        public void test(TestContext context) {
            // TODO
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.randomTreeType.enabled() ? Mystical.CONFIG.randomTreeType.weight() : 0);
        }
    }
}
