package skycat.mystical.spell;

import lombok.Getter;
import skycat.mystical.util.SpellConsequenceType;

@Getter
public /* abstract */ class SpellConsequence {
    private final SpellConsequenceType consequenceType;

    public SpellConsequence(SpellConsequenceType consequenceType) {
        this.consequenceType = consequenceType;
    }

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
    }
}
