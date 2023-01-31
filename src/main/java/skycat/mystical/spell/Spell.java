package skycat.mystical.spell;

import lombok.Getter;
import lombok.Setter;
import skycat.mystical.spell.consequence.SpellConsequence;
import skycat.mystical.spell.cure.SpellCure;
import skycat.mystical.util.EventCallbackEnum;

@Getter @Setter
public class Spell {
    private SpellConsequence consequence;
    /**
     * The handler the consequence is active for.
     * For example, a consequence may be able to support
     * many events, but {@code eventClass} is the event
     * that it is used for in this instance.
     */
    private EventCallbackEnum callbackType;
    private SpellCure cure;

    public Class<?> getEventClass() {
        return callbackType.getClazz();
    }

    public Spell(SpellConsequence consequence, EventCallbackEnum callbackType, SpellCure cure) {
        this.consequence = consequence;
        this.callbackType = callbackType;
        this.cure = cure;
    }
}
