package skycat.mystical.util;

import skycat.mystical.spell.KillOnSleepConsequence;
import skycat.mystical.spell.SpellConsequence;

public enum SpellConsequenceType {
    KILL_ON_SLEEP(KillOnSleepConsequence.class);

    public final Class<? extends SpellConsequence> clazz;
    SpellConsequenceType(Class<? extends SpellConsequence> clazz) {
        this.clazz = clazz;
    }
}
