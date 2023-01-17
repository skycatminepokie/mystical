package skycat.mystical.util;

import com.google.gson.*;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;

import java.lang.reflect.Type;


public class StatSerializer implements JsonSerializer<Stat<?>>, JsonDeserializer<Stat<?>> {
    @Override
    public Stat<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        StatType type = context.deserialize(json.getAsJsonObject().get("type"), StatType.class); // Deserialize the type
        return (type.getOrCreateStat(context.deserialize(json.getAsJsonObject().get("value"), Object.class)));
                 // Get the stat from the StatType
    }

    @Override
    public JsonElement serialize(Stat<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", context.serialize(src.getType(), StatType.class));
        object.add("value", context.serialize(src.getValue(), ));
        src.

        return object;

    }
}
