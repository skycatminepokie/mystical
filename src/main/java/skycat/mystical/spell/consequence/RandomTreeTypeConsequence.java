package skycat.mystical.spell.consequence;

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
public class RandomTreeTypeConsequence extends SpellConsequence { // TODO: add a configurable chance
    public static final ArrayList<SaplingGenerator> SAPLING_GENERATORS = new ArrayList<>();
    public static final Factory FACTORY = new Factory();

    static {
        Collections.addAll(SAPLING_GENERATORS,
                new AcaciaSaplingGenerator(),
                new BirchSaplingGenerator(),
                new DarkOakSaplingGenerator(),
                new OakSaplingGenerator(),
                new SpruceSaplingGenerator(),
                new AzaleaSaplingGenerator(),
                new JungleSaplingGenerator()
                );
    }
    private RandomTreeTypeConsequence(Class consequenceType, Class callbackType) {
        super(consequenceType, callbackType);
    }
    public static class Factory implements ConsequenceFactory<RandomTreeTypeConsequence> {
        @Override
        public @NotNull RandomTreeTypeConsequence make(@NonNull Random random, double points) {
            return new RandomTreeTypeConsequence(RandomTreeTypeConsequence.class, null);
        }
    }
}
