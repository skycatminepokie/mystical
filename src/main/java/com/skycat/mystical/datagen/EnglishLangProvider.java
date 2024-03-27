package com.skycat.mystical.datagen;

import com.skycat.mystical.common.spell.Spells;
import com.skycat.mystical.common.spell.consequence.ConsequenceFactory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

class EnglishLangProvider extends FabricLanguageProvider {

    protected EnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder tb) {
        // Config
        addConfig(tb, "title", "Mystical Config");

        addConfigSection(tb, "General");
        addConfigOption(tb, "devMode", "Dev mode");
        addConfigOption(tb, "spellMaxHard", "Max spells (Hard)");
        addConfigOptionTooltip(tb, "spellMaxHard", "The maximum number of spells active at a time.\nMystical will not delete extra spells, but won't make any past this point.");
        addConfigOption(tb, "spellMinHard", "Min spells (Hard)");
        addConfigOptionTooltip(tb, "spellMinHard", "The minimum number of spells active at a time.\nMystical will make sure there are this many spells active whenever spell rewards are paid out.");
        addConfigOption(tb, "spellDecay", "Spell decay (%)");
        addConfigOptionTooltip(tb, "spellDecay", "How much spells will decay each night.");
        addConfigOption(tb, "spellDecayLinear", "Decay spells linearly?");
        addConfigOptionTooltip(tb, "spellDecayLinear", "If true, spells will decay based on the full requirement of the spell.\nIf false, they will decay based on what's left.");

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
        addCommandText(tb, "mystical.spell.delete.deleteButton", " [X]");

        addConfigSection(tb, "Spells");
        // Config + spells
        for (ConsequenceFactory<?> factory : Spells.getShortNameToFactory().values()) {
            addConfigSpell(tb, factory);
        }
        addConfigOption(tb, "bigCreeperExplosion.multiplier", "Multiplier");
        addConfigOption(tb, "fishingRodLaunch.multiplier", "Multiplier");
        addConfigOption(tb, "turboChickens.speed", "Speed multiplier");
        addConfigOptionTooltip(tb, "turboChickens.speed", "Actually this is a duration divisor. The egg-laying cooldown will be divided by this.\nAccepts positive, nonzero numbers.");
        addConfigOption(tb, "randomCreeperEffectClouds.effectDuration", "Effect duration (s)");
        addConfigOption(tb, "randomCreeperEffectClouds.effectAmplifier", "Effect amplifier");
        addConfigOptionTooltip(tb, "randomCreeperEffectClouds.effectAmplifier", "0 = level one, just like the /effect command");
        addConfigOption(tb, "soundSwap.numberOfSwaps", "Number of swaps");
        addConfigOptionTooltip(tb, "soundSwap.numberOfSwaps", "Not all swaps will be heard (like rare sounds) or even take effect (for example block sounds don't work).\nThat's why this is so big by default.\nAccepts positive, nonzero integers.");

        // Config enums
        addConfig(tb, "enum.logLevel.debug", "Debug");
        addConfig(tb, "enum.logLevel.error", "Error");
        addConfig(tb, "enum.logLevel.info", "Info");
        addConfig(tb, "enum.logLevel.off", "No logging");
        addConfig(tb, "enum.logLevel.warn", "Warn");

        // Additional spells
        addConsequenceTranslation(tb, "unbreakableLocation", "noBreaking", "A mystical force prevents you from breaking that block.");

        // Advancements
        addAdvancementTranslation(tb, AdvancementProvider.CURE_SPELL_ADVANCEMENT_ID, "Watch Your Step!", "Not everything is as it seems...");
        addAdvancementTranslation(tb, AdvancementProvider.MAKE_HAVEN_ADVANCEMENT_ID, "An Invisible Fortress", "Ward a chunk from unknown forces");
        addAdvancementTranslation(tb, AdvancementProvider.SOLO_SPELL_ADVANCEMENT_ID, "Flying Solo", "Cure a spell all on your own");
        addAdvancementTranslation(tb, AdvancementProvider.DOUBLE_CURE_ADVANCEMENT_ID, "Shoot the Moon", "Now do it blindfolded");
        addAdvancementTranslation(tb, AdvancementProvider.PREVENTED_BREAKING_ADVANCEMENT_ID, "No Trespassing", "Be magically blocked from mining");
        addAdvancementTranslation(tb, AdvancementProvider.PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID, "Hidden in the Depths of Despair", "Be magically prevented from mining Ancient Debris");

        // Other
        addTextTranslation(tb, "events.spellsChange", "The world shifts...");
        addTextTranslation(tb, "events.cureSpell", "1 spell was cured this night.");
        addTextTranslation(tb, "events.cureSpells", "%d spells were cured this night.");
        addTextTranslation(tb, "events.newSpell", "1 new spell fell over the world.");
        addTextTranslation(tb, "events.newSpells", "%d new spells fell over the world.");
        addTextTranslation(tb, "spellGenerator.emptyConsequenceList", "SpellGenerator found an empty consequence supplier list. Using default consequence.");
        addTextTranslation(tb, "text.mystical.spellGenerator.emptyCureList", "SpellGenerator found an empty cure list. Using default cure.");
        addTextTranslation(tb, "cure.kill", "Kill %ss");
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
        tb.add("text.config.mysticalConfig.option." + shortName + ".chance", "% chance"); // Chance
        tb.add(factory.translationKey() + ".fired", "Spell " + shortName + ": " + factory.getFiredMessage() + ".");
        // Config stuff
        addConfigCategory(tb, shortName, longName); // Category
        addConfigOptionInCategory(tb, shortName, "enabled", "Enable?"); // Enabled option
        addConfigOptionInCategory(tb, shortName, "logLevel", "Logging"); // Logging option
        addConfigOptionInCategory(tb, shortName, "weight", "Weight"); // Weight option
    }

    private void addConsequenceTranslation(TranslationBuilder tb, String consequence, String key, String value) {
        addConsequenceTranslation(tb, consequence + "." + key, value);
    }

    private void addConsequenceTranslation(TranslationBuilder tb, String key, String value) {
        tb.add("text.mystical.consequence." + key, value);
    }

    private void addConfigCategory(TranslationBuilder tb, String key, String value) {
        addConfig(tb, "category." + key, value);
    }

    /**
     * Adds translation for logging options. Prepends `"text.mystical.logging"` and adds a config option.
     * @param tb      The TranslationBuilder to use.
     * @param key     The key under the logging section.
     * @param console The translation for console output.
     * @param option  The translation for the config option.
     */
    private void addLoggingOption(TranslationBuilder tb, String key, String console, String option) {
        tb.add("text.mystical.logging." + key, console);
        addConfigOption(tb, key + "LogLevel", option);
    }

    /**
     * Add command translation. Prepends `"text.mystical.command."`.
     */
    private void addCommandText(TranslationBuilder tb, String key, String value) {
        tb.add("text.mystical.command." + key, value);
    }

    /**
     * Add text translations. Prepends `"text.mystical."`.
     */
    private void addTextTranslation(TranslationBuilder tb, String key, String value) {
        tb.add("text.mystical." + key, value);
    }

    private void addAdvancementTranslation(TranslationBuilder tb, String advancementKey, String title, String description) {
        String key = getKeyForAdvancementTranslation(advancementKey);
        tb.add(advancementKey + ".title", title);
        tb.add(advancementKey + ".description", description);
    }

    public static String getKeyForAdvancementTranslation(String advancementKey) {
        return "text.mystical.advancement." + advancementKey.replace('/', '.');
    }
}
