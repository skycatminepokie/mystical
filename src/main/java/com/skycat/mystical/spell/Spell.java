package com.skycat.mystical.spell;

import com.skycat.mystical.spell.consequence.SpellConsequence;
import com.skycat.mystical.spell.cure.SpellCure;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Spell {
    private SpellConsequence consequence;
    private SpellCure cure;

    public Spell(SpellConsequence consequence, SpellCure cure) {
        this.consequence = consequence;
        this.cure = cure;
    }
}
