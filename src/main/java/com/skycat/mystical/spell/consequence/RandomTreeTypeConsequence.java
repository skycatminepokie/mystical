package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.block.sapling.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Makes saplings grow into random trees.
 */
public class RandomTreeTypeConsequence extends SpellConsequence { // TODO: Tests
    public static final ArrayList<SaplingGenerator> SAPLING_GENERATORS = new ArrayList<>();
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<RandomTreeTypeConsequence> getFactory() {
        return FACTORY;
    }

    static {
        Collections.addAll(SAPLING_GENERATORS,
                new AcaciaSaplingGenerator(),
                new AzaleaSaplingGenerator(),
                new BirchSaplingGenerator(),
                new CherrySaplingGenerator(),
                new DarkOakSaplingGenerator(),
                new JungleSaplingGenerator(),
                new MangroveSaplingGenerator(0.85f), // tallChance from PropaguleBlock
                new OakSaplingGenerator(),
                new SpruceSaplingGenerator()
        );
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
        public double getWeight() {
            return (Mystical.CONFIG.randomTreeType.enabled() ? Mystical.CONFIG.randomTreeType.weight() : 0);
        }
    }
}
