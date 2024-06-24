package com.skycat.mystical;

import com.skycat.mystical.advancement.MakeHavenCriterion;
import com.skycat.mystical.advancement.SpellCuredCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MysticalCriteria {
    public static final MakeHavenCriterion MAKE_HAVEN_CRITERION = Criteria.register(MakeHavenCriterion.ID.toTranslationKey(), new MakeHavenCriterion());
    public static final SpellCuredCriterion SPELL_CURED_CRITERION = Criteria.register(SpellCuredCriterion.ID.toTranslationKey(), new SpellCuredCriterion());
    public static final @NotNull Identifier PREVENTED_BREAKING_CRITERION_ID = Objects.requireNonNull(Identifier.tryParse(Mystical.MOD_ID, "prevented_breaking"));
    public static final ItemCriterion PREVENTED_BREAKING_CRITERION = Criteria.register(PREVENTED_BREAKING_CRITERION_ID.toTranslationKey(), new ItemCriterion());
    public static void init() {}
}
