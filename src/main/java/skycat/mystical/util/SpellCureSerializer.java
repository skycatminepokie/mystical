package skycat.mystical.util;

import com.google.gson.*;
import skycat.mystical.spell.SpellCure;

import java.lang.reflect.Type;

public class SpellCureSerializer implements JsonSerializer<SpellCure>, JsonDeserializer<SpellCure> {
    @Override
    public SpellCure deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SpellCureType cureType = context.deserialize(json.getAsJsonObject().get("cureType"), SpellCureType.class);
        return context.deserialize(json, cureType.clazz);
    }

    @Override
    public JsonElement serialize(SpellCure src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getCureType().clazz);
    }
}
