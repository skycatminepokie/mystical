package skycat.mystical.spell.consequence;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface ConsequenceFactory<T extends SpellConsequence> {
    /**
     * Make a new consequence of class {@link T}
     * @param random The random to use to generate anything that should be randomized
     * @param points The point target to aim for.
     * @return A new {@link T}.
     */
    @NotNull T make(@NonNull Random random, double points);

    /**
     * Return the config option's chance for the consequence, or 0 if it's disabled
     * Probably an awful way to do this
     * @return the config option's chance for the consequence, or 0 if it's disabled
     */
    double getWeight();
}
