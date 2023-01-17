package skycat.mystical.spell;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Spell {
    private SpellConsequence consequence;
    /**
     * The handler the consequence is active for.
     * For example, a consequence may be able to support
     * many events, but {@code eventClass} is the event
     * that it is used for in this instance.
     */
    private Class<?> eventClass;
    private SpellCure cure;
}
