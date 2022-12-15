package skycat.mystical.util;

import com.google.gson.*;
import skycat.mystical.curses.CurseRemovalCondition;

import java.lang.reflect.Type;
import java.util.HashMap;

public class ConditionSerializer implements JsonSerializer<CurseRemovalCondition>, JsonDeserializer<CurseRemovalCondition> {
    private static final HashMap<String, CurseRemovalCondition> lookupMap = new HashMap<>();
    @Override
    public CurseRemovalCondition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(CurseRemovalCondition src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
