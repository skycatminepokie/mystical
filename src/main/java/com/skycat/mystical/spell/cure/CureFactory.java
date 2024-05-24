package com.skycat.mystical.spell.cure;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

@FunctionalInterface
public interface CureFactory<T extends SpellCure> {
    /**
     * Make a new cure of class {@link T}
     *
     * @param random The random to use to generate anything that should be randomized
     * @return A new {@link T}.
     */
    @NotNull T make(@NotNull Random random);
}
