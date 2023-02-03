package skycat.mystical.spell.consequence;

import java.util.Random;

@FunctionalInterface
public interface ConsequenceFactory<T extends SpellConsequence> {
    /**
     * Make a new consequence of class {@link T}
     * @param random The random to use to generate anything that should be randomized
     * @param points The point target to aim for.
     * @return A new {@link T}.
     */
    T make(Random random, double points);
}
