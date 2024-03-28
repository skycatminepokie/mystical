package com.skycat.mystical;

import com.skycat.mystical.advancement.MakeHavenCriterion;
import com.skycat.mystical.advancement.SpellCuredCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.util.Identifier;

public class MysticalCriteria {
    public static final MakeHavenCriterion MAKE_HAVEN_CRITERION = Criteria.register(new MakeHavenCriterion());
    public static final SpellCuredCriterion SPELL_CURED_CRITERION = Criteria.register(new SpellCuredCriterion());
    public static final Identifier PREVENTED_BREAKING_CRITERION_ID = Identifier.of(Mystical.MOD_ID, "prevented_breaking");
    public static final ItemCriterion PREVENTED_BREAKING_CRITERION = Criteria.register(new ItemCriterion(PREVENTED_BREAKING_CRITERION_ID));
    public static void init() {}
}
