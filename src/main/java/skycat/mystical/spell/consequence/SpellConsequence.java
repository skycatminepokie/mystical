package skycat.mystical.spell.consequence;

import com.google.gson.*;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public /* abstract */ class SpellConsequence {
    private final SpellConsequenceType consequenceType;

    public SpellConsequence(SpellConsequenceType consequenceType) {
        this.consequenceType = consequenceType;
    }

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
    }

    public static class Serializer implements JsonSerializer<SpellConsequence>, JsonDeserializer<SpellConsequence> {

        // TODO: This could use some nicer workings, like simply using the class name instead of an enum. Same with SpellCure.Serializer.
        @Override
        public SpellConsequence deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            SpellConsequenceType consequenceType = context.deserialize(json.getAsJsonObject().get("consequenceType"), SpellConsequenceType.class);
            return context.deserialize(json, consequenceType.clazz);
        }

        @Override
        public JsonElement serialize(SpellConsequence src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src, src.getConsequenceType().clazz);
        }
    }
}
