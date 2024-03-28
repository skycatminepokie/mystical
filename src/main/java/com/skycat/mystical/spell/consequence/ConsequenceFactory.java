package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.spell.Spells;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.test.GameTest;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

/**
 * A factory for a {@link SpellConsequence}.
 *
 * @implSpec Register in {@link Spells}<br>
 * Implement {@link GameTest} annotated methods for spell/no spell and haven/no haven.<br>
 * {@link GameTest#batchId()} is ignored - it will never be run in parallel. See {@link com.skycat.mystical.test.MysticalTests#getSpellTests(ArrayList)}.
 */
public abstract class ConsequenceFactory<T extends SpellConsequence> {
    public static final Codec<ConsequenceFactory<?>> FACTORY_CODEC = Codec.STRING.xmap(Spells::getFactory, ConsequenceFactory::getShortName);
    public static final String CONSEQUENCE_TRANSLATION_PREFIX = "text.mystical.consequence.";
    @Getter public final String shortName;
    @Getter public final String longName;
    @Getter public final String description;
    @Getter public final String firedMessage;
    @Getter public final Class<T> consequenceType;
    @Getter public final Codec<T> codec;

    protected ConsequenceFactory(String shortName, String longName, String description, String firedMessage, Class<T> consequenceType, Codec<T> codec) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.firedMessage = firedMessage;
        this.consequenceType = consequenceType;
        this.codec = codec;
    }

    public String getDescriptionKey() {
        return translationKey() + ".description";
    }

    /**
     * This is a player-readable description of the consequence.
     * Override this if you have parameters to add to the translation.
     *
     * @param consequence The consequence to get the description of. Used for parameterized translations.
     * @throws IllegalArgumentException If consequence.class != consequenceType
     */
    public MutableText getDescriptionText(SpellConsequence consequence) throws IllegalArgumentException {
        if (consequence.getClass() != consequenceType) {
            throw new IllegalArgumentException(consequence.getClass() + " != " + consequenceType);
        }
        return Utils.translatable(getDescriptionKey());
    }

    public String getLongNameKey() {
        return translationKey() + ".longName";
    }

    public MutableText getLongNameText() {
        return Utils.translatable(getLongNameKey());
    }

    public String getShortNameKey() {
        return translationKey() + ".shortName";
    }

    public MutableText getShortNameText() {
        return Utils.translatable(getShortNameKey());
    }

    /**
     * Return the config option's chance for the consequence, or 0 if it's disabled
     * Probably an awful way to do this
     *
     * @return the config option's chance for the consequence, or 0 if it's disabled
     */
    public abstract double getWeight();

    /**
     * Make a new consequence of class {@link T}
     *
     * @param random The random to use to generate anything that should be randomized
     * @param points The point target to aim for.
     * @return A new {@link T}.
     */
    @NotNull
    public abstract T make(@NonNull Random random, double points);

    /**
     * Get the base translation "path" for this consequence
     *
     * @return The base part of the translations for this spell
     * @implNote Returns {@link ConsequenceFactory#CONSEQUENCE_TRANSLATION_PREFIX} + getShortName(). There is probably not a translation at this key - this is just a base "path"
     */
    public String translationKey() {
        return CONSEQUENCE_TRANSLATION_PREFIX + getShortName();
    }
}
