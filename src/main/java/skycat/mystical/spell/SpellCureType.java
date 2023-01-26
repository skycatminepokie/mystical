package skycat.mystical.spell;

public enum SpellCureType {
    // One enum per deserialization method
    STAT_BACKED(StatBackedSpellCure.class);


    Class<?> clazz;

    SpellCureType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
