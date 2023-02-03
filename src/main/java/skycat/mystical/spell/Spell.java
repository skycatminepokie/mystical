package skycat.mystical.spell;

import lombok.Getter;
import lombok.Setter;
import skycat.mystical.spell.consequence.SpellConsequence;
import skycat.mystical.spell.cure.SpellCure;

@Getter @Setter
public class Spell {
    private SpellConsequence consequence;
    private SpellCure cure;

    public Spell(SpellConsequence consequence, SpellCure cure) {
        this.consequence = consequence;
        this.cure = cure;
    }
}
