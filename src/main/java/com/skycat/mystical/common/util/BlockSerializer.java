package com.skycat.mystical.common.util;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class BlockSerializer implements JsonSerializer<Block>, JsonDeserializer<Block> {
    @Override
    public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registries.BLOCK.get(Identifier.tryParse(json.getAsString()));
    }

    @Override
    public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(Registries.BLOCK.getId(src));
    }
}
