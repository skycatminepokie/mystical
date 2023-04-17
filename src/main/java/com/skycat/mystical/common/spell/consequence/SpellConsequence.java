package com.skycat.mystical.common.spell.consequence;

import com.google.gson.*;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
@Getter
public abstract class SpellConsequence {
    private final Class consequenceType;
    /**
     * If there is no relevant callback, this should be null
     */
    private final Class callbackType;
    private final double difficulty;

    public SpellConsequence(Class consequenceType, Class callbackType, double difficulty) {
        this.consequenceType = consequenceType;
        this.callbackType = callbackType;
        this.difficulty = difficulty;
    }

    /*
        // I really don't know if this would work.
        public SpellConsequence(Class callbackType) {
            this.consequenceType = this.getClass();
            this.callbackType = callbackType;
        }
     */

    /**
     * Returns the factory associated with this type of consequence.
     * {@code ? extends SpellConsequence} should ALWAYS be {@code this.getClass()}.
     *
     * @return The factory associated with this type of consequence.
     */
    @NonNull
    public abstract ConsequenceFactory<? extends SpellConsequence> getFactory(); // TODO: Find a way to force ? to be this.getClass().

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
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
