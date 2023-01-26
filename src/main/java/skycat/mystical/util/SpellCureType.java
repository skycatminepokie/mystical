package skycat.mystical.util;

import com.google.gson.annotations.SerializedName;
import skycat.mystical.spell.SpellCure;
import skycat.mystical.spell.StatBackedSpellCure;

public enum SpellCureType { // I suppose we could actually serialize a Class, but that sounds like it'll cause a lot more problems when given strange input
    @SerializedName("STAT_BACKED")
    STAT_BACKED(StatBackedSpellCure.class);

    SpellCureType(Class<? extends SpellCure> clazz) {
        this.clazz = clazz;
    }

    public final Class<? extends SpellCure> clazz;
}
