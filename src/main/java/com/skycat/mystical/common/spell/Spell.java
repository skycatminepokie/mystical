package com.skycat.mystical.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.common.spell.consequence.SpellConsequence;
import com.skycat.mystical.common.spell.cure.SpellCure;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Spell {
    // https://forge.gemwire.uk/wiki/Codecs
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(spellInstance -> spellInstance.group(
            SpellConsequence.CODEC.fieldOf("consequence").forGetter(Spell::getConsequence),
            SpellCure.CODEC.fieldOf("cure").forGetter(Spell::getCure)
    ).apply(spellInstance, Spell::new));
    private SpellConsequence consequence;
    private SpellCure cure;

    public Spell(SpellConsequence consequence, SpellCure cure) {
        this.consequence = consequence;
        this.cure = cure;
    }
}
