package com.skycat.mystical.datagen;

import com.skycat.mystical.common.spell.SpellGenerator;
import com.skycat.mystical.common.spell.consequence.ConsequenceFactory;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(EnglishLangProvider::new);
        pack.addProvider(EntityTypeTagProvider::new);
    }
    private static class EnglishLangProvider extends FabricLanguageProvider {

        protected EnglishLangProvider(FabricDataOutput dataOutput) {
            super(dataOutput, "en_us");
        }

        @Override
        public void generateTranslations(TranslationBuilder tb) {
            addConfig(tb, "title", "Mystical Config");

            addConfigSection(tb, "General");
            addConfigOption(tb, "devMode", "Dev mode");
            addConfigOption(tb, "spellMaxHard", "Max spells (Hard)");
            addConfigOption(tb, "spellMinHard", "Min spells (Hard)");
            addConfigOptionTooltip(tb, "spellMaxHard", "The maximum number of spells active at a time.\nMystical will not delete extra spells, but won't make any past this point.");
            addConfigOptionTooltip(tb, "spellMinHard", "The minimum number of spells active at a time.\nMystical will make sure there are this many spells active whenever spell rewards are paid out.");

            addConfigSection(tb, "Logging");
            addLoggingOption(tb, "newSpellCommand", "New spell created using command", "New spell command (console)");
            addConfigOption(tb, "newSpellCommandBroadcast", "New spell command (in-game)");
            addLoggingOption(tb, "failedToGetRandomBlock", "Failed to get random block, using a command block instead.", "Failed to get random block");
            addLoggingOption(tb, "failedToLoadHavenManager", "Failed to load haven manager, making a new one instead.", "Failed to load haven manager");
            addLoggingOption(tb, "failedToLoadSpellHandler", "Failed to load spell handler, making a new one instead.", "Failed to load spell handler");
            addLoggingOption(tb, "failedToSaveSpellHandler", "Failed to save spell manager.", "Failed to save spell manager");
            addLoggingOption(tb, "failedToSetNightTimer", "Failed to set night timer because %s", "Failed to set night timer");
            addLoggingOption(tb, "spellContribution", "Spell contribution by %s. Amount: %d.", "Spell contribution");
            addLoggingOption(tb, "timeOfDayAtStartup", "Time of day at startup: %l.", "Time of day at startup");

            addCommandText(tb, "mystical.spell.delete.noSpells", "There are no active spells.");
            addCommandText(tb, "mystical.spell.new.success", "Successfully created new %s spell.");
            addCommandText(tb, "mystical.reload.success", "Successfully reloaded config and set night timer.");
            addCommandText(tb, "mystical.spell.list.noSpells", "There are no active spells.");
            addCommandText(tb, "mystical.spell.new.spell.warnDisabled", "Warning: Randomly generating this spell is disabled, or its weight is zero.");

            addConfigSection(tb, "Spells");
            // Generate translations for consequences
            for (ConsequenceFactory<?> factory : SpellGenerator.getShortNameToFactory().values()) {
                addConfigSpell(tb, factory);
            }

            addConfig(tb, "enum.logLevel.debug", "Debug");
            addConfig(tb, "enum.logLevel.error", "Error");
            addConfig(tb, "enum.logLevel.info", "Info");
            addConfig(tb, "enum.logLevel.off", "No logging");
            addConfig(tb, "enum.logLevel.warn", "Warn");

            Optional<Path> file = dataOutput.getModContainer().findPath("assets/mystical/lang/en_us.existing.json");
            if (file.isPresent()){
                try {
                    tb.add(file.get());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to add existing language file", e);
                }
            } else {
                System.out.println("Warning: no existing language file found");
            }
        }

        private void addConfig(TranslationBuilder tb, String key, String value) {
            tb.add("text.config.mysticalConfig." + key, value);
        }

        private void addConfigOption(TranslationBuilder tb, String key, String value) {
            addConfig(tb, "option." + key, value);
        }

        private void addConfigOptionInCategory(TranslationBuilder tb, String category, String key, String value) {
            addConfigOption(tb, category + "." + key, value);
        }

        private void addConfigOptionTooltip(TranslationBuilder tb, String key, String value) {
            addConfigOption(tb, key + ".tooltip", value);
        }

        private void addConfigSection(TranslationBuilder tb, String section) {
            addConfig(tb, "section." + section, section);
        }

        /**
         * Adds translation information for a consequence, along with its category.
         * Note that this only adds generic options (enable, logging, and weight)
         *
         * @param tb      The builder
         * @param factory The factory of the consequence to add translations for
         */
        private void addConfigSpell(TranslationBuilder tb, ConsequenceFactory<?> factory) {
            // General stuff
            String shortName = factory.getShortName();
            String longName = factory.getLongName();
            tb.add(factory.getShortNameKey(), shortName); // Short name
            tb.add(factory.getLongNameKey(), longName); // Long name
            tb.add(factory.getDescriptionKey(), factory.getDescription()); // Description
            tb.add(factory.translationKey() + ".fired", "Spell " + shortName + ": " + factory.getFiredMessage() + ".");
            // Config stuff
            addConfigCategory(tb, shortName, longName); // Category
            addConfigOptionInCategory(tb, shortName, "enabled", "Enable?"); // Enabled option
            addConfigOptionInCategory(tb, shortName, "logLevel", "Logging"); // Logging option
            addConfigOptionInCategory(tb, shortName, "weight", "Weight"); // Weight option
        }

        private void addConfigCategory(TranslationBuilder tb, String key, String value) {
            addConfig(tb, "category." + key, value);
        }

        /**
         * @param tb      The TranslationBuilder to use.
         * @param key     The key under the logging section.
         * @param console The translation for config output.
         * @param option  The translation for the config option.
         */
        private void addLoggingOption(TranslationBuilder tb, String key, String console, String option) {
            tb.add("text.mystical.logging." + key, console);
            addConfigOption(tb, key + "LogLevel", option);
        }

        private void addCommandText(TranslationBuilder tb, String key, String value) {
            tb.add("text.mystical.command." + key, value);
        }

    }
    private static class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
        private static final TagKey<EntityType<?>> BOSSES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:bosses"));
        // Using tags like this will make startup time a bit longer, but will allow for compatibility
        private static final TagKey<EntityType<?>> ZOMBIE_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:zombie_variants"));
        private static final TagKey<EntityType<?>> SKELETON_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:skeleton_variants"));
        private static final TagKey<EntityType<?>> ENDERMAN_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:enderman_variants"));

        public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(BOSSES)
                    .add(EntityType.ENDER_DRAGON)
                    .add(EntityType.WITHER)
                    .add(EntityType.WARDEN)
                    .add(EntityType.ELDER_GUARDIAN)
                    .addOptionalTag(new Identifier("c:bosses"));
            getOrCreateTagBuilder(ZOMBIE_VARIANTS)
                    .add(EntityType.ZOMBIE)
                    .add(EntityType.HUSK)
                    .add(EntityType.ZOMBIE_VILLAGER)
                    .add(EntityType.ZOMBIFIED_PIGLIN)
                    .add(EntityType.DROWNED);
            getOrCreateTagBuilder(SKELETON_VARIANTS)
                    .add(EntityType.SKELETON)
                    .add(EntityType.WITHER_SKELETON)
                    .add(EntityType.STRAY);
            getOrCreateTagBuilder(ENDERMAN_VARIANTS)
                    .add(EntityType.ENDERMAN)
                    .add(EntityType.ENDERMITE);
        }
    }
}
