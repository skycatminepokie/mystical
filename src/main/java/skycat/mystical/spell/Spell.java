package skycat.mystical.spell;

import lombok.Getter;
import lombok.Setter;
import skycat.mystical.spell.consequence.SpellConsequence;
import skycat.mystical.spell.cure.SpellCure;

@Getter @Setter
public class Spell {
    private SpellConsequence consequence;
    /**
     * The handler the consequence is active for.
     * For example, a consequence may be able to support
     * many events, but this is the event
     * that it is used for in this instance.
     */
    private Class callbackType;
    private SpellCure cure;

    public Spell(SpellConsequence consequence, Class callbackType, SpellCure cure) {
        this.consequence = consequence;
        this.callbackType = callbackType;
        this.cure = cure;
    }
}
