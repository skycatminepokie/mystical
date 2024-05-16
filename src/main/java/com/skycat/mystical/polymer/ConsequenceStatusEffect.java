package com.skycat.mystical.polymer;

import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.effect.StatusEffect;

public class ConsequenceStatusEffect extends StatusEffect implements PolymerStatusEffect {
    protected ConsequenceFactory<?> consequenceType;

    protected ConsequenceStatusEffect(ConsequenceFactory<?> consequenceType, int color) {
        super(consequenceType.getStatusEffectCategory(), color);
        this.consequenceType = consequenceType;
    }

}
