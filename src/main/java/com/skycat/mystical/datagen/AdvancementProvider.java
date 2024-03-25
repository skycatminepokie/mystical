package com.skycat.mystical.datagen;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.advancement.MakeHavenCriterion;
import com.skycat.mystical.common.advancement.SpellCuredCriterion;
import com.skycat.mystical.common.util.Utils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    public static final String MAKE_HAVEN_ADVANCEMENT_ID = "mystical:make_haven";
    private static final String SPELL_CURED_ADVANCEMENT_ID = "mystical:spell_cured";

    protected AdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement makeHavenAdvancement = Advancement.Builder.createUntelemetered()
                .display(
                        Items.ACACIA_SAPLING,
                        Utils.translatable("text.mystical.advancement.make_haven.title"),
                        Utils.translatable("text.mystical.advancement.make_haven.description"),
                        Identifier.of("minecraft", "textures/block/purpur_block.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(String.valueOf(Mystical.MAKE_HAVEN_CRITERION.getId()), new MakeHavenCriterion.Conditions(LootContextPredicate.EMPTY))
                .build(consumer, MAKE_HAVEN_ADVANCEMENT_ID);
        Advancement.Builder.createUntelemetered()
                .display(
                        Items.YELLOW_BANNER,
                        Utils.textOf("CURE SPELL"),
                        Utils.textOf("CURED A SPELL WITH >10%"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(String.valueOf(Mystical.SPELL_CURED_CRITERION.getId()), new SpellCuredCriterion.Conditions(LootContextPredicate.EMPTY, NumberRange.FloatRange.atLeast(0.2)))
                .parent(makeHavenAdvancement)
                .build(consumer, SPELL_CURED_ADVANCEMENT_ID);
    }
}
