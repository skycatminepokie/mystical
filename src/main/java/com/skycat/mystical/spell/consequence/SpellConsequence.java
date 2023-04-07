package com.skycat.mystical.spell.consequence;

import com.google.gson.*;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import net.minecraft.text.MutableText;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
@Getter
public abstract class SpellConsequence {
    private static final String CONSEQUENCE_TRANSLATION_PREFIX = "text.mystical.consequence.";
    private final String shortName;
    private final String description;
    private final Class consequenceType;
    // TODO Probably move callbackType to an extended class
    private final String longName;
    /**
     * If there is no relevant callback, this should be the same as consequenceType
     */
    private final Class callbackType;

    @Deprecated
    public SpellConsequence(Class consequenceType, Class callbackType) {
        this(consequenceType, callbackType, "defaultSpell", "Default Spell", "A spell that really shouldn't be here.");
    }

    public SpellConsequence(Class consequenceType, Class callbackType, String shortName, String longName, String description) {
        this.consequenceType = consequenceType;
        this.callbackType = callbackType;
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
    }

    /**
     * This is a player-readable description of the consequence.
     * Override this if you have parameters to add to the translation.
     */
    public MutableText getDescriptionText() {
        return Utils.translatable(getDescriptionKey());
    }

    public MutableText getShortNameText() {
        return Utils.translatable(getShortNameKey());
    }

    public MutableText getLongNameText() {
        return Utils.translatable(getLongNameKey());
    }

    public String getDescriptionKey() {
        return translationKey() + ".description";
    }

    public String getShortNameKey() {
        return translationKey() + ".shortName";
    }

    public String getLongNameKey() {
        return translationKey() + ".longName";
    }

    /**
     * Get the base translation "path" for this consequence
     * @implNote Returns {@link SpellConsequence#CONSEQUENCE_TRANSLATION_PREFIX} + getShortName(). There is probably not a translation at this key - this is just a base "path"
     * @return The base part of the translations for this spell
     */
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
