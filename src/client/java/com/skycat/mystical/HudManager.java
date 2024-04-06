package com.skycat.mystical;

import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.spell.consequence.LevitateConsequence;
import com.skycat.mystical.spell.consequence.SpellConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HudManager {
    protected List<Spell> cachedSpells = Collections.emptyList();
    protected List<StatusEffectInstance> fakeStatusEffects = Collections.emptyList();
    private static final String SPELL_EFFECT_PREFIX = "spell/";
    public static final Identifier SPELL_OUTLINE = Identifier.of(Mystical.MOD_ID, "textures/mob_effect/spell/outline.png");
    protected StatusEffectCategory difficultyToStatusEffectCategory(double difficulty) {
        if (difficulty > 0) return StatusEffectCategory.HARMFUL;
        if (difficulty < 0) return StatusEffectCategory.BENEFICIAL;
        return StatusEffectCategory.NEUTRAL;
    }

    public List<Spell> getCachedSpells() {
        return cachedSpells;
    }

    public List<StatusEffectInstance> getFakeStatusEffects() {
        return fakeStatusEffects;
    }

    public void updateCachedSpells(List<Spell> spells) {
        cachedSpells = spells;
        updateFakeStatusEffects();
    }

    protected void updateFakeStatusEffects() {
        List<StatusEffectInstance> newList = new ArrayList<>();
        for (Spell spell : cachedSpells) {
            SpellConsequence consequence = spell.getConsequence();
            ConsequenceFactory<?> consequenceFactory = consequence.getFactory();
            newList.add(new StatusEffectInstance(SpellStatusEffect.getOrCreate( // TODO: Don't have multiple of the same?
                    iconForSpell(consequenceFactory), // TODO: Gametest that these all exist
                    difficultyToStatusEffectCategory(consequence.getDifficulty())), -1));
        }
        fakeStatusEffects = newList;
    }

    protected Identifier iconForSpell(ConsequenceFactory<?> consequenceFactory) {
        if (consequenceFactory.equals(LevitateConsequence.FACTORY)) {
            return Identifier.of("minecraft", "levitation"); // It'll be differentiated by the spell border
        }
        return Identifier.of(Mystical.MOD_ID, Utils.camelCaseToSnakeCase(SPELL_EFFECT_PREFIX + consequenceFactory.shortName));
    }
}
