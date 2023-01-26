package skycat.mystical.util;

import com.google.gson.*;
import skycat.mystical.spell.SpellCure;
import skycat.mystical.spell.StatBackedSpellCure;

import java.lang.reflect.Type;

public class SpellCureSerializer implements JsonSerializer<SpellCure>, JsonDeserializer<SpellCure> {
    @Override
    public SpellCure deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SpellCureType cureType = context.deserialize(json.getAsJsonObject().get("cureType"), SpellCureType.class);
        if (cureType == SpellCureType.STAT_BACKED) {
            return context.deserialize(json, StatBackedSpellCure.class);
        }
        // TODO: logging, error throwing
        // Should not get here in normal circumstances
        return null;
    }

    @Override
    public JsonElement serialize(SpellCure src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getCureType().clazz);
    }
}
