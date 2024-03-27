package com.skycat.mystical.datagen;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.advancement.MakeHavenCriterion;
import com.skycat.mystical.common.advancement.SpellCuredCriterion;
import com.skycat.mystical.common.util.Utils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    public static final String CURE_SPELL_ADVANCEMENT_ID = "mystical";
    public static final String MAKE_HAVEN_ADVANCEMENT_ID = "mystical/make_haven";
    public static final String SOLO_SPELL_ADVANCEMENT_ID = "mystical/solo_spell";
    public static final String DOUBLE_CURE_ADVANCEMENT_ID = "mystical/double_cure";
    public static final String PREVENTED_BREAKING_ADVANCEMENT_ID = "mystical/prevented_breaking";
    public static final String PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID = "mystical/prevented_breaking/ancient_debris";

    protected AdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement cureSpell = Advancement.Builder.createUntelemetered()
                .display(
                        Items.DRAGON_BREATH,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(CURE_SPELL_ADVANCEMENT_ID) + ".title"), // Inefficient due to calculating the key multiple times? Yes. Do I care? Not really, no.
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(CURE_SPELL_ADVANCEMENT_ID) + ".description"),
                        Identifier.of("minecraft", "textures/block/purpur_block.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                )
                .criterion(String.valueOf(Mystical.SPELL_CURED_CRITERION.getId()), new SpellCuredCriterion.Conditions(LootContextPredicate.EMPTY, NumberRange.FloatRange.atLeast(0.2)))
                .build(consumer, CURE_SPELL_ADVANCEMENT_ID);
        Advancement makeHaven = Advancement.Builder.createUntelemetered()
                .display(
                        Items.STONE_BRICKS,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(MAKE_HAVEN_ADVANCEMENT_ID) + ".title"),
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(MAKE_HAVEN_ADVANCEMENT_ID) + ".description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(String.valueOf(Mystical.MAKE_HAVEN_CRITERION.getId()), new MakeHavenCriterion.Conditions(LootContextPredicate.EMPTY))
                .parent(cureSpell)
                .build(consumer, MAKE_HAVEN_ADVANCEMENT_ID);
        Advancement soloSpell = Advancement.Builder.createUntelemetered()
                .display(
                        Items.FIREWORK_ROCKET,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(SOLO_SPELL_ADVANCEMENT_ID) + ".title"),
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(SOLO_SPELL_ADVANCEMENT_ID) + ".description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion(String.valueOf(Mystical.SPELL_CURED_CRITERION.getId()),
                        new SpellCuredCriterion.Conditions(LootContextPredicate.EMPTY, NumberRange.FloatRange.atLeast(100), NumberRange.IntRange.exactly(1)))
                .parent(cureSpell)
                .build(consumer, SOLO_SPELL_ADVANCEMENT_ID);
        Advancement doubleCure = Advancement.Builder.createUntelemetered()
                .display(
                        Items.SPECTRAL_ARROW,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(DOUBLE_CURE_ADVANCEMENT_ID) + ".title"),
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(DOUBLE_CURE_ADVANCEMENT_ID) + ".description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        true
                )
                .criterion(String.valueOf(Mystical.SPELL_CURED_CRITERION.getId()),
                        new SpellCuredCriterion.Conditions(LootContextPredicate.EMPTY, NumberRange.FloatRange.atLeast(200)))
                .parent(soloSpell)
                .build(consumer, DOUBLE_CURE_ADVANCEMENT_ID);
        Advancement preventedBreaking = Advancement.Builder.createUntelemetered()
                .display(
                        Items.BEDROCK,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_ADVANCEMENT_ID) + ".title"),
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_ADVANCEMENT_ID) + ".description"),
                        null,
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                )
                .criterion(String.valueOf(Mystical.PREVENTED_BREAKING_CRITERION_ID), new ItemCriterion.Conditions(Mystical.PREVENTED_BREAKING_CRITERION_ID, LootContextPredicate.EMPTY, LootContextPredicate.EMPTY))
                .parent(cureSpell)
                .build(consumer, PREVENTED_BREAKING_ADVANCEMENT_ID);
        Advancement preventedBreakingAncientDebris = Advancement.Builder.createUntelemetered()
                .display(
                        Items.ANCIENT_DEBRIS,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID) + ".title"),
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID) + ".description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion(String.valueOf(Mystical.PREVENTED_BREAKING_CRITERION_ID), createPreventedBreakingCriterion(Blocks.ANCIENT_DEBRIS))
                .parent(preventedBreaking)
                .build(consumer, PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID);
    }

    /**
     * Create a criterion for being prevented from breaking a block.
     * @param block The block to require.
     * @return A new criterion.
     * @see ItemCriterion.Conditions#createPlacedBlock(Block)
     */
    public static ItemCriterion.Conditions createPreventedBreakingCriterion(Block block) {
        return new ItemCriterion.Conditions(Mystical.PREVENTED_BREAKING_CRITERION_ID, LootContextPredicate.EMPTY, LootContextPredicate.create(BlockStatePropertyLootCondition.builder(block).build()));
    }
}