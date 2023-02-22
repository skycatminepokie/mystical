package skycat.mystical.util;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.stat.StatType;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;

public class StatTypeSerializer implements JsonSerializer<StatType<?>>, JsonDeserializer<StatType<?>> {

    @Override
    public StatType<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registry.STAT_TYPE.getCodec().parse(JsonOps.INSTANCE, json).getOrThrow(false, (s -> Utils.log("error line 15 stattypeserializer this is very bad practice")));
    }

    @Override
    public JsonElement serialize(StatType<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return Registry.STAT_TYPE.getCodec().encodeStart(JsonOps.INSTANCE, src).getOrThrow(false, (s -> Utils.log("error serializing stat type this is awful practice ok bye")));
    }
}
