package com.skycat.mystical;

import com.skycat.mystical.util.LogLevel;
import io.wispforest.owo.config.annotation.*;

@SuppressWarnings("unused") // They are used by owo-config
@Modmenu(modId = "mystical")
@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel {
    @SectionHeader("General")
    public boolean devMode = false; // Not implemented
    public int spellMaxHard = 3;
    public int spellMinHard = 0;
    public double spellDecay = 10.0;
    public boolean spellDecayLinear = false;
    public int baseHavenCost = 1000;

    @SectionHeader("Spells")
    @Nest public BigCreeperExplosionConfig bigCreeperExplosion = new BigCreeperExplosionConfig();
    @Nest public CatVariantChangeConfig catVariantChange = new CatVariantChangeConfig();
    @Nest public EnderTypeChangeConfig enderTypeChange = new EnderTypeChangeConfig();
    @Nest public FishingRodLaunchConfig fishingRodLaunch = new FishingRodLaunchConfig();
    @Nest public LevitateConfig levitate = new LevitateConfig();
    @Nest public RandomTreeTypeConfig randomTreeType = new RandomTreeTypeConfig();
    @Nest public SheepColorChangeConfig sheepColorChange = new SheepColorChangeConfig();
    @Nest public ZombieTypeChangeConfig zombieTypeChange = new ZombieTypeChangeConfig();
    @Nest public SkeletonTypeChangeConfig skeletonTypeChange = new SkeletonTypeChangeConfig();
    @Nest public DisableDaylightBurningConfig disableDaylightBurning = new DisableDaylightBurningConfig();
    @Nest public NoFuseConfig noFuse = new NoFuseConfig();
    @Nest public MobSpawnSwapConfig mobSpawnSwap = new MobSpawnSwapConfig();
    @Nest public AggressiveGolemsConfig aggressiveGolems = new AggressiveGolemsConfig();
    @Nest public UnbreakableLocationConfig unbreakableLocation = new UnbreakableLocationConfig();
    @Nest public TurboChickensConfig turboChickens = new TurboChickensConfig();
    @Nest public OneStrikeWardensConfig oneStrikeWardens = new OneStrikeWardensConfig();
    @Nest public RandomCreeperEffectCloudsConfig randomCreeperEffectClouds = new RandomCreeperEffectCloudsConfig();
    @Nest public TurboMobsConfig turboMobs = new TurboMobsConfig();
    @Nest public RandomEvokerSummonsConfig randomEvokerSummons = new RandomEvokerSummonsConfig();
    @Nest public IllusionersReplaceEvokersConfig illusionersReplaceEvokers = new IllusionersReplaceEvokersConfig();
    @Nest public ExplosionsInfestConfig explosionsInfest = new ExplosionsInfestConfig();
    @Nest public BoldSlimesConfig boldSlimes = new BoldSlimesConfig();
    @Nest public ChangingArmorHurtsConfig changingArmorHurts = new ChangingArmorHurtsConfig();
    @Nest public SoundSwapConfig soundSwap = new SoundSwapConfig();
    @Nest public FishingRodSwapConfig fishingRodSwap = new FishingRodSwapConfig();
    @Nest public MysteryEggsConfig mysteryEggs = new MysteryEggsConfig();

    @SectionHeader("Logging") // Note: Logging as ERROR level does not always mean a critical error.
    public LogLevel failedToSetNightTimerLogLevel = LogLevel.WARN;
    public LogLevel timeOfDayAtStartupLogLevel = LogLevel.DEBUG;
    public LogLevel failedToLoadHavenManagerLogLevel = LogLevel.INFO;
    public LogLevel spellContributionLogLevel = LogLevel.OFF;
    public LogLevel failedToGetRandomBlockLogLevel = LogLevel.ERROR;
    public LogLevel failedToLoadSpellHandlerLogLevel = LogLevel.WARN;
    public LogLevel failedToSaveSpellHandlerLogLevel = LogLevel.ERROR;
    public LogLevel newSpellCommandLogLevel = LogLevel.INFO;
    public boolean newSpellCommandBroadcast = true;

    public static class BigCreeperExplosionConfig {
        public boolean enabled = true; // Not implemented
        public double multiplier = 2.0;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1; // Not implemented

        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class CatVariantChangeConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class EnderTypeChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class FishingRodLaunchConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public double multiplier = 20;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class LevitateConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class RandomTreeTypeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class SheepColorChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class SkeletonTypeChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class ZombieTypeChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class DisableDaylightBurningConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class NoFuseConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class MobSpawnSwapConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class AggressiveGolemsConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class UnbreakableLocationConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 5.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class TurboChickensConfig {
      public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("positiveNonzeroPredicate")
        public double speed = 10;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
        public static boolean positiveNonzeroPredicate(double value) {
            return ConfigModel.positiveNonzeroPredicate(value);
        }
    }
    
    public static class OneStrikeWardensConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
    
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class TurboMobsConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class RandomCreeperEffectCloudsConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @RangeConstraint(min = 1, max = 1000000)
        public int effectDuration = 10; // In seconds, similar to vanilla command
        @RangeConstraint(min = 0, max = 255)
        public int effectAmplifier = 0; // 0 = level 1, similar to vanilla command

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class RandomEvokerSummonsConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }
    }

    public static class IllusionersReplaceEvokersConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }
    }

    public static class ExplosionsInfestConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }
    }

    public static class BoldSlimesConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class ChangingArmorHurtsConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }
    }

    public static class SoundSwapConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("positiveNonzeroIntPredicate")
        public int numberOfSwaps = 50;
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
        public static boolean positiveNonzeroIntPredicate(int value) {
            return ConfigModel.positiveNonzeroIntPredicate(value);
        }
    }

    public static class FishingRodSwapConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class MysteryEggsConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }
        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    /**
     * Verify that the chance is valid.
     * Used instead of {@link RangeConstraint} because it doesn't make a slider.
     *
     * @param value The chance value to check
     * @return value >= 0 && value <= 100
     */
    public static boolean chancePredicate(double value) {
        return value >= 0 && value <= 100;
    }

    /**
     * Verify that the given weight is valid.
     * Used instead of {@link RangeConstraint} because it doesn't make a slider.
     * @param value The weight to check
     * @return value >= 0
     */
    public static boolean weightPredicate(double value) {
        return value >= 0;
    }
    public static boolean percentPredicate(double value) {
        return value >= 0 && value <= 100;
    }
    public static boolean positivePredicate(double value) {
        return value >= 0;
    }

    public static boolean positiveNonzeroPredicate(double value) {
        return positivePredicate(value) && value != 0;
    }
    public static boolean positiveNonzeroIntPredicate(int value) {
        return value > 0;
    }

}