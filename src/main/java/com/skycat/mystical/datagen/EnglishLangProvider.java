package com.skycat.mystical.datagen;

import com.skycat.mystical.spell.Spells;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
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
        addConfigOption(tb, "baseHavenCost", "Haven cost");
        addConfigOptionTooltip(tb, "baseHavenCost", "The amount of power required to haven a chunk.");

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
        addConsequenceTranslation(tb, "turboMobs", "failedGetRandomEntityType", "Failed to get a random entity type, using zombie instead.");

        // Commands
        addCommandText(tb, "mystical.help", "Mystical is a mod about spells cast by otherworldly beings. To learn more, click on one of the following commands (any path will teach you what you need to know):\n%s\n%s\n%s");

        addCommandText(tb, "mystical.credits", """
                ---
                CREDITS:
                skycatminepokie - Author
                SuperiorTabby - Code & texture contributor
                Phaserock - Texture contributor
                Implement - Texture contributor
                Members of the Fabric Discord - Lots of programming help, and too many people to name. Thank you guys!
                modmuss, player50, and the rest of the Fabric team - Fabric
                Patbox @ Nucleoid - Server Translations API
                Lucko - Permissions API""");

        addCommandText(tb, "mystical.spell.help", """
                ---
                Spells are cast by otherworldly beings, changing the world in strange ways.
                Sometimes, spells are beneficial. Sometimes, they are not.
                See the currently active spells with %s. Hover over them to discover the cure.
                To learn more about curing spells, use %s.
                If you have the mod installed, you can look at the effect HUD to see active spells.""");
        addCommandText(tb, "mystical.spell.delete.noSpells", "There are no active spells.");
        addCommandText(tb, "mystical.spell.new.success", "Successfully created new %s spell.");
        addCommandText(tb, "mystical.reload.success", "Successfully reloaded config and set night timer.");
        addCommandText(tb, "mystical.spell.list.noSpells", "There are no active spells.");
        addCommandText(tb, "mystical.spell.new.spell.warnDisabled", "Warning: Randomly generating this spell is disabled, or its weight is zero.");
        addCommandText(tb, "mystical.spell.delete.deleteButton", " [X]");
        addCommandText(tb, "mystical.spell.delete.success", "Deleted %d spells.");

        addCommandText(tb, "mystical.power.help", """
                ---
                Power can be used to haven chunks, protecting them from the influence of spells.
                Gain power by contributing to curing a spell (see %s for more info).
                Spend it on havens (see %s for more info).""");
        addCommandText(tb, "mystical.power.add.player.amount.success", "Successfully added %d power to %d player(s).");
        addCommandText(tb, "mystical.power.remove.player.amount.success", "Successfully removed %d power from %d player(s).");
        addCommandText(tb, "mystical.power.get.player", "%s has %d power.");

        addCommandText(tb, "mystical.haven.help", """
                ---
                A haven is a place of safety from spells.
                You can create a chunk-wide haven with power by using %s.
                To learn more about power, see %s
                To learn more about spells, see %s""");
        addCommandText(tb, "mystical.haven.info.inHaven", "This is in a haven.");
        addCommandText(tb, "mystical.haven.info.notInHaven", "This chunk is not havened.");
        addCommandText(tb, "mystical.haven.pos.action", "Havening chunk at [%d, %d] for %d power.");
        addCommandText(tb, "mystical.haven.pos.button", " [Confirm]");
        addCommandText(tb, "mystical.haven.pos.confirm.notEnoughPower", "You tried as hard as you could, but you couldn't haven the area. Do you have enough power?");

        addCommandText(tb, "generic.success", "Success!");

        addCommandText(tb, "generic.notAPlayer", "This command must be run by a player.");
        addCommandText(tb, "generic.notAPlayer.solution", "This command must be run by a player. Try %s.");
        addCommandText(tb, "generic.alreadyHavened", "This chunk is already havened.");
        addCommandText(tb, "generic.notAnEntity", "This must be called by an entity.");
        addCommandText(tb, "generic.clickToRunTheCommand", "Click to run the command!");

        // Advancements
        addAdvancementTranslation(tb, AdvancementProvider.CURE_SPELL_ADVANCEMENT_ID, "Mystical", "Not everything is as it seems...");
        addAdvancementTranslation(tb, AdvancementProvider.MAKE_HAVEN_ADVANCEMENT_ID, "An Invisible Fortress", "Ward a chunk from unknown forces");
        addAdvancementTranslation(tb, AdvancementProvider.SOLO_SPELL_ADVANCEMENT_ID, "Flying Solo", "Cure a spell all on your own");
        addAdvancementTranslation(tb, AdvancementProvider.DOUBLE_CURE_ADVANCEMENT_ID, "Shoot the Moon", "Cure a spell a little too much");
        addAdvancementTranslation(tb, AdvancementProvider.PREVENTED_BREAKING_ADVANCEMENT_ID, "No Trespassing", "Be magically blocked from mining");
        addAdvancementTranslation(tb, AdvancementProvider.PREVENTED_BREAKING_ANCIENT_DEBRIS_ADVANCEMENT_ID, "Hidden in the Depths of Despair", "Be magically prevented from mining Ancient Debris");
        addAdvancementTranslation(tb, AdvancementProvider.PREVENTED_BREAKING_DIAMOND_ORE_ADVANCEMENT_ID, "Worse than Glow Lichen", "Find diamonds, only to be unable to mine them");

        // Other
        addTextTranslation(tb, "events.spellsChange", "The world shifts...");
        addTextTranslation(tb, "events.cureSpell", "1 spell was cured this night.");
        addTextTranslation(tb, "events.cureSpells", "%d spells were cured this night.");
        addTextTranslation(tb, "events.newSpell", "1 new spell fell over the world.");
        addTextTranslation(tb, "events.newSpells", "%d new spells fell over the world.");
        addTextTranslation(tb, "spellGenerator.emptyConsequenceList", "SpellGenerator found an empty consequence supplier list. Using default consequence.");
        addTextTranslation(tb, "spellGenerator.allConsequencesDisabled", "All spells are disabled, skipping making a spell");
        addTextTranslation(tb, "spellGenerator.emptyCureList", "SpellGenerator found an empty cure list. Using default cure.");
        addTextTranslation(tb, "cure.kill", "Kill %ss");
        addTextTranslation(tb, "client.networkHandler.spellPacket.null", "Nbt from active spell packet was null. Skipping.");
        addTextTranslation(tb, "client.networkHandler.spellPacket.failedDeserialize", "Spells from active spell packet could not be deserialized. Skipping.");
        addTextTranslation(tb, "consequence.mysteryEggs.failedSpawn", "Unable to spawn a random egg mob.");
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
     * Adds translation for logging options. Prepends "{@code text.mystical.logging}" and adds a config option.
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
     * Add command translation. Prepends "{@code text.mystical.command.}".
     */
    private void addCommandText(TranslationBuilder tb, String key, String value) {
        tb.add("text.mystical.command." + key, value);
    }

    /**
     * Add text translations. Prepends "{@code text.mystical.}".
     */
    private void addTextTranslation(TranslationBuilder tb, String key, String value) {
        tb.add("text.mystical." + key, value);
    }

    private void addAdvancementTranslation(TranslationBuilder tb, String advancementKey, String title, String description) {
        String key = getKeyForAdvancementTranslation(advancementKey);
        tb.add(key + ".title", title);
        tb.add(key + ".description", description);
    }

    public static String getKeyForAdvancementTranslation(String advancementKey) {
        return "text.mystical.advancement." + advancementKey.substring(advancementKey.indexOf(':') + 1).replace('/', '.'); // Trim off "mystical:" and replace "/" with "."
    }
}
