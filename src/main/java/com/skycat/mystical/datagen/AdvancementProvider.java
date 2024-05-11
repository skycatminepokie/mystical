package com.skycat.mystical.datagen;

import com.skycat.mystical.MysticalCriteria;
import com.skycat.mystical.advancement.MakeHavenCriterion;
import com.skycat.mystical.advancement.SpellCuredCriterion;
import com.skycat.mystical.util.Utils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    public static final String CURE_SPELL_ADVANCEMENT_ID = "mystical:mystical";
    public static final String MAKE_HAVEN_ADVANCEMENT_ID = "mystical:mystical/make_haven";
    public static final String SOLO_SPELL_ADVANCEMENT_ID = "mystical:mystical/solo_spell";
    public static final String DOUBLE_CURE_ADVANCEMENT_ID = "mystical:mystical/double_cure";
    public static final String PREVENTED_BREAKING_ADVANCEMENT_ID = "mystical:mystical/prevented_breaking";
    public static final String PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID = "mystical:mystical/prevented_breaking/ancient_debris";
    public static final String PREVENTED_BREAKING_DIAMOND_ORE_ADVANCEMENT_ID = "mystical:mystical/prevented_breaking/diamond_ore";

    protected AdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> wrapperFuture) {
        super(output, wrapperFuture);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry cureSpell = Advancement.Builder.createUntelemetered()
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
                .criterion(String.valueOf(MysticalCriteria.SPELL_CURED_CRITERION.getId()), MysticalCriteria.SPELL_CURED_CRITERION.create(new SpellCuredCriterion.Conditions(null, NumberRange.DoubleRange.atLeast(0.2), null)))
                .build(consumer, CURE_SPELL_ADVANCEMENT_ID);
        AdvancementEntry makeHaven = Advancement.Builder.createUntelemetered()
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
                .criterion(String.valueOf(MysticalCriteria.MAKE_HAVEN_CRITERION.getId()), MysticalCriteria.MAKE_HAVEN_CRITERION.create(new MakeHavenCriterion.Conditions(Optional.empty())))
                .parent(cureSpell)
                .build(consumer, MAKE_HAVEN_ADVANCEMENT_ID);
        AdvancementEntry soloSpell = Advancement.Builder.createUntelemetered()
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
                .criterion(String.valueOf(MysticalCriteria.SPELL_CURED_CRITERION.getId()),
                        MysticalCriteria.SPELL_CURED_CRITERION.create(new SpellCuredCriterion.Conditions(null, NumberRange.DoubleRange.atLeast(100), NumberRange.IntRange.exactly(1))))
                .parent(cureSpell)
                .build(consumer, SOLO_SPELL_ADVANCEMENT_ID);
        AdvancementEntry doubleCure = Advancement.Builder.createUntelemetered()
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
                .criterion(String.valueOf(MysticalCriteria.SPELL_CURED_CRITERION.getId()),
                        MysticalCriteria.SPELL_CURED_CRITERION.create(new SpellCuredCriterion.Conditions(null, NumberRange.DoubleRange.atLeast(200), null)))
                .rewards(AdvancementRewards.Builder.experience(50))
                .parent(soloSpell)
                .build(consumer, DOUBLE_CURE_ADVANCEMENT_ID);
        AdvancementEntry preventedBreaking = Advancement.Builder.createUntelemetered()
                .display(
                        Items.BEDROCK,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_ADVANCEMENT_ID) + ".title"),
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_ADVANCEMENT_ID) + ".description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        false,
                        false
                )
                .criterion(String.valueOf(MysticalCriteria.PREVENTED_BREAKING_CRITERION_ID),
                        MysticalCriteria.PREVENTED_BREAKING_CRITERION.create(
                                new ItemCriterion.Conditions(Optional.empty(),
                                        Optional.of(LootContextPredicate.create(BlockStatePropertyLootCondition.builder(Blocks.STONE).or(BlockStatePropertyLootCondition.builder(Blocks.GRASS_BLOCK)).build()))
                                )) // Note: names of arguments seem to swapped in Yarn
                        ) // TODO: Make this work for anything
                .parent(cureSpell)
                .build(consumer, PREVENTED_BREAKING_ADVANCEMENT_ID);
        AdvancementEntry preventedBreakingAncientDebris = Advancement.Builder.createUntelemetered()
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
                .criterion(String.valueOf(MysticalCriteria.PREVENTED_BREAKING_CRITERION_ID), createPreventedBreakingCriterion(Blocks.ANCIENT_DEBRIS))
                .parent(preventedBreaking)
                .build(consumer, PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID);
        AdvancementEntry preventedBreakingDiamondOre = Advancement.Builder.createUntelemetered()
                .display(
                        Items.DIAMOND_ORE,
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_DIAMOND_ORE_ADVANCEMENT_ID) + ".title"),
                        Utils.translatable(EnglishLangProvider.getKeyForAdvancementTranslation(PREVENTED_BREAKING_DIAMOND_ORE_ADVANCEMENT_ID) + ".description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion(String.valueOf(MysticalCriteria.PREVENTED_BREAKING_CRITERION_ID),
                        MysticalCriteria.PREVENTED_BREAKING_CRITERION.create(new ItemCriterion.Conditions(Optional.empty(), Optional.of(LootContextPredicate.create(BlockStatePropertyLootCondition.builder(Blocks.DIAMOND_ORE).or(BlockStatePropertyLootCondition.builder(Blocks.DEEPSLATE_DIAMOND_ORE)).build()))))) // TODO: Make this use a tag instead, then make one for shulker boxes
                .parent(preventedBreaking)
                .build(consumer, PREVENTED_BREAKING_DIAMOND_ORE_ADVANCEMENT_ID);
    }

    /**
     * Create a criterion for being prevented from breaking a block.
     *
     * @param block The block to require.
     * @return A new criterion.
     * @see ItemCriterion.Conditions#createPlacedBlock(Block)
     */
    public static AdvancementCriterion<?> createPreventedBreakingCriterion(Block block) {
        return MysticalCriteria.PREVENTED_BREAKING_CRITERION.create(new ItemCriterion.Conditions(Optional.empty(), Optional.of(LootContextPredicate.create(BlockStatePropertyLootCondition.builder(block).build()))));
    }
}
