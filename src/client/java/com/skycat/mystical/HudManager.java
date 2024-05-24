package com.skycat.mystical;

import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.spell.consequence.LevitateConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class HudManager {
    protected List<Spell> cachedSpells = Collections.emptyList();
    private static final String SPELL_EFFECT_PREFIX = "spell/";
    public static final Identifier SPELL_OUTLINE = Identifier.of(Mystical.MOD_ID, "hud/spell_background");
    protected StatusEffectCategory difficultyToStatusEffectCategory(double difficulty) {
        if (difficulty > 0) return StatusEffectCategory.HARMFUL;
        if (difficulty < 0) return StatusEffectCategory.BENEFICIAL;
        return StatusEffectCategory.NEUTRAL;
    }

    public List<Spell> getCachedSpells() {
        return cachedSpells;
    }

    public void updateCachedSpells(List<Spell> spells) {
        cachedSpells = spells;
        updateFakeStatusEffects();
    }

    protected void updateFakeStatusEffects() { // TODO Fix display

    }

    protected Identifier iconForSpell(ConsequenceFactory<?> consequenceFactory) {
        if (consequenceFactory.equals(LevitateConsequence.FACTORY)) {
            return Identifier.of("minecraft", "levitation"); // It'll be differentiated by the spell border
        }
        return Identifier.of(Mystical.MOD_ID, Utils.camelCaseToSnakeCase(SPELL_EFFECT_PREFIX + consequenceFactory.shortName));
    }
}
