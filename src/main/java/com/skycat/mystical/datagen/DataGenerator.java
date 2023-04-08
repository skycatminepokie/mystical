package com.skycat.mystical.datagen;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.SpellGenerator;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.spell.consequence.SpellConsequence;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(EnglishLangProvider::new);
    }
    private static class EnglishLangProvider extends FabricLanguageProvider {

        protected EnglishLangProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator, "en_us");
        }

        @Override
        public void generateTranslations(TranslationBuilder tb) {
            addConfig(tb, "title", "Mystical Config");

            addConfigSection(tb, "General");
            addConfigOption(tb, "devMode", "Dev mode");

            addConfigSection(tb, "Logging");
            addConfigOption(tb, "failedToGetRandomBlockLogLevel", "Failed to get random block");
            addConfigOption(tb, "failedToLoadHavenManagerLogLevel", "Failed to load haven manager");
            addConfigOption(tb, "failedToLoadSpellHandlerLogLevel", "Failed to load spell handler");
            addConfigOption(tb, "failedToSaveHavenManagerLogLevel", "Failed to save haven manager");
            addConfigOption(tb, "failedToSaveSpellHandlerLogLevel", "Failed to save spell manager");
            addConfigOption(tb, "failedToSetNightTimerLogLevel", "Failed to set night timer");
            addConfigOption(tb, "playerContributedLogLevel", "Player contribution");
            addConfigOption(tb, "timeOfDayAtStartupLogLevel", "Time of day at startup");
            addConfigOption(tb, "newSpellCommandLogLevel", "New spell command");

            addConfigSection(tb, "Spells");
            // Generate translations for consequences
            for (ConsequenceFactory<?> factory : SpellGenerator.getShortNameToFactory().values()) {
                SpellConsequence consequence = factory.make(Mystical.RANDOM, 0);
                addConfigSpell(tb, consequence);
            }

            addConfig(tb, "enum.logLevel.debug", "Debug");
            addConfig(tb, "enum.logLevel.error", "Error");
            addConfig(tb, "enum.logLevel.info", "Info");
            addConfig(tb, "enum.logLevel.off", "No logging");
            addConfig(tb, "enum.logLevel.warn", "Warn");

            Optional<Path> file = dataGenerator.getModContainer().findPath("assets/mystical/lang/en_us.existing.json");
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
         * @param tb The builder
         * @param consequence The consequence to add
         */
        private void addConfigSpell(TranslationBuilder tb, SpellConsequence consequence) {
            // General stuff
            String shortName = consequence.getShortName();
            String longName = consequence.getLongName();
            tb.add(consequence.getShortNameKey(), shortName); // Short name
            tb.add(consequence.getLongNameKey(), longName); // Long name
            tb.add(consequence.getDescriptionKey(), consequence.getDescription()); // Description
            // Config stuff
            addConfigCategory(tb, shortName, longName); // Category
            addConfigOptionInCategory(tb, shortName, "enabled", "Enable?"); // Enabled option
            addConfigOptionInCategory(tb, shortName, "logLevel", "Logging"); // Logging option
            addConfigOptionInCategory(tb, shortName, "weight", "Weight"); // Weight option

        }

        private void addConfigCategory(TranslationBuilder tb, String key, String value) {
            addConfig(tb, "category." + key, value);
        }


    }
}