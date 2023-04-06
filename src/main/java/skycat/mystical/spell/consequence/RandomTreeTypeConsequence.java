package skycat.mystical.spell.consequence;

import lombok.NonNull;
import net.minecraft.block.sapling.*;
import org.jetbrains.annotations.NotNull;
import skycat.mystical.Mystical;

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
        super(consequenceType, callbackType, "randomTreeType", "Random Tree Types", "The saplings are spies!");
    }

    public static class Factory implements ConsequenceFactory<RandomTreeTypeConsequence> {
        @Override
        public @NotNull RandomTreeTypeConsequence make(@NonNull Random random, double points) {
            return new RandomTreeTypeConsequence(RandomTreeTypeConsequence.class, RandomTreeTypeConsequence.class);
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.randomTreeType.enabled()?Mystical.CONFIG.randomTreeType.weight():0);
        }
    }
}
