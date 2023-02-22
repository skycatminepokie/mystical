package skycat.mystical.util;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.HashMap;


public class StatSerializer implements JsonSerializer<Stat<?>>, JsonDeserializer<Stat<?>> {
    // A somewhat weird way of figuring out what kind of objects are supported by each stat
    // Probably doesn't work for EntityTypes.
    // Adapted from Stats.java
    @SuppressWarnings("rawtypes") private static final HashMap<StatType, Class> keyClassLookup = new HashMap<>();
    static {
        keyClassLookup.put(Stats.MINED, Block.class);
        keyClassLookup.put(Stats.CRAFTED, Item.class);
        keyClassLookup.put(Stats.USED, Item.class);
        keyClassLookup.put(Stats.BROKEN, Item.class);
        keyClassLookup.put(Stats.PICKED_UP, Item.class);
        keyClassLookup.put(Stats.DROPPED, Item.class);
        keyClassLookup.put(Stats.KILLED, EntityType.class);
        keyClassLookup.put(Stats.KILLED_BY, EntityType.class);
        keyClassLookup.put(Stats.CUSTOM, Identifier.class);
    }

    @Override
    public Stat<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        StatType<?> type = context.deserialize(json, StatType.class); // Figure out the stat type
        return (Stat<?>) type.getRegistry().getCodec().parse(JsonOps.INSTANCE, json).getOrThrow(false, s -> Utils.log("awful"));
    }

    @Override
    public JsonElement serialize(Stat<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return serializeGeneric(src, typeOfSrc, context);
    }

    public <T> JsonElement serializeGeneric(Stat<T> src, Type typeOfSrc, JsonSerializationContext context) { // ChatGPT is what helped me fix this, believe it or not.
        return src.getType().getRegistry().getCodec().encodeStart(JsonOps.INSTANCE, src.getValue()).getOrThrow(false, s -> Utils.log("error and awful"));
    }
}
