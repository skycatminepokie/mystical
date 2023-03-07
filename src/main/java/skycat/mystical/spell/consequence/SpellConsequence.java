package skycat.mystical.spell.consequence;

import com.google.gson.*;
import lombok.Getter;
import net.minecraft.text.MutableText;
import skycat.mystical.util.Utils;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
@Getter
public abstract class SpellConsequence {
    private final Class consequenceType;
    // TODO Probably move callbackType to an extended class
    /**
     * If there is no relevant callback, this should be the same as consequenceType
     */
    private final Class callbackType;
    private final String translationKey;

    public SpellConsequence(Class consequenceType, Class callbackType) {
        this(consequenceType, callbackType, "text.mystical.spellConsequence.default");
    }

    public SpellConsequence(Class consequenceType, Class callbackType, String translationKey) {
        this.consequenceType = consequenceType;
        this.callbackType = callbackType;
        this.translationKey = translationKey;
    }

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
    }

    /**
     * This is a player-readable description of the consequence.
     * Override this if you have parameters to add to the translation.
     */
    public MutableText getDescription() {
        return Utils.translatable(translationKey);
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
