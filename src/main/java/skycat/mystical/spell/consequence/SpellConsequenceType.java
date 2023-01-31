package skycat.mystical.spell.consequence;

public enum SpellConsequenceType {
    KILL_ON_SLEEP(KillOnSleepConsequence.class);

    public final Class<? extends SpellConsequence> clazz;
    SpellConsequenceType(Class<? extends SpellConsequence> clazz) {
        this.clazz = clazz;
    }
}
