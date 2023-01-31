package skycat.mystical.util;

import com.google.gson.*;
import skycat.mystical.spell.SpellConsequence;

import java.lang.reflect.Type;

public class SpellConsequenceSerializer implements JsonSerializer<SpellConsequence>, JsonDeserializer<SpellConsequence> {

    // TODO: This could use some nicer workings, like simply using the class name instead of an enum. Same with SpellCureSerializer.
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
