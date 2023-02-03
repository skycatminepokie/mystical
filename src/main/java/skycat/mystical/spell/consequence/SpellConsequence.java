package skycat.mystical.spell.consequence;

import com.google.gson.*;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public abstract class SpellConsequence {
    private final Class consequenceType;
    private final Class callback;

    public SpellConsequence(Class consequenceType, Class callback) {
        this.consequenceType = consequenceType;
        this.callback = callback;
    }

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
