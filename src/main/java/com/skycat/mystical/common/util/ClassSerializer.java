package com.skycat.mystical.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Serializes Class objects.
 * Warning: this might be scuffed.
 * Either it's really awesome, or really awful.
 */
@SuppressWarnings("rawtypes")
public class ClassSerializer implements JsonSerializer<Class>, JsonDeserializer<Class> {
    @Override
    public Class deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Class.forName(json.getAsString());
        } catch (ClassNotFoundException e) {
            Utils.log(Utils.translateString("text.mystical.classSerializer.failedDeserializeName", json.getAsString())); // TODO: Config
            // STOPSHIP big ol errors, dump info
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonElement serialize(Class src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }
}
