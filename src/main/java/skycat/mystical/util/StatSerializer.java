package skycat.mystical.util;

import com.google.gson.*;
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
        StatType<?> type = context.deserialize(json.getAsJsonObject().get("type"), StatType.class); // Figure out the stat type
        return (type.getOrCreateStat(context.deserialize(json.getAsJsonObject().get("value"), keyClassLookup.get(type)))); // Figure out the stat value
    }

    @Override
    public JsonElement serialize(Stat<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", context.serialize(src.getType(), StatType.class));
        // https://stackoverflow.com/questions/4584541/check-if-a-class-object-is-subclass-of-another-class-object-in-java
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Class.html#isAssignableFrom(java.lang.Class)
        Class<?> valueClass = src.getValue().getClass();
        Class<?> assignTo;
        if (Block.class.isAssignableFrom(valueClass)) { // Figure out what class we want to serialize as
            assignTo = Block.class;
        } else if (Item.class.isAssignableFrom(valueClass)) {
            assignTo = Item.class;
        } else if (Identifier.class.isAssignableFrom(valueClass)) {
            assignTo = Identifier.class;
        } else {
            Utils.log("Uh-oh. Mystical couldn't serialize a Stat<?> properly.");
            object.add("value", JsonNull.INSTANCE); // TODO: More errors and logging
            return object;
        }

        object.add("value", context.serialize(src.getValue(), assignTo));
        return object;

    }
}
