package com.skycat.mystical.spell.consequence;

import com.google.gson.*;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import net.minecraft.text.MutableText;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
@Getter
public abstract class SpellConsequence {
    @Deprecated
    private static final String CONSEQUENCE_TRANSLATION_PREFIX = "text.mystical.consequence.";
    @Deprecated
    private final String shortName;
    @Deprecated
    private final String description;
    @Deprecated
    private final Class consequenceType;
    // TODO Probably move callbackType to an extended class
    @Deprecated
    private final String longName;
    /**
     * If there is no relevant callback, this should be the same as consequenceType
     */
    private final Class callbackType;

    @Deprecated
    public SpellConsequence(Class consequenceType, Class callbackType) {
        this(consequenceType, callbackType, "defaultSpell", "Default Spell", "A spell that really shouldn't be here.");
    }

    @Deprecated
    public SpellConsequence(Class consequenceType, Class callbackType, String shortName, String longName, String description) {
        this.consequenceType = consequenceType;
        this.callbackType = callbackType;
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    public SpellConsequence(Class callbackType) {
        this.consequenceType = null;
        this.callbackType = callbackType;
        this.shortName = null;
        this.longName = null;
        this.description = null;
    }

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
    }

    /**
     * This is a player-readable description of the consequence.
     * Override this if you have parameters to add to the translation.
     */
    @Deprecated
    public MutableText getDescriptionText() {
        return Utils.translatable(getDescriptionKey());
    }

    @Deprecated
    public String getDescriptionKey() {
        return translationKey() + ".description";
    }

    /**
     * Get the base translation "path" for this consequence
     * @implNote Returns {@link SpellConsequence#CONSEQUENCE_TRANSLATION_PREFIX} + getShortName(). There is probably not a translation at this key - this is just a base "path"
     * @return The base part of the translations for this spell
     */
    @Deprecated
    protected String translationKey() {
        return CONSEQUENCE_TRANSLATION_PREFIX + getShortName();
    }

    public static class Serializer implements JsonSerializer<SpellConsequence>, JsonDeserializer<SpellConsequence> {
        @Override
        public SpellConsequence deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Class consequenceType = context.deserialize(json.getAsJsonObject().get("consequenceType"), Class.class);
            return context.deserialize(json, consequenceType);
        }

        @Override
        public JsonElement serialize(SpellConsequence src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src, src.getConsequenceType());
        }
    }
}
