package com.skycat.mystical.spell.cure;

import com.mojang.serialization.Codec;

import java.util.HashMap;

public class CureTypes {
    private static final HashMap<String, CureType<?>> ID_TO_TYPE = new HashMap<>();
    private static final HashMap<CureType<?>, String> TYPE_TO_ID = new HashMap<>();
    public static final Codec<CureType<?>> TYPE_CODEC = Codec.STRING.xmap(CureTypes::getType, CureTypes::getId);

    // Register types here
    public static final CureType<StatBackedSpellCure> STAT_BACKED = registerType("stat_backed", new StatBackedCureType());

    // Stuff to deal with registering
    public static String getId(CureType<?> type) {
        return TYPE_TO_ID.get(type);
    }

    public static CureType<?> getType(String id) {
        return ID_TO_TYPE.get(id);
    }

    public static <T extends SpellCure> CureType<T> registerType(String id, CureType<T> type) {
        ID_TO_TYPE.put(id, type);
        TYPE_TO_ID.put(type, id);
        return type;
    }
}
