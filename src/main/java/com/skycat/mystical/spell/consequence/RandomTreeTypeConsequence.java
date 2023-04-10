package com.skycat.mystical.spell.consequence;

import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.block.sapling.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Makes saplings grow into random trees.
 * {@link SpellConsequence#callbackType} is ignored!
 */
public class RandomTreeTypeConsequence extends SpellConsequence { // TODO: CONFIG: add a configurable chance
    public static final ArrayList<SaplingGenerator> SAPLING_GENERATORS = new ArrayList<>();
    public static final Factory FACTORY = new Factory();

    @Override
    public ConsequenceFactory<RandomTreeTypeConsequence> getFactory() {
        return FACTORY;
    }

    static {
        Collections.addAll(SAPLING_GENERATORS,
                new AcaciaSaplingGenerator(),
                new AzaleaSaplingGenerator(),
                new BirchSaplingGenerator(),
                new DarkOakSaplingGenerator(),
                new JungleSaplingGenerator(),
                new MangroveSaplingGenerator(0.85f), // tallChance from PropaguleBlock
                new OakSaplingGenerator(),
                new SpruceSaplingGenerator()
        );
    }

    private RandomTreeTypeConsequence(Class consequenceType, Class callbackType) {
        super(consequenceType, callbackType);
    }

    public static class Factory extends ConsequenceFactory<RandomTreeTypeConsequence> {
        public Factory() {
            super("randomTreeType", "Random Tree Types", "The saplings are spies!", "Random tree generated", RandomTreeTypeConsequence.class);
        }

        @Override
        public @NotNull RandomTreeTypeConsequence make(@NonNull Random random, double points) {
            return new RandomTreeTypeConsequence(RandomTreeTypeConsequence.class, null);
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.randomTreeType.enabled() ? Mystical.CONFIG.randomTreeType.weight() : 0);
        }
    }
}
