package skycat.mystical.util;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;

public class StatCodec implements Codec<Stat<?>>, JsonSerializer<Stat<?>>, JsonDeserializer<Stat<?>> {
    public static StatCodec INSTANCE = new StatCodec();
    public static Codec<Pair<StatType<?>, Identifier>> TYPE_IDENTIFIER_CODEC = Codec.pair(
            Registry.STAT_TYPE.getCodec().fieldOf("type").codec(),
            Identifier.CODEC.fieldOf("id").codec()
    );

    public <T, S> DataResult<T> genericEncode(Stat<S> input, DynamicOps<T> ops, T prefix) {
        return TYPE_IDENTIFIER_CODEC.encodeStart(ops, Pair.of(input.getType(), input.getType().getRegistry().getId(input.getValue())));
    }

    public <T,S> Stat<S> getStat(DynamicOps<T> ops, T input) {
        var result = TYPE_IDENTIFIER_CODEC.parse(ops, input);
        if (result.result().isPresent()) {
            try {
                StatType<S> type = (StatType<S>) result.result().get().getFirst();
                Stat<S> stat = type.getOrCreateStat(type.getRegistry().get(result.result().get().getSecond()));
                return stat;
            } catch (Exception e) {
                throw new JsonParseException("Failed to deserialize Stat object: " + e.getMessage());
            }
        } else {
            throw new JsonParseException("Failed to deserialize Stat object: invalid input");
        }
    }

    @Override
    public <T> DataResult<Pair<Stat<?>, T>> decode(DynamicOps<T> ops, T input) {
        try {
            Stat<?> stat = getStat(ops, input);
            if (stat == null) {
                throw new JsonParseException("Failed to deserialize Stat object");
            }
            return DataResult.success(Pair.of(stat, input));
        } catch (Exception e) {
            return DataResult.error("Failed to deserialize Stat object: " + e.getMessage());
        }
    }

    @Override
    public <T> DataResult<T> encode(Stat<?> input, DynamicOps<T> ops, T prefix) {
        return genericEncode(input, ops, prefix);
    }

    @Override
    public Stat<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            Stat<?> stat = getStat(JsonOps.INSTANCE, json);
            if (stat == null) {
                throw new JsonParseException("Failed to deserialize Stat object");
            }
            return stat;
        } catch (Exception e) {
            throw new JsonParseException("Failed to deserialize Stat object: " + e.getMessage());
        }
    }

    @Override
    public JsonElement serialize(Stat<?> src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            JsonElement json = genericEncode(src, JsonOps.INSTANCE, JsonNull.INSTANCE).result().orElseThrow(() ->
                new JsonParseException("Failed to serialize Stat object")
            );
            return json;
        } catch (Exception e) {
            throw new JsonParseException("Failed to serialize Stat object: " + e.getMessage());
        }
    }
}
