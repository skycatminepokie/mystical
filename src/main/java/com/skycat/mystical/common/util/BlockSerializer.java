package com.skycat.mystical.common.util;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class BlockSerializer implements JsonSerializer<Block>, JsonDeserializer<Block> {
    @Override
    public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registry.BLOCK.get(Identifier.tryParse(json.getAsString()));
    }

    @Override
    public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(Registry.BLOCK.getId(src));
    }
}
