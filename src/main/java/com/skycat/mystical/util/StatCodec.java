package com.skycat.mystical.util;

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
            // Success
            try {
                @SuppressWarnings("unchecked")
                StatType<S> type = (StatType<S>) result.result().get().getFirst(); // I'm pretty sure this is a safe cast if all the data is valid
                Stat<S> stat = type.getOrCreateStat(type.getRegistry().get(result.result().get().getSecond()));
                return stat;
            } catch (ClassCastException e) {
                Utils.log("Stat could not be deserialized properly. Printing stacktrace.");
                e.printStackTrace();
            }
        } else {
            // TODO better error handling
            throw new RuntimeException("Stat could not be deserialized- result was not present.");
        }
        return null; // WARN error handling
    }

    @Override
    public <T> DataResult<Pair<Stat<?>, T>> decode(DynamicOps<T> ops, T input) {
        // In the case of using json, T is JsonElement
        return DataResult.success(Pair.of(getStat(ops, input), input)); // TODO Error handling
    }

    @Override
    public <T> DataResult<T> encode(Stat<?> input, DynamicOps<T> ops, T prefix) {
        return genericEncode(input, ops, prefix);
    }

    @Override
    public Stat<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return getStat(JsonOps.INSTANCE, json);
    }

    @Override
    public JsonElement serialize(Stat<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return encodeStart(JsonOps.INSTANCE, src).getOrThrow(false, s -> Utils.log("Could not parse the following string as a stat: \"" + s + "\".")); // WARN Error checking
    }
}
