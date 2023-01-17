package skycat.mystical.util;

import com.google.gson.*;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;

public class StatTypeSerializer implements JsonSerializer<StatType<?>>, JsonDeserializer<StatType<?>> {

    @Override
    public StatType<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registry.STAT_TYPE.get((Identifier) context.deserialize(json, Identifier.class));
    }

    @Override
    public JsonElement serialize(StatType<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(Registry.STAT_TYPE.getId(src), Identifier.class);
    }
}
