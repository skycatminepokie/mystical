package com.skycat.mystical;

import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.spell.consequence.SpellConsequence;
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
            newList.add(new StatusEffectInstance(FakeStatusEffect.getOrCreate(
                    Identifier.of(Mystical.MOD_ID, SPELL_EFFECT_PREFIX + consequenceFactory.shortName), // TODO: Gametest that these all exist
                    difficultyToStatusEffectCategory(consequence.getDifficulty()))));
        }
        fakeStatusEffects = newList;
    }
}
